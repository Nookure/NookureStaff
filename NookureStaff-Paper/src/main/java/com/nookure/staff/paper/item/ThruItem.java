package com.nookure.staff.paper.item;

import com.google.inject.Inject;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.ItemsConfig;
import com.nookure.staff.api.item.ExecutableItem;
import com.nookure.staff.api.item.ExecutableLocationItem;
import com.nookure.staff.api.item.Items;
import com.nookure.staff.api.item.StaffItem;
import com.nookure.staff.paper.PaperPlayerWrapper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class ThruItem extends StaffItem implements ExecutableLocationItem, ExecutableItem {
    @Inject
    public ThruItem(ConfigurationContainer<ItemsConfig> itemsConfig) {
        super(itemsConfig.get().staffItems.getItems().get(Items.THRU.toString()));
    }

    @Override
    public void click(@NotNull PlayerWrapper player, @NotNull Location location) {
        if (!(player instanceof PaperPlayerWrapper playerWrapper)) return;
        Player bukkitPlayer = playerWrapper.getPlayer();

        passThrough(bukkitPlayer, location);
    }

    @Override
    public void click(@NotNull PlayerWrapper player) {
        if (!(player instanceof PaperPlayerWrapper playerWrapper)) return;
        Player bukkitPlayer = playerWrapper.getPlayer();

        Vector direction = bukkitPlayer.getLocation().getDirection().normalize();
        bukkitPlayer.setVelocity(direction.multiply(2));
        bukkitPlayer.setFallDistance(0);
    }

    public void passThrough(Player player, Location clickedLocation) {
        Vector direction = player.getLocation().getDirection().normalize();

        int distance = 0;
        do {
            clickedLocation.add(direction);
            distance++;
        } while (clickedLocation.getBlock().getType().equals(Material.AIR) && distance < 6);

        player.teleportAsync(clickedLocation);
    }
}
