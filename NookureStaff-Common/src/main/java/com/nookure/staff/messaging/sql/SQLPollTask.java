package com.nookure.staff.messaging.sql;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.messaging.MessengerConfig;
import com.nookure.staff.api.event.EventManager;
import com.nookure.staff.api.messaging.EventMessenger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.sql.DataSource;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class SQLPollTask {
    private final AtomicReference<DataSource> dataSource;
    private final Logger logger;
    private final EventMessenger eventMessenger;
    private final EventManager eventManager;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final ConfigurationContainer<MessengerConfig> config;

    private static final String LAST_POLL_QUERY = "SELECT MAX(`id`) as `latest` FROM `nookure_staff_messenger`";
    private static final String POLL_QUERY =
            "SELECT * FROM `nookure_staff_messenger` WHERE `id` > ? AND (NOW() - `time` < 30)";
    private static final String CLEANUP_QUERY = "DELETE FROM `nookure_staff_messenger` WHERE `time` < (NOW() - 60)";

    private long lastPoll = -1;

    @Inject
    public SQLPollTask(
            @NotNull final AtomicReference<DataSource> dataSource,
            @NotNull final Logger logger,
            @NotNull final EventMessenger eventMessenger,
            @NotNull final EventManager eventManager,
            @NotNull final ConfigurationContainer<MessengerConfig> config) {
        this.dataSource = dataSource;
        this.logger = logger;
        this.eventMessenger = eventMessenger;
        this.eventManager = eventManager;
        this.config = config;

        if (!(eventMessenger instanceof SQLMessenger)) {
            throw new IllegalArgumentException("EventMessenger must be an instance of SQLMessenger");
        }

        try (var connection = dataSource.get().getConnection()) {
            ResultSet rs = connection.prepareStatement(LAST_POLL_QUERY).executeQuery();
            if (rs.next()) {
                lastPoll = rs.getLong("latest");
            }
        } catch (SQLException e) {
            logger.severe("Failed to poll SQLMessenger");
            throw new RuntimeException(e);
        }
    }

    public void pollMessages() {
        long start = System.currentTimeMillis();
        logger.debug("Polling SQLMessenger...");

        lock.writeLock().lock();
        try (var connection = dataSource.get().getConnection()) {
            var statement = connection.prepareStatement(POLL_QUERY);
            statement.setLong(1, lastPoll);
            var rs = statement.executeQuery();
            while (rs.next()) {
                final var uuid = rs.getString("server_uuid");

                if (uuid.equals(config.get().serverId.toString())) {
                    continue;
                }

                final var blob = rs.getBlob("data");
                final var data = blob.getBytes(1, (int) blob.length());
                eventMessenger.decodeEvent(data).ifPresent(event -> {
                    logger.debug("Received event " + event.getClass().getSimpleName() + " from redis");
                    eventManager.fireEvent(event);
                });
                lastPoll = rs.getLong("id");
            }
        } catch (SQLException e) {
            logger.severe("Failed to poll SQLMessenger");
            throw new RuntimeException(e);
        } finally {
            lock.writeLock().unlock();
        }

        logger.debug("Finished polling SQLMessenger in " + (System.currentTimeMillis() - start) + "ms");
    }

    public void cleanup() {
        logger.debug("Cleaning up SQLMessenger...");
        long start = System.currentTimeMillis();

        lock.writeLock().lock();
        try (var connection = dataSource.get().getConnection()) {
            connection.prepareStatement(CLEANUP_QUERY).execute();
            connection.commit();
        } catch (SQLException e) {
            logger.severe("Failed to cleanup SQLMessenger");
            throw new RuntimeException(e);
        } finally {
            lock.writeLock().unlock();
        }

        logger.debug("Finished cleaning up SQLMessenger in " + (System.currentTimeMillis() - start) + "ms");
    }
}
