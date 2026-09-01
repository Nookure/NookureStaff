package com.nookure.staff.api.database;

import com.nookure.staff.api.config.partials.DatabaseConfig;
import io.ebean.Database;
import java.sql.Connection;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a connection to the database.
 * Use {@link #connect(DatabaseConfig, ClassLoader)} to connect to the database.
 */
public abstract class AbstractPluginConnection {
    public static final String DATABASE_NAME = "nkstaff";

    /**
     * Connects to the database.
     * <p>
     * This method should be called before any other method.
     * If the connection is already established, this method should do nothing.
     * If the connection is not established, this method should establish it.
     * If the connection is not established, and the connection fails, the plugin should
     * try using SQLite.
     * If the connection is not established, and the connection fails, and SQLite fails,
     * the plugin should disable itself.
     * </p>
     *
     * @param config the database config
     * @see DatabaseConfig
     */
    public abstract void connect(@NotNull DatabaseConfig config, ClassLoader classLoader);

    /**
     * Closes the connection to the database.
     *
     * <p>
     * This method should be called when the plugin is disabled.
     * If the connection is already closed, this method should do nothing.
     * </p>
     */
    public abstract void close();

    /**
     * Returns the connection to the database.
     * this should be called after {@link #connect(DatabaseConfig, ClassLoader)}.
     * <p>
     * If the connection is not established, this method should throw an exception.
     * If the connection is established, this method should return the connection.
     * </p>
     *
     * @return the connection to the database
     */
    public abstract @NotNull Connection getConnection();

    /**
     * Returns the Ebean database.
     * This should be called after {@link #connect(DatabaseConfig, ClassLoader)}.
     * <p>
     * If the connection is not established, this method should throw an exception.
     * If the connection is established, this method should return the Ebean database.
     * </p>
     *
     * @return the Ebean database
     */
    public abstract Database getEbeanDatabase();

    /**
     * Reloads the connection to the database.
     *
     * @param config      the database config
     * @param classLoader the class loader
     */
    public abstract void reload(@NotNull DatabaseConfig config, @NotNull ClassLoader classLoader);
}
