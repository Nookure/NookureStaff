package com.nookure.staff.paper.listener.staff.state;

import com.google.inject.Inject;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.entity.TrialSpawnerSpawnEvent;
import org.jetbrains.annotations.NotNull;

public class OnSpawnerSpawn implements Listener {
  private final PlayerWrapperManager<Player> playerWrapperManager;

  @Inject
  public OnSpawnerSpawn(@NotNull final PlayerWrapperManager<Player> playerWrapperManager) {
    this.playerWrapperManager = playerWrapperManager;
  }

  @EventHandler
  public void onSpawnerSpawn(@NotNull final SpawnerSpawnEvent event) {
    final var spawner = event.getSpawner();
    if (spawner == null) return;

    final var location = spawner.getLocation();
    final var range = spawner.getRequiredPlayerRange();

    boolean spawn = false;

    if (location.getWorld() == null) return;

    for (final var player : location.getWorld().getNearbyPlayers(location, range, range, range)) {
      if (canTriggerSpawner(player)) {
        spawn = true;
        break;
      }
    }

    if (!spawn) event.setCancelled(true);
  }

  @SuppressWarnings("UnstableApiUsage")
  // @EventHandler // This keeps disabled for now, because it seems to be buggy
  // This should be working, but it seems not working on some cases
  // TODO: Some kind of triage
  public void onTrialSpawnerSpawn(@NotNull final TrialSpawnerSpawnEvent event) {
    final var spawner = event.getTrialSpawner();
    final var location = spawner.getLocation();
    final int range = spawner.getRequiredPlayerRange();

    if (location.getWorld() == null) return;

    for (final var entity : location.getWorld().getNearbyEntities(location, range, range, range)) {
      if (!(entity instanceof Player player)) continue;

      if (!canTriggerSpawner(player)) {
        spawner.stopTrackingPlayer(player);
      } else {
        spawner.startTrackingPlayer(player);
      }
    }
  }

  boolean canTriggerSpawner(@NotNull final Player player) {
    if (!playerWrapperManager.isStaffPlayer(player.getUniqueId())) {
      return true;
    }

    final var staffPlayer = playerWrapperManager.getStaffPlayerOrNull(player.getUniqueId());
    if (staffPlayer == null) return true;

    return !staffPlayer.isStaffModeOrVanish();
  }
}
