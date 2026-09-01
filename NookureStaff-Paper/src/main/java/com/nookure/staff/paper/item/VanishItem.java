package com.nookure.staff.paper.item;

import com.google.inject.Inject;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.ItemsConfig;
import com.nookure.staff.api.item.ExecutableItem;
import com.nookure.staff.api.item.Items;
import com.nookure.staff.api.item.StaffItem;
import org.jetbrains.annotations.NotNull;

public class VanishItem extends StaffItem implements ExecutableItem {
    @Inject
    public VanishItem(ConfigurationContainer<ItemsConfig> itemsConfig) {
        super(itemsConfig.get().staffItems.getItems().get(Items.VANISH.toString()));
    }

    @Override
    public void click(@NotNull PlayerWrapper player) {
        ((StaffPlayerWrapper) player).toggleVanish();
    }
}
