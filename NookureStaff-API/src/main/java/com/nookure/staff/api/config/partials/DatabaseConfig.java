package com.nookure.staff.api.config.partials;

import com.nookure.staff.api.database.DataProvider;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class DatabaseConfig {
    @Setting
    @Comment("""
      The type of the database.
      This can be either MYSQL or SQLITE.
      For MariaDB, use MYSQL driver.
      ¿When use MYSQL or SQLITE?
      - If you have more than 1 server, use MYSQL.
      - If you have only 1 server, but you want
      to be able to scale, use MYSQL.
      """)
    private DataProvider type = DataProvider.SQLITE;

    @Setting
    @Comment("""
      The host of the database.
      This can be an IP address or a domain name.
      ┏━━━━━━ Pterodactyl Users ━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
      ┃If you are using pterodactyl, I recommend using the ┃
      ┃docker bridge IP address, smth like 172.x.x.x       ┃
      ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
      """)
    private String host = "localhost";

    @Setting
    @Comment("""
      The port of the database.
      This is usually 3306 for MySQL
      """)
    private int port = 3306;

    @Setting
    @Comment("""
      The username of the database.
      """)
    private String username = "nookure";

    @Setting
    @Comment("""
      The password of the database.
      """)
    private String password = "yourSecurePassword";

    @Setting
    @Comment("""
      The name of the database.
      """)
    private String database = "database";

    public DataProvider getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }

    @Override
    public String toString() {
        return "DatabaseConfig{" + "type="
                + type + ", host='"
                + host + '\'' + ", port="
                + port + ", username='"
                + username + '\'' + ", password='"
                + password + '\'' + ", database='"
                + database + '\'' + '}';
    }
}
