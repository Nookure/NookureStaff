package com.nookure.staff.paper.task;

import com.google.inject.Inject;
import com.nookure.core.inv.paper.PaperNookureInventoryEngine;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitConfig;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.api.model.PinModel;
import com.nookure.staff.api.state.PinState;
import com.nookure.staff.api.util.NumberUtils;
import com.nookure.staff.api.util.TextUtils;
import com.nookure.staff.paper.PaperPlayerWrapper;
import com.nookure.staff.paper.inventory.InventoryList;
import io.ebean.Database;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PinTask implements Runnable {
    @Inject
    private PlayerWrapperManager<Player> manager;

    @Inject
    private AtomicReference<PaperNookureInventoryEngine> engine;

    @Inject
    private ConfigurationContainer<BukkitConfig> config;

    @Inject
    private ConfigurationContainer<BukkitMessages> messages;

    @Inject
    private JavaPlugin plugin;

    @Inject
    private AtomicReference<Database> database;

    @Override
    public void run() {
        manager.stream().filter(player -> player instanceof StaffPlayerWrapper).forEach(p -> {
            PinState pinState = p.getState().getState(PinState.class);

            if (pinState == null) {
                return;
            }

            check(p, pinState);
        });
    }

    private void check(PlayerWrapper wrapper, @NotNull PinState pinState) {
        Player player = ((PaperPlayerWrapper) wrapper).getPlayer();
        if (pinState.isLogin()) return;
        if (pinState.isPinInventoryOpen()) return;

        PinModel model = database.get()
                .find(PinModel.class)
                .where()
                .eq("player", wrapper.getPlayerModel())
                .findOne();

        if (model != null) {
            if (model.ip().equals(wrapper.getPlayerModel().getLastIp())) {
                pinState.setLogin(true);
                return;
            }
        }

        if (pinState.isPinSet()) {
            engine.get()
                    .openAsync(
                            player,
                            InventoryList.ENTER_PIN,
                            "player",
                            player,
                            "pinSize",
                            pinState.getPin().length());
            pinState.setPinInventoryOpen(true);
            return;
        }

        if (pinState.getTimeToCreateAPin() == -1) {
            pinState.setTimeToCreateAPin(System.currentTimeMillis()
                    + NumberUtils.parseToMillis(config.get().staffMode.pinTime()));
        }

        if (System.currentTimeMillis() >= pinState.getTimeToCreateAPin()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> wrapper.kick(messages.get().pin.pinTimeExpired), 1L);
            return;
        }

        wrapper.sendMiniMessage(
                messages.get().pin.youMustHaveToSetAPin,
                "time",
                TextUtils.formatTime(pinState.getTimeToCreateAPin() - System.currentTimeMillis()));
    }
}
