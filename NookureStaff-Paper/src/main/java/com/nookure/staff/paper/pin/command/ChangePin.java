package com.nookure.staff.paper.pin.command;

import com.google.inject.Inject;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.command.CommandData;
import com.nookure.staff.api.command.CommandSender;
import com.nookure.staff.api.command.StaffCommand;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.service.PinUserService;
import com.nookure.staff.api.state.PinState;
import com.nookure.staff.api.util.transformer.PlayerTransformer;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@CommandData(name = "changepin", permission = Permissions.CHANGE_PIN_PERMISSION, description = "Change your pin")
public class ChangePin extends StaffCommand {
    private final ConfigurationContainer<BukkitMessages> messages;
    private final PinUserService service;

    @Inject
    public ChangePin(
            @NotNull final PlayerTransformer transformer,
            @NotNull final ConfigurationContainer<BukkitMessages> messages,
            @NotNull final PinUserService service) {
        super(transformer, messages);
        this.messages = messages;
        this.service = service;
    }

    @Override
    protected void onStaffCommand(
            @NotNull StaffPlayerWrapper sender, @NotNull String label, @NotNull List<String> args) {
        PinState pinState = sender.getState().getState(PinState.class);

        if (pinState == null) {
            return;
        }

        if (args.isEmpty()) {
            sender.sendMiniMessage(messages.get().pin.changePinCommandUsage);
            return;
        }

        String newPin = args.getFirst();

        if (newPin.length() != 4) {
            sender.sendMiniMessage(messages.get().pin.changePinCommandUsage);
            return;
        }

        service.setPin(sender.getPlayerModel(), newPin);
        sender.sendMiniMessage(messages.get().pin.pinChanged, "pin", newPin);
    }

    @Override
    public @NotNull List<String> onTabComplete(
            @NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args) {
        return List.of("<new pin>");
    }
}
