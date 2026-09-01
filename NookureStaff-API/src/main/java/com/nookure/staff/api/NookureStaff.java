package com.nookure.staff.api;

import com.google.inject.Injector;
import com.nookure.staff.api.command.Command;
import java.io.File;
import java.io.InputStream;

public interface NookureStaff {
    Logger getPLogger();

    boolean isDebug();

    void setDebug(boolean debug);

    void reload();

    File getPluginDataFolder();

    InputStream getPluginResource(String s);

    Injector getInjector();

    String getPrefix();

    default void registerCommand(Command command) {}

    default void unregisterCommand(Command command) {}
}
