package com.nookure.staff.api.util;

import com.google.inject.AbstractModule;
import com.nookure.staff.api.event.EventManager;

public abstract class PluginModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EventManager.class).asEagerSingleton();
    }
}
