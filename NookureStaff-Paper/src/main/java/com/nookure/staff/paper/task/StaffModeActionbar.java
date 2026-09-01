package com.nookure.staff.paper.task;

import com.google.inject.Inject;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.paper.StaffPaperPlayerWrapper;
import org.bukkit.entity.Player;

public class StaffModeActionbar implements Runnable {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Override
    public void run() {
        playerWrapperManager.stream()
                .filter(player -> player instanceof StaffPlayerWrapper)
                .forEach(player -> {
                    StaffPaperPlayerWrapper staffPlayer = (StaffPaperPlayerWrapper) player;
                    staffPlayer.addActionBar();
                });
    }
}
