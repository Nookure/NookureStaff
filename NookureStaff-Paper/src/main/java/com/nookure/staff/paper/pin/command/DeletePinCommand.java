package com.nookure.staff.paper.pin.command;

import com.google.inject.Inject;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.command.CommandData;
import com.nookure.staff.api.command.StaffCommand;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.model.PinModel;
import com.nookure.staff.api.model.PlayerModel;
import com.nookure.staff.api.util.transformer.PlayerTransformer;
import io.ebean.Database;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;

@CommandData(name = "deletepin", permission = Permissions.SET_PIN_PERMISSION, description = "Delete your pin")
public class DeletePinCommand extends StaffCommand {
    private final AtomicReference<Database> database;
    private final ConfigurationContainer<BukkitMessages> messages;

    @Inject
    public DeletePinCommand(
            @NotNull final PlayerTransformer transformer,
            @NotNull final ConfigurationContainer<BukkitMessages> messages,
            @NotNull final AtomicReference<Database> database) {
        super(transformer, messages);
        this.database = database;
        this.messages = messages;
    }

    @Override
    protected void onStaffCommand(
            @NotNull StaffPlayerWrapper sender, @NotNull String label, @NotNull List<String> args) {
        if (args.isEmpty()) {
            sender.sendMiniMessage(messages.get().pin.pinDeleteCommandUsage);
            return;
        }

        String name = args.getFirst();

        PlayerModel playerModel;

        if (name.length() == 36) {
            playerModel = database.get()
                    .find(PlayerModel.class)
                    .where()
                    .eq("uuid", name)
                    .findOne();
        } else {
            playerModel = database.get()
                    .find(PlayerModel.class)
                    .where()
                    .eq("name", name)
                    .findOne();
        }

        if (playerModel == null) {
            sender.sendMiniMessage(messages.get().playerNotFound());
            return;
        }

        PinModel model = database.get()
                .find(PinModel.class)
                .where()
                .eq("player", playerModel)
                .findOne();

        if (model == null) {
            sender.sendMiniMessage(messages.get().playerNotFound());
            return;
        }

        model.delete();
        sender.sendMiniMessage(messages.get().pin.pinDeleted.replace("{player}", playerModel.getName()));
    }
}
