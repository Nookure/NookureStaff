package com.nookure.staff.paper.command.main;

import com.google.inject.Inject;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.command.Command;
import com.nookure.staff.api.command.CommandData;
import com.nookure.staff.api.command.CommandSender;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@CommandData(name = "reload", description = "Reloads the plugin")
public class ReloadSubCommand extends Command {
    @Inject
    private NookureStaff nookureStaff;

    @Inject
    private ConfigurationContainer<BukkitMessages> messages;

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args) {
        nookureStaff.reload();
        sender.sendMiniMessage(messages.get().reload());
    }
}
