package com.nookure.staff.paper.listener.staff.state;

import com.google.inject.Inject;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import org.bukkit.Bukkit;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class OnOpenChest implements Listener {
  @Inject
  private PlayerWrapperManager<Player> playerWrapperManager;

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();

    if (playerWrapperManager.getStaffPlayer(player.getUniqueId()).isEmpty()) {
      return;
    }

    StaffPlayerWrapper playerWrapper = playerWrapperManager.getStaffPlayer(player.getUniqueId()).orElseThrow();

    if (!playerWrapper.isStaffModeOrVanish()) {
      return;
    }

    if (event.getClickedBlock() == null) {
      return;
    }

    if (event.getClickedBlock().getState() instanceof EnderChest) {
      event.setCancelled(true);
      player.openInventory(player.getEnderChest());
      return;
    }

    if (!(event.getClickedBlock().getState() instanceof Container container)) {
      return;
    }

    if (container instanceof Chest || container instanceof Barrel || container instanceof ShulkerBox) {
      Inventory bInv = Bukkit.createInventory(
          null,
          container.getInventory().getSize(),
          container.getInventory().getType().defaultTitle()
      );

      bInv.setContents(container.getInventory().getContents());

      player.openInventory(bInv);
      event.setCancelled(true);
    }
  }
}
