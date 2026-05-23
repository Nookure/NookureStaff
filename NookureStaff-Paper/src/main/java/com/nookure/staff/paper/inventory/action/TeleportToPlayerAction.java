package com.nookure.staff.paper.inventory.action;

import com.google.inject.Inject;
import com.nookure.core.inv.paper.CustomPaperAction;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.core.inv.paper.annotation.CustomActionData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@CustomActionData(value = "TELEPORT_TO_PLAYER", hasValue = true)
public class TeleportToPlayerAction extends CustomPaperAction {
  @Inject
  private PlayerWrapperManager<Player> playerWrapperManager;
  @Inject
  private ConfigurationContainer<BukkitMessages> messages;
  @Inject
  private Logger logger;

  @Override
  public void execute(Player player, @Nullable String value) {
    if (value == null) {
      logger.warning("TeleportToPlayerAction called with null value");
      return;
    }

    PlayerWrapper staffPlayer = playerWrapperManager.getPlayerWrapper(player.getUniqueId()).orElse(null);
    if (staffPlayer == null) {
      logger.warning("Staff player wrapper not found for " + player.getName());
      return;
    }

    // The value can be either a player name or UUID
    PlayerWrapper targetPlayer = null;
    
    // Try to parse as UUID first
    try {
      UUID targetUUID = UUID.fromString(value);
      targetPlayer = playerWrapperManager.getPlayerWrapper(targetUUID).orElse(null);
    } catch (IllegalArgumentException e) {
      // Not a UUID, try to find by name
      targetPlayer = playerWrapperManager.stream()
          .filter(p -> p.getName().equalsIgnoreCase(value))
          .findFirst()
          .orElse(null);
    }

    if (targetPlayer == null) {
      staffPlayer.sendMiniMessage(messages.get().playerNotFound(), "player", value);
      return;
    }

    // Use native teleportation API
    staffPlayer.teleport(targetPlayer);
    staffPlayer.sendMiniMessage(messages.get().staffMode.teleportingTo(), "player", targetPlayer.getName());
    
    logger.debug("Teleported %s to %s using native API", staffPlayer.getName(), targetPlayer.getName());
  }
} 