package com.nookure.staff.paper.listener.staff.state;

import com.google.inject.Inject;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class OnItemDrop implements Listener {
  @Inject
  private PlayerWrapperManager<Player> playerWrapperManager;

  @EventHandler
  public void onItemDrop(PlayerDropItemEvent event) {
    playerWrapperManager.getStaffPlayer(event.getPlayer().getUniqueId()).ifPresent(playerWrapper -> {
      if (playerWrapper.isStaffModeOrVanish()) event.setCancelled(true);
    });
  }
}
