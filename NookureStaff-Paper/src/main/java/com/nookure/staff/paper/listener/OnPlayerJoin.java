package com.nookure.staff.paper.listener;

import com.google.inject.Inject;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitConfig;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.api.state.PinState;
import com.nookure.staff.api.state.PlayerState;
import com.nookure.staff.paper.PaperPlayerWrapper;
import com.nookure.staff.paper.StaffPaperPlayerWrapper;
import com.nookure.staff.paper.factory.PaperPlayerWrapperFactory;
import com.nookure.staff.paper.factory.StaffPaperPlayerWrapperFactory;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class OnPlayerJoin implements Listener {
    private final PlayerWrapperManager<Player> playerWrapperManager;
    private final Logger logger;
    private final PaperPlayerWrapperFactory playerWrapperFactory;
    private final StaffPaperPlayerWrapperFactory staffPaperPlayerWrapperFactory;
    private final List<Class<? extends PlayerState>> states;

    @Inject
    public OnPlayerJoin(
            @NotNull final PlayerWrapperManager<Player> playerWrapperManager,
            @NotNull final Logger logger,
            @NotNull final List<Class<? extends PlayerState>> states,
            @NotNull final PaperPlayerWrapperFactory playerWrapperFactory,
            @NotNull final StaffPaperPlayerWrapperFactory staffPaperPlayerWrapperFactory,
            @NotNull final ConfigurationContainer<BukkitConfig> config) {
        this.playerWrapperManager = playerWrapperManager;
        this.logger = logger;
        this.playerWrapperFactory = playerWrapperFactory;
        this.staffPaperPlayerWrapperFactory = staffPaperPlayerWrapperFactory;
        this.states = states;

        if (config.get().modules.isPinCode()) {
            states.add(PinState.class);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {
        logger.debug("Player %s has joined the server", event.getPlayer().getName());
        logger.debug("Creating player wrapper for %s", event.getPlayer().getName());

        if (event.getPlayer().hasPermission(Permissions.STAFF_PERMISSION)) {
            StaffPaperPlayerWrapper playerWrapper = staffPaperPlayerWrapperFactory.create(event.getPlayer(), states);

            playerWrapperManager.addPlayerWrapper(event.getPlayer(), playerWrapper, true);

            logger.debug(
                    "Player %s has staff permission, adding staff player wrapper",
                    event.getPlayer().getName());
            return;
        }

        PaperPlayerWrapper playerWrapper = playerWrapperFactory.create(event.getPlayer(), List.of());

        playerWrapperManager.addPlayerWrapper(event.getPlayer(), playerWrapper);

        logger.debug(
                "Player %s does not have staff permission, adding player wrapper",
                event.getPlayer().getName());
    }
}
