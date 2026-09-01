package com.nookure.staff.paper.util.transoformer;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.manager.FreezeManager;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.api.state.PlayerState;
import com.nookure.staff.api.util.transformer.PlayerTransformer;
import com.nookure.staff.paper.PaperPlayerWrapper;
import com.nookure.staff.paper.StaffPaperPlayerWrapper;
import com.nookure.staff.paper.extension.FreezePlayerExtension;
import com.nookure.staff.paper.factory.PaperPlayerWrapperFactory;
import com.nookure.staff.paper.factory.StaffPaperPlayerWrapperFactory;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class PaperPlayerTransformer implements PlayerTransformer {
    private final PlayerWrapperManager<Player> playerWrapperManager;
    private final PaperPlayerWrapperFactory paperPlayerWrapperFactory;
    private final StaffPaperPlayerWrapperFactory staffPaperPlayerWrapperFactory;
    private final Logger logger;
    private final ConfigurationContainer<BukkitMessages> messages;
    private final FreezeManager freezeManager;
    private final List<Class<? extends PlayerState>> states;
    private JavaPlugin plugin;

    @Inject
    public PaperPlayerTransformer(
            @NotNull final PlayerWrapperManager<Player> playerWrapperManager,
            @NotNull final PaperPlayerWrapperFactory paperPlayerWrapperFactory,
            @NotNull final StaffPaperPlayerWrapperFactory staffPaperPlayerWrapperFactory,
            @NotNull final Logger logger,
            @NotNull final ConfigurationContainer<BukkitMessages> messages,
            @NotNull final FreezeManager freezeManager,
            @NotNull final List<Class<? extends PlayerState>> states,
            @NotNull final JavaPlugin plugin) {
        this.playerWrapperManager = playerWrapperManager;
        this.paperPlayerWrapperFactory = paperPlayerWrapperFactory;
        this.staffPaperPlayerWrapperFactory = staffPaperPlayerWrapperFactory;
        this.freezeManager = freezeManager;
        this.logger = logger;
        this.messages = messages;
        this.states = states;
        this.plugin = plugin;
    }

    @Override
    public void player2Staff(@NotNull final UUID uuid) {
        final PlayerWrapper player = playerWrapperManager.getPlayerWrapperOrNull(uuid);
        if (player == null) return;

        if (player instanceof StaffPlayerWrapper) {
            logger.debug("Player %s is already a staff member.", player.getName());
            return;
        }

        if (!(player instanceof PaperPlayerWrapper paperPlayerWrapper)) return;

        playerWrapperManager.removePlayerWrapper(paperPlayerWrapper.getPlayer());

        Bukkit.getScheduler().runTask(plugin, () -> {
            final StaffPaperPlayerWrapper staffPlayerWrapper =
                    staffPaperPlayerWrapperFactory.create(paperPlayerWrapper.getPlayer(), states);

            playerWrapperManager.addPlayerWrapper(staffPlayerWrapper.getPlayer(), staffPlayerWrapper);
            staffPlayerWrapper.sendMiniMessage(messages.get().youAreNowAnStaffDuringPermissionInterceptor());
        });
    }

    @Override
    public void staff2player(@NotNull final UUID uuid) {
        final PlayerWrapper player = playerWrapperManager.getPlayerWrapperOrNull(uuid);
        if (player == null) return;

        if (player instanceof PaperPlayerWrapper) {
            logger.debug("Player %s is already a player.", player.getName());
        }

        if (!(player instanceof StaffPaperPlayerWrapper staffPlayerWrapper)) return;

        if (staffPlayerWrapper.isInStaffMode()) {
            staffPlayerWrapper.disableVanish(false);
            staffPlayerWrapper.disableStaffMode();
            staffPlayerWrapper
                    .getExtension(FreezePlayerExtension.class)
                    .flatMap(extension -> freezeManager.stream()
                            .filter(freezeContainer -> freezeContainer
                                    .target()
                                    .equals(staffPlayerWrapper.getPlayer().getUniqueId()))
                            .findFirst())
                    .ifPresent(freezeContainer -> {
                        freezeManager.removeFreezeContainer(freezeContainer.target());
                    });
        }

        playerWrapperManager.removePlayerWrapper(staffPlayerWrapper.getPlayer());
        final PaperPlayerWrapper paperPlayerWrapper =
                paperPlayerWrapperFactory.create(staffPlayerWrapper.getPlayer(), List.of());

        playerWrapperManager.addPlayerWrapper(paperPlayerWrapper.getPlayer(), paperPlayerWrapper);
    }
}
