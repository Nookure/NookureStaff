package com.nookure.staff.api.config.messaging;

import java.util.UUID;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class MessengerConfig {
    @Setting
    @Comment("""
          The configuration for the Redis messenger.
          """)
    public RedisPartial redis = new RedisPartial();

    @Setting
    @Comment("This UUID represents the server in SQL databases, ensure it's unique.")
    public UUID serverId = UUID.randomUUID();

    @Setting
    @Comment("""
          The type of messenger to use.
          Here are the options:
          - REDIS: Use Redis to send messages between servers, the most
            reliable way to send messages between servers, but it requires
            a Redis server to be installed and configured, it's the fastest.

          - PM: Use plugin messages to send messages between servers this
            will only work if all the servers are under the same proxy and
            in the proxy is installed the bridge plugin, it's not the most
            reliable way to send messages between servers.

          - MYSQL: Use MySQL to send messages between servers, a bit more
            reliable than PM but much more slower than Redis.

          - NONE: Disable the sync, useful when you don't have a network
          and you only have 1 server.
          """)
    private MessengerType type = MessengerType.NONE;

    public MessengerType getType() {
        return type;
    }

    public enum MessengerType {
        REDIS,
        PM,
        MYSQL,
        NONE
    }
}
