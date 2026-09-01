package com.nookure.staff.paper.pin.command;

import com.google.inject.Inject;
import com.nookure.core.inv.paper.PaperNookureInventoryEngine;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.command.CommandData;
import com.nookure.staff.api.command.StaffCommand;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.state.PinState;
import com.nookure.staff.api.util.transformer.PlayerTransformer;
import com.nookure.staff.paper.StaffPaperPlayerWrapper;
import com.nookure.staff.paper.inventory.InventoryList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;

@CommandData(name = "setpin", permission = Permissions.SET_PIN_PERMISSION, description = "Set your pin")
public class SetPinCommand extends StaffCommand {
    private final AtomicReference<PaperNookureInventoryEngine> engine;
    private final ConfigurationContainer<BukkitMessages> messages;

    @Inject
    public SetPinCommand(
            @NotNull final PlayerTransformer transformer,
            @NotNull final ConfigurationContainer<BukkitMessages> messages,
            @NotNull final AtomicReference<PaperNookureInventoryEngine> engine) {
        super(transformer, messages);
        this.engine = engine;
        this.messages = messages;
    }

    @Override
    protected void onStaffCommand(
            @NotNull StaffPlayerWrapper sender, @NotNull String label, @NotNull List<String> args) {
        if (sender instanceof StaffPaperPlayerWrapper player) {
            PinState pinState = player.getState().getState(PinState.class);

            if (pinState == null) {
                return;
            }

            if (pinState.isLogin()) {
                sender.sendMiniMessage(messages.get().pin.alreadyLoggedIn);
                return;
            }

            engine.get()
                    .openAsync(
                            player.getPlayer(),
                            InventoryList.ENTER_PIN,
                            "player",
                            player.getPlayer(),
                            "pinSize",
                            pinState.getPin().length());
        }
    }
}
