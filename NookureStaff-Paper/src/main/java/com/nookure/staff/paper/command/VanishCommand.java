package com.nookure.staff.paper.command;

import com.google.inject.Inject;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.command.CommandData;
import com.nookure.staff.api.command.StaffCommand;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.util.transformer.PlayerTransformer;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@CommandData(
        name = "vanish",
        aliases = {"v"},
        permission = "nookure.staff.vanish",
        usage = "/vanish",
        description = "Toggle vanish mode")
public class VanishCommand extends StaffCommand {
    @Inject
    public VanishCommand(
            @NotNull final PlayerTransformer transformer,
            @NotNull final ConfigurationContainer<BukkitMessages> messages) {
        super(transformer, messages);
    }

    @Override
    protected void onStaffCommand(
            @NotNull StaffPlayerWrapper sender, @NotNull String label, @NotNull List<String> args) {
        sender.toggleVanish();
    }
}
