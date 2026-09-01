package com.nookure.staff.paper.pin.action;

import com.google.inject.Inject;
import com.nookure.core.inv.paper.CustomPaperAction;
import com.nookure.core.inv.paper.PaperNookureInventoryEngine;
import com.nookure.core.inv.paper.annotation.CustomActionData;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.api.service.PinUserService;
import com.nookure.staff.api.state.PinState;
import com.nookure.staff.paper.inventory.InventoryList;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@CustomActionData(value = "pin_button", hasValue = true)
public class PinButtonPressed extends CustomPaperAction {
    @Inject
    private PinUserService service;

    @Inject
    private ConfigurationContainer<BukkitMessages> messages;

    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Inject
    private Logger logger;

    @Inject
    private AtomicReference<PaperNookureInventoryEngine> engine;

    @Override
    public void execute(Player player, @Nullable String value) {
        if (value == null) {
            return;
        }

        PlayerWrapper playerWrapper =
                playerWrapperManager.getPlayerWrapper(player.getUniqueId()).orElse(null);

        if (playerWrapper == null) {
            return;
        }

        PinState pinState = playerWrapper.getState().getState(PinState.class);

        if (pinState == null) {
            return;
        }

        char digit = value.charAt(0);

        if (digit == '-') {
            pinState.removePinDigit();
            engine.get()
                    .openAsync(
                            player,
                            InventoryList.ENTER_PIN,
                            "player",
                            player,
                            "pinSize",
                            pinState.getPin().length());
            return;
        }

        if (pinState.addPinDigit(digit)) {
            engine.get()
                    .openAsync(
                            player,
                            InventoryList.ENTER_PIN,
                            "player",
                            player,
                            "pinSize",
                            pinState.getPin().length());
            return;
        }

        String pin = pinState.getPin();
        pinState.setPinInventoryOpen(false);
        player.closeInventory();
        pinState.deletePin();

        if (!service.isPinSet(playerWrapper.getPlayerModel())) {
            service.setPin(playerWrapper.getPlayerModel(), pin);
            pinState.setPinSet(true);
            pinState.setLogin(true);
            pinState.setTimeToCreateAPin(-1);

            playerWrapper.sendMiniMessage(messages.get().pin.pinSet, "pin", pin);
            return;
        }

        if (service.isValid(player.getUniqueId(), pin)) {
            playerWrapper.sendMiniMessage(messages.get().pin.correctPin);
            pinState.setLogin(true);
            pinState.setTimeToCreateAPin(-1);
            service.updateIp(playerWrapper.getPlayerModel());
        } else {
            playerWrapper.kick(messages.get().pin.thePinIsIncorrect);
        }
    }
}
