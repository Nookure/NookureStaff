package com.nookure.staff.database.repository;

import com.google.inject.Inject;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitConfig;
import com.nookure.staff.api.database.model.StaffStateModel;
import com.nookure.staff.api.database.repository.StaffStateRepository;
import com.nookure.staff.api.database.util.MigrationUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import javax.sql.DataSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SQLStaffStateRepository implements StaffStateRepository {
    private final AtomicReference<DataSource> dataSource;
    private final ConfigurationContainer<BukkitConfig> config;
    private final Logger logger;

    private final Map<String, List<String>> migrations = Map.of(
            "MYSQL",
                    List.of(MigrationUtil.fromClasspath(
                            "migrations/mysql/22_22_25_11_2024_create_nookure_staff_data.sql")),
            "SQLITE",
                    List.of(MigrationUtil.fromClasspath(
                            "migrations/sqlite/22_22_25_11_2024_create_nookure_staff_data.sql")));

    @Inject
    public SQLStaffStateRepository(
            @NotNull final AtomicReference<DataSource> dataSource,
            @NotNull final ConfigurationContainer<BukkitConfig> config,
            @NotNull final Logger logger) {
        this.dataSource = dataSource;
        this.config = config;
        this.logger = logger;
    }

    @Override
    public void migrate() {
        switch (config.get().database.getType()) {
            case MYSQL -> migrations.get("MYSQL").forEach(this::runMigration);
            case SQLITE -> migrations.get("SQLITE").forEach(this::runMigration);
        }
    }

    private void runMigration(@NotNull final String migration) {
        try (Connection connection = dataSource.get().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(migration);
            ps.execute();
            connection.commit();
        } catch (SQLException e) {
            logger.severe("Failed to run migration: " + migration);
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nullable StaffStateModel fromUUID(@NotNull UUID uuid) {
        try (Connection connection = dataSource.get().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM nookure_staff_data WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            return StaffStateModel.builder()
                    .uuid(UUID.fromString(rs.getString("UUID")))
                    .staffMode(rs.getBoolean("staff_mode"))
                    .vanished(rs.getBoolean("vanished"))
                    .staffChatEnabled(rs.getBoolean("staff_chat_enabled"))
                    .build();
        } catch (SQLException e) {
            logger.severe("Failed to retrieve staff state model for UUID: " + uuid);
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nullable StaffStateModel fromID(int id) {
        try (Connection connection = dataSource.get().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM nookure_staff_data WHERE ID = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            return StaffStateModel.builder()
                    .uuid(UUID.fromString(rs.getString("UUID")))
                    .staffMode(rs.getBoolean("staff_mode"))
                    .vanished(rs.getBoolean("vanished"))
                    .staffChatEnabled(rs.getBoolean("staff_chat_enabled"))
                    .build();
        } catch (SQLException e) {
            logger.severe("Failed to retrieve staff state model for ID: " + id);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void savePlayerModel(@NotNull StaffStateModel model) {
        if (fromUUID(model.uuid()) != null) {
            logger.severe("Staff state model already exists for UUID: %s", model.uuid());
            throw new IllegalStateException("Staff state model already exists for UUID: " + model.uuid());
        }

        try (Connection connection = dataSource.get().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO nookure_staff_data (UUID, staff_mode, vanished, staff_chat_enabled) VALUES (?, ?, ?, ?)");
            ps.setString(1, model.uuid().toString());
            ps.setBoolean(2, model.staffMode());
            ps.setBoolean(3, model.vanished());
            ps.setBoolean(4, model.staffChatEnabled());
            ps.execute();
            connection.commit();
        } catch (SQLException e) {
            logger.severe("Failed to save staff state model %s", model);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePlayerModel(@NotNull StaffStateModel model) {
        try (Connection connection = dataSource.get().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM nookure_staff_data WHERE UUID = ?");
            ps.setString(1, model.uuid().toString());
            ps.execute();
            connection.commit();
        } catch (SQLException e) {
            logger.severe("Failed to delete staff state model %s", model);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePlayerModel(@NotNull StaffStateModel model) {
        try (Connection connection = dataSource.get().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE nookure_staff_data SET staff_mode = ?, vanished = ?, staff_chat_enabled = ? WHERE UUID = ?");
            ps.setBoolean(1, model.staffMode());
            ps.setBoolean(2, model.vanished());
            ps.setBoolean(3, model.staffChatEnabled());
            ps.setString(4, model.uuid().toString());
            ps.execute();
            logger.debug("Updated staff state model %s", model);
            connection.commit();
        } catch (SQLException e) {
            logger.severe("Failed to update staff state model %s", model);
            throw new RuntimeException(e);
        }
    }
}
