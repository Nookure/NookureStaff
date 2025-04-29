package com.nookure.staff.paper.listener.staff.state;

import com.google.inject.Inject;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OnWorldChange implements Listener {
  @Inject
  private PlayerWrapperManager<Player> playerWrapperManager;
  @Inject
  private JavaPlugin plugin;

  @EventHandler
  public void onWorldChange(PlayerChangedWorldEvent event) {
    Player player = event.getPlayer();

    playerWrapperManager.getStaffPlayer(player.getUniqueId()).ifPresent(playerWrapper -> {
      if (!playerWrapper.isStaffModeOrVanish()) {
        return;
      }

      Bukkit.getScheduler().runTaskLater(plugin, () -> {
        player.setAllowFlight(true);
        player.setFlying(true);
      }, 20L);
    });
  }
}
