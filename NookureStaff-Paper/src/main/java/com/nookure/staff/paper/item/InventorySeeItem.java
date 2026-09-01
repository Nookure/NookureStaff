package com.nookure.staff.paper.item;

import com.google.inject.Inject;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.ItemsConfig;
import com.nookure.staff.api.item.Items;
import com.nookure.staff.api.item.PlayerInteractItem;
import com.nookure.staff.api.item.StaffItem;
import com.nookure.staff.paper.PaperPlayerWrapper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InventorySeeItem extends StaffItem implements PlayerInteractItem {
    @Inject
    public InventorySeeItem(ConfigurationContainer<ItemsConfig> itemsConfig) {
        super(itemsConfig.get().staffItems.getItems().get(Items.INVSEE.toString()));
    }

    @Override
    public void click(@NotNull PlayerWrapper player, @NotNull PlayerWrapper target) {
        Player bukkitPlayer = ((PaperPlayerWrapper) player).getPlayer();
        Player bukkitTarget = ((PaperPlayerWrapper) target).getPlayer();

        bukkitPlayer.openInventory(bukkitTarget.getInventory());
    }
}
