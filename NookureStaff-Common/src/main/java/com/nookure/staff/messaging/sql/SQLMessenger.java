package com.nookure.staff.messaging.sql;

import com.google.inject.Inject;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.messaging.MessengerConfig;
import com.nookure.staff.api.database.util.MigrationUtil;
import com.nookure.staff.api.event.EventManager;
import com.nookure.staff.api.messaging.EventMessenger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import javax.sql.DataSource;
import org.jetbrains.annotations.NotNull;

public final class SQLMessenger extends EventMessenger {
    private final Logger logger;
    private final AtomicReference<DataSource> dataSource;
    private final ConfigurationContainer<MessengerConfig> config;
    private final EventManager eventManager;

    private static final String INSERT_QUERY = "INSERT INTO nookure_staff_messenger (data, server_uuid) VALUES (?, ?)";

    @Inject
    public SQLMessenger(
            @NotNull final AtomicReference<DataSource> dataSource,
            @NotNull final Logger logger,
            @NotNull final ConfigurationContainer<MessengerConfig> config,
            @NotNull final EventManager eventManager,
            @NotNull final NookureStaff plugin) {
        super(logger, plugin);
        this.dataSource = dataSource;
        this.logger = logger;
        this.config = config;
        this.eventManager = eventManager;
    }

    @Override
    public void prepare() {
        final var query = MigrationUtil.fromClasspath("migrations/mysql/21_55_16_12_2025_sql_messenger_system.sql");

        try (var connection = this.dataSource.get().getConnection()) {
            final var statement = connection.prepareStatement(query);
            statement.execute();
            connection.commit();
        } catch (final Exception e) {
            logger.severe("Failed to prepare SQLMessenger");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void publish(@NotNull PlayerWrapper sender, byte @NotNull [] data) {
        CompletableFuture.runAsync(() -> this.insert(data))
                .thenRun(() -> logger.debug("Published event to SQLMessenger"));

        decodeEvent(data).ifPresent(eventManager::fireEvent);
    }

    public void insert(byte @NotNull [] data) {
        try (var connection = this.dataSource.get().getConnection()) {
            final var statement = connection.prepareStatement(INSERT_QUERY);
            final var blob = connection.createBlob();
            blob.setBytes(1, data);
            statement.setBlob(1, blob);

            statement.setString(2, config.get().serverId.toString());

            statement.execute();
            connection.commit();
        } catch (final Exception e) {
            logger.severe("Failed to publish event to SQLMessenger");
            throw new RuntimeException(e);
        }
    }
}
