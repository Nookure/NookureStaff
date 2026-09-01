package com.nookure.staff.paper.loader;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.ItemsConfig;
import com.nookure.staff.api.item.Items;
import com.nookure.staff.api.manager.StaffItemsManager;
import com.nookure.staff.api.util.AbstractLoader;
import com.nookure.staff.paper.factory.CustomCommandItemFactory;
import com.nookure.staff.paper.item.*;

public class ItemsLoader implements AbstractLoader {
    @Inject
    private StaffItemsManager manager;

    @Inject
    private ConfigurationContainer<ItemsConfig> itemConfig;

    @Inject
    private Injector injector;

    @Inject
    private Logger logger;

    @Inject
    private CustomCommandItemFactory customCommandItemFactory;

    @Override
    public void load() {
        manager.clearItems();

        itemConfig.get().staffItems.getItems().forEach((name, item) -> {
            logger.debug("Loading %s item class", name);
            if (!item.isEnabled()) return;

            switch (Items.valueOf(name.toUpperCase())) {
                case VANISH -> manager.addItem(name, injector.getInstance(VanishItem.class));
                case ENDER_CHEST -> manager.addItem(name, injector.getInstance(EnderchestItem.class));
                case FREEZE -> manager.addItem(name, injector.getInstance(FreezeItem.class));
                case INVSEE -> manager.addItem(name, injector.getInstance(InventorySeeItem.class));
                case NIGHT_VISION -> manager.addItem(name, injector.getInstance(NightVisionItem.class));
                case RANDOM_PLAYER_TELEPORT -> manager.addItem(name, injector.getInstance(RandomTeleportItem.class));
                case THRU -> manager.addItem(name, injector.getInstance(ThruItem.class));
                default -> logger.severe("Could not find %s item class", name);
            }
        });

        itemConfig.get().staffItems.getCustomItems().forEach((name, item) -> {
            if (!item.isEnabled()) return;
            if (item.getCommand() == null) {
                logger.severe("Could not find %s item command", name);
                return;
            }

            if (item.getType() == null) {
                logger.severe("Could not find %s item type", name);
                return;
            }

            manager.addItem(name, customCommandItemFactory.create(item));
        });
    }

    @Override
    public void reload() {
        load();
    }
}
