package com.nookure.staff.database;

import static java.util.Objects.requireNonNull;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.config.partials.DatabaseConfig;
import com.nookure.staff.api.database.AbstractPluginConnection;
import com.nookure.staff.api.database.DataProvider;
import com.nookure.staff.api.database.repository.StaffStateRepository;
import com.nookure.staff.api.model.NoteModel;
import com.nookure.staff.api.model.PinModel;
import com.nookure.staff.api.model.PlayerModel;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.ContainerConfig;
import io.ebean.datasource.DataSourceConfig;
import io.ebean.platform.mysql.MySqlPlatform;
import io.ebean.platform.sqlite.SQLitePlatform;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.sql.DataSource;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PluginConnection extends AbstractPluginConnection {
    @Inject
    private Logger logger;

    @Inject
    private NookureStaff plugin;

    @Inject
    private AtomicReference<Database> databaseReference;

    @Inject
    private AtomicReference<DataSource> dataSource;

    @Inject
    private StaffStateRepository staffStateRepository;

    private ClassLoader classLoader;
    private Connection connection;
    private HikariDataSource hikariDataSource;
    private Database database;

    @Override
    public void connect(@NotNull DatabaseConfig config, ClassLoader classLoader) {
        requireNonNull(config, "config is null");

        this.classLoader = classLoader;

        logger.debug("Connecting to the database");
        logger.debug("Debug database data: " + config);

        if (connection != null) {
            return;
        }

        if (config.getType() == DataProvider.MYSQL) loadMySQL(config);
        else loadSqlite(config);

        databaseReference.set(database);
        dataSource.set(hikariDataSource);
        staffStateRepository.migrate();
        logger.info("<green>Successfully connected to the database!");
    }

    private void loadMySQL(@NotNull DatabaseConfig config) {
        requireNonNull(config, "config is null");

        HikariConfig hikariConfig = getHikariConfig(config);

        try {
            hikariDataSource = new HikariDataSource(hikariConfig);
            connection = hikariDataSource.getConnection();
            loadEbean(config);
        } catch (Exception e) {
            logger.severe("An error occurred while connecting to the database");
            logger.severe("Now trying to connect to SQLite");
            logger.severe(e);
            loadSqlite(config);
        }
    }

    private void loadEbean(DatabaseConfig config) {
        io.ebean.config.DatabaseConfig ebeanConfig = getDatabaseConfig(config);
        Thread.currentThread().setContextClassLoader(classLoader);
        database = DatabaseFactory.create(ebeanConfig);
    }

    @NotNull private io.ebean.config.DatabaseConfig getDatabaseConfig(DatabaseConfig config) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(hikariDataSource.getJdbcUrl());

        if (config.getType() == DataProvider.MYSQL) {
            dataSourceConfig.setUsername(hikariDataSource.getUsername());
            dataSourceConfig.setPassword(hikariDataSource.getPassword());
        }

        dataSourceConfig.setName(DATABASE_NAME);

        io.ebean.config.DatabaseConfig ebeanConfig = new io.ebean.config.DatabaseConfig();

        ebeanConfig.loadFromProperties();

        ContainerConfig containerConfig = new ContainerConfig();
        containerConfig.setActive(false);

        ebeanConfig.setDataSourceConfig(dataSourceConfig);

        ebeanConfig.setName("nkstaff");
        ebeanConfig.setRunMigration(true);
        ebeanConfig.setClasses(List.of(PlayerModel.class, NoteModel.class, PinModel.class));
        ebeanConfig.setDataSource(hikariDataSource);
        ebeanConfig.setDefaultServer(true);

        if (config.getType() == DataProvider.MYSQL) {
            ebeanConfig.setDatabasePlatform(new MySqlPlatform());
        } else {
            ebeanConfig.setDatabasePlatform(new SQLitePlatform());
        }

        return ebeanConfig;
    }

    @NotNull private HikariConfig getHikariConfig(@NotNull DatabaseConfig config) {
        requireNonNull(config, "config is null");

        HikariConfig hikariConfig = new HikariConfig();
        if (config.getType() == DataProvider.MYSQL) {
            hikariConfig.setJdbcUrl("jdbc:mysql://"
                    + config.getHost()
                    + ":"
                    + config.getPort()
                    + "/"
                    + config.getDatabase()
                    + "?autoReconnect=true&useUnicode=yes");
            hikariConfig.setUsername(config.getUsername());
            hikariConfig.setPassword(config.getPassword());
        } else {
            hikariConfig.setJdbcUrl("jdbc:sqlite:" + plugin.getPluginDataFolder() + "/database.db");
        }

        hikariConfig.setMaximumPoolSize(20);
        hikariConfig.setConnectionTimeout(30000);
        hikariConfig.setAutoCommit(false);
        hikariConfig.setLeakDetectionThreshold(0);
        return hikariConfig;
    }

    private void loadSqlite(DatabaseConfig config) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            logger.severe("<red>┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            logger.severe("<red>┃ The SQLite driver couldn't be found, contact nookure support             ┃");
            logger.severe("<red>┃ to get help with this issue.                                             ┃");
            logger.severe("<red>┃                                                                          ┃");
            logger.severe("<red>┃ This is a fatal error, the plugin will now disable itself.               ┃");
            logger.severe("<red>┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

            throw new RuntimeException(e);
        }

        try {
            String url = "jdbc:sqlite:" + plugin.getPluginDataFolder() + "/database.db";
            connection = DriverManager.getConnection(url);
            hikariDataSource = new HikariDataSource(getHikariConfig(config));
            loadEbean(config);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reload(@NotNull DatabaseConfig config, @NotNull ClassLoader classLoader) {
        close();
        connect(config, classLoader);
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (hikariDataSource != null) {
            hikariDataSource.close();
        }

        if (database != null) {
            database.shutdown(true, false);
        }
    }

    @Override
    public @NotNull Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("The connection is not established");
        }

        return connection;
    }

    @Override
    public Database getEbeanDatabase() {
        if (connection == null) {
            throw new IllegalStateException("The connection is not established");
        }

        return database;
    }
}
