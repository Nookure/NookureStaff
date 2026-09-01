package com.nookure.staff.paper.listener;

import com.google.inject.Inject;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Inject
    private Logger logger;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        logger.debug("Player " + event.getPlayer().getName() + " has left the server");
        logger.debug("Removing player wrapper for " + event.getPlayer().getName());

        playerWrapperManager.removePlayerWrapper(event.getPlayer());
    }
}
