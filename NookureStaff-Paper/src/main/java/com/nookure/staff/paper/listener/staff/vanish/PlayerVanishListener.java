package com.nookure.staff.paper.listener.staff.vanish;

import com.google.inject.Inject;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.paper.StaffPaperPlayerWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerVanishListener implements Listener {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Inject
    private JavaPlugin javaPlugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission(Permissions.STAFF_VANISH_SEE)) return;

        playerWrapperManager.stream()
                .filter(player -> {
                    if (player instanceof StaffPlayerWrapper staffPlayer) {
                        return staffPlayer.isInVanish();
                    }

                    return false;
                })
                .forEach(player -> {
                    StaffPaperPlayerWrapper staffPlayer = (StaffPaperPlayerWrapper) player;
                    event.getPlayer().hidePlayer(javaPlugin, staffPlayer.getPlayer());
                });
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        playerWrapperManager.stream().forEach(player -> {
            if (!(player instanceof StaffPaperPlayerWrapper staffPlayer)) return;

            event.getPlayer().showPlayer(javaPlugin, staffPlayer.getPlayer());
        });
    }
}
