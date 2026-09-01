package com.nookure.staff.paper.listener.freeze;

import com.google.inject.Inject;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitConfig;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.manager.FreezeManager;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.paper.extension.FreezePlayerExtension;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnFreezePlayerQuit implements Listener {
    @Inject
    private FreezeManager freezeManager;

    @Inject
    private ConfigurationContainer<BukkitConfig> config;

    @Inject
    private ConfigurationContainer<BukkitMessages> messages;

    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!freezeManager.isFrozen(event.getPlayer())) return;

        if (!config.get().freeze.executeCommandOnExit()) {
            freezeManager.removeFreezeContainer(event.getPlayer().getUniqueId());
            return;
        }

        Optional<FreezeManager.FreezeContainer> optionalContainer =
                freezeManager.getFreezeContainer(event.getPlayer().getUniqueId());

        if (optionalContainer.isEmpty()) return;

        FreezeManager.FreezeContainer container = optionalContainer.get();

        Optional<StaffPlayerWrapper> optionalStaff = playerWrapperManager.getStaffPlayer(container.staff());

        if (optionalStaff.isEmpty()) return;

        StaffPlayerWrapper staff = optionalStaff.get();

        Optional<FreezePlayerExtension> optionalExtension = staff.getExtension(FreezePlayerExtension.class);

        if (optionalExtension.isEmpty()) return;

        FreezePlayerExtension extension = optionalExtension.get();

        if (!config.get().freeze.askToExecuteCommandOnExit()) {
            freezeManager.removeFreezeContainer(event.getPlayer().getUniqueId());
            extension.executeFreezeCommands(staff, event.getPlayer().getName());
            return;
        }

        staff.sendMiniMessage(messages.get()
                .freeze
                .confirmPunishMessage()
                .replace("{player}", event.getPlayer().getName())
                .replace("{uuid}", event.getPlayer().getUniqueId().toString()));
    }
}
