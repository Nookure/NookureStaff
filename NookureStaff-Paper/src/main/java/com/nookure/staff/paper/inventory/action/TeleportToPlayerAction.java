package com.nookure.staff.paper.inventory.action;

import com.google.inject.Inject;
import com.nookure.core.inv.paper.CustomPaperAction;
import com.nookure.core.inv.paper.annotation.CustomActionData;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CustomActionData(value = "TELEPORT_TO_PLAYER", hasValue = true)
public class TeleportToPlayerAction extends CustomPaperAction {
    private final PlayerWrapperManager<Player> playerWrapperManager;
    private final ConfigurationContainer<BukkitMessages> messages;
    private final Logger logger;

    @Inject
    public TeleportToPlayerAction(
            @NotNull final PlayerWrapperManager<Player> playerWrapperManager,
            @NotNull final ConfigurationContainer<BukkitMessages> messages,
            @NotNull final Logger logger) {
        this.playerWrapperManager = playerWrapperManager;
        this.messages = messages;
        this.logger = logger;
    }

    @Override
    public void execute(Player player, @Nullable String value) {
        if (value == null) {
            logger.warning("TeleportToPlayerAction called with null value");
            return;
        }

        PlayerWrapper staffPlayer =
                playerWrapperManager.getPlayerWrapper(player.getUniqueId()).orElse(null);
        if (staffPlayer == null) {
            logger.warning("Staff player wrapper not found for " + player.getName());
            return;
        }

        PlayerWrapper targetPlayer = null;

        try {
            UUID targetUUID = UUID.fromString(value);
            targetPlayer = playerWrapperManager.getPlayerWrapper(targetUUID).orElse(null);
        } catch (IllegalArgumentException e) {
            targetPlayer = playerWrapperManager.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(value))
                    .findFirst()
                    .orElse(null);
        }

        if (targetPlayer == null) {
            staffPlayer.sendMiniMessage(messages.get().playerNotFound(), "player", value);
            return;
        }

        staffPlayer.teleport(targetPlayer);
        staffPlayer.sendMiniMessage(messages.get().staffMode.teleportingTo(), "player", targetPlayer.getName());

        logger.debug("[Inventory] Teleported %s to %s", staffPlayer.getName(), targetPlayer.getName());
    }
}
