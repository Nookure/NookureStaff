package com.nookure.staff.messaging;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.event.EventManager;
import com.nookure.staff.api.messaging.EventMessenger;
import com.nookure.staff.api.util.Scheduler;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Singleton
public class RedisMessenger extends EventMessenger {
    private static final byte[] CHANNEL = "nkstaff_events".getBytes();
    private final JedisPool jedisPool;
    private final Injector injector;
    private final Logger logger;
    private final Scheduler scheduler;
    private BinaryJedisPubSub messenger;
    private int subscribeTaskID;

    @Inject
    public RedisMessenger(
            @NotNull Logger logger,
            @NotNull NookureStaff plugin,
            @NotNull JedisPool jedisPool,
            @NotNull Injector injector,
            @NotNull Scheduler scheduler) {
        super(logger, plugin);
        this.logger = logger;
        this.jedisPool = jedisPool;
        this.injector = injector;
        this.scheduler = scheduler;
    }

    @Override
    public void prepare() {
        logger.debug("Subscribing to redis channel " + Arrays.toString(CHANNEL));
        messenger = injector.getInstance(RedisEventMessenger.class);
        subscribeTaskID = scheduler.async(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(messenger, CHANNEL);
            } catch (Exception e) {
                logger.severe("Failed to subscribe to redis channel " + Arrays.toString(CHANNEL));
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void publish(@NotNull PlayerWrapper sender, byte @NotNull [] data) {
        try (Jedis jedis = jedisPool.getResource()) {
            logger.debug("Publishing event to redis");
            jedis.publish(CHANNEL, data);
        } catch (Exception e) {
            logger.severe("Failed to publish event to redis");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        messenger.unsubscribe();
        scheduler.cancel(subscribeTaskID);
        jedisPool.close();
    }

    private static class RedisEventMessenger extends BinaryJedisPubSub {
        @Inject
        private EventMessenger eventTransport;

        @Inject
        private EventManager eventManager;

        @Inject
        private Logger logger;

        @Override
        public void onSubscribe(byte[] channel, int subscribedChannels) {
            logger.debug("Subscribed to redis channel " + Arrays.toString(channel));
        }

        @Override
        public void onUnsubscribe(byte[] channel, int subscribedChannels) {
            logger.debug("Unsubscribed from redis channel " + Arrays.toString(channel));
        }

        @Override
        public void onMessage(byte[] channel, byte[] message) {
            try {
                logger.debug("Received message from redis");
                eventTransport.decodeEvent(message).ifPresent(event -> {
                    logger.debug("Received event " + event.getClass().getSimpleName() + " from redis");
                    eventManager.fireEvent(event);
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
