package com.nookure.staff.paper.loader;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nookure.staff.api.util.AbstractLoader;
import com.nookure.staff.paper.placeholder.PlaceholderApiExtension;
import org.bukkit.Bukkit;

public class PlaceholderApiLoader implements AbstractLoader {
    @Inject
    private Injector injector;

    @Override
    public void load() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            return;
        }

        injector.getInstance(PlaceholderApiExtension.class).register();
    }
}
