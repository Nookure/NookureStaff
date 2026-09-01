package com.nookure.staff.paper.listener.staff.vanish;

import com.google.inject.Inject;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StaffVanishListener implements Listener {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Inject
    private ConfigurationContainer<BukkitMessages> messages;

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Optional<StaffPlayerWrapper> playerWrapper =
                playerWrapperManager.getStaffPlayer(event.getPlayer().getUniqueId());

        if (playerWrapper.isEmpty()) return;

        if (playerWrapper.get().isInVanish()) {
            event.joinMessage(null);
            playerWrapper.get().sendMiniMessage(messages.get().vanish.youAreStillInVanish());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Optional<StaffPlayerWrapper> playerWrapper =
                playerWrapperManager.getStaffPlayer(event.getPlayer().getUniqueId());

        if (playerWrapper.isEmpty()) return;

        if (playerWrapper.get().isInVanish()) {
            event.quitMessage(null);
        }
    }
}
