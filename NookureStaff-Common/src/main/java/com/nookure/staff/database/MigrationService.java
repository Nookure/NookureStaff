package com.nookure.staff.database;

import io.ebean.annotation.Platform;
import io.ebean.dbmigration.DbMigration;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MigrationService {
    public static void main(String[] args) throws IOException {
        DbMigration dbMigration = DbMigration.create();

        System.setProperty(
                "ddl.migration.version", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));

        dbMigration.addPlatform(Platform.SQLITE);
        dbMigration.addPlatform(Platform.MYSQL);

        dbMigration.generateMigration();
    }
}
