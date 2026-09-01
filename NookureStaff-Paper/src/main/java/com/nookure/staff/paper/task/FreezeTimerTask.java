package com.nookure.staff.paper.task;

import com.google.inject.Inject;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.manager.FreezeManager;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.api.util.Scheduler;
import com.nookure.staff.api.util.TextUtils;
import com.nookure.staff.paper.extension.FreezePlayerExtension;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class FreezeTimerTask implements Runnable {
    @Inject
    private FreezeManager freezeManager;

    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Inject
    private Scheduler scheduler;

    @Override
    public void run() {
        freezeManager.stream().forEach(container -> {
            if (container.timeLeft() == -1) return;

            Optional<PlayerWrapper> optionalPlayer = playerWrapperManager.getPlayerWrapper(container.target());
            Optional<StaffPlayerWrapper> optionalStaff = playerWrapperManager.getStaffPlayer(container.staff());
            if (optionalPlayer.isEmpty() || optionalStaff.isEmpty()) return;

            PlayerWrapper player = optionalPlayer.get();
            StaffPlayerWrapper staff = optionalStaff.get();

            if (!(container.timeLeft() < System.currentTimeMillis())) {
                Component cmp = TextUtils.toComponent(
                        "<red>" + TextUtils.formatTime(container.timeLeft() - System.currentTimeMillis()));

                player.sendActionbar(cmp);
                return;
            }

            Optional<FreezePlayerExtension> optionalExtension = staff.getExtension(FreezePlayerExtension.class);

            if (optionalExtension.isEmpty()) return;

            FreezePlayerExtension extension = optionalExtension.get();

            extension.unfreezePlayer(player);

            scheduler.sync(() -> extension.executeFreezeCommands(staff, player.getName()));
        });
    }
}
