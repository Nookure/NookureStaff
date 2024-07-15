package com.nookure.staff.paper.loader;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nookure.core.inv.paper.PaperNookureInventoryEngine;
import com.nookure.core.inv.template.extension.OpenInventoryExtension;
import com.nookure.core.inv.template.extension.PaginationItemExtension;
import com.nookure.staff.api.util.AbstractLoader;
import com.nookure.staff.api.util.JarUtil;
import com.nookure.staff.paper.inventory.extenion.DataFormatExtension;
import com.nookure.staff.paper.inventory.extenion.NookurePlayerExtension;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class InventoryLoader implements AbstractLoader {
  @Inject
  private JavaPlugin plugin;
  @Inject
  private AtomicReference<PaperNookureInventoryEngine> engine;
  @Inject
  private Injector injector;
  private static final boolean replaceFolder;

  static {
    String property = System.getProperty("nkstaff.inventory.replace");

    if (property == null) {
      replaceFolder = false;
    } else {
      replaceFolder = Boolean.parseBoolean(property);
    }
  }

  @Override
  public void load() {
    PaperNookureInventoryEngine.Builder builder = new PaperNookureInventoryEngine.Builder()
        .plugin(plugin)
        .templateFolder("inventories")
        .extensions(
            new PaginationItemExtension(),
            new OpenInventoryExtension(),
            injector.getInstance(NookurePlayerExtension.class),
            injector.getInstance(DataFormatExtension.class)
        );

    engine.set(builder.build());

    try {
      JarUtil.CopyOption option;

      option = replaceFolder ? JarUtil.CopyOption.REPLACE_IF_EXIST : JarUtil.CopyOption.COPY_IF_NOT_EXIST;

      JarUtil.copyFolderFromJar("inventories", plugin.getDataFolder(), option);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void reload() {
    engine.set(null);
    load();
  }
}
