package com.nookure.staff.paper.pin.listener;

import com.google.inject.Inject;
import com.nookure.core.inv.paper.InventoryContainer;
import com.nookure.core.inv.paper.PaperNookureInventoryEngine;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.api.state.PinState;
import com.nookure.staff.paper.inventory.InventoryList;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class OnInventoryClose implements Listener {
    @Inject
    private PlayerWrapperManager<Player> wrapperManager;

    @Inject
    private AtomicReference<PaperNookureInventoryEngine> engine;

    @Inject
    private Logger logger;

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof InventoryContainer)) {
            return;
        }

        PlayerWrapper wrapper =
                wrapperManager.getPlayerWrapper(event.getPlayer().getUniqueId()).orElse(null);

        if (wrapper == null) {
            return;
        }

        PinState pinState = wrapper.getState().getState(PinState.class);

        if (pinState == null) {
            return;
        }

        if (!pinState.isPinInventoryOpen()) return;
        if (event.getReason() == InventoryCloseEvent.Reason.PLUGIN
                || event.getReason() == InventoryCloseEvent.Reason.OPEN_NEW) return;
        logger.info("Pin inventory closed for " + event.getPlayer().getName());
        logger.info("Close reason: " + event.getReason());

        if (!pinState.isLogin()) {
            engine.get()
                    .openAsync(
                            (Player) event.getPlayer(),
                            InventoryList.ENTER_PIN,
                            "player",
                            event.getPlayer(),
                            "pinSize",
                            pinState.getPin().length());
        }
    }
}
