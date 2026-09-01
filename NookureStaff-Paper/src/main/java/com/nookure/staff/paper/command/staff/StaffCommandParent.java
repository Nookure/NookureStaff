package com.nookure.staff.paper.command.staff;

import com.google.inject.Inject;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.command.Command;
import com.nookure.staff.api.command.CommandData;
import com.nookure.staff.api.command.CommandParent;
import com.nookure.staff.api.command.CommandSender;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.util.transformer.PlayerTransformer;
import java.lang.annotation.Annotation;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@CommandData(
        name = "staff",
        description = "Staff mode main command",
        permission = Permissions.STAFF_MODE_PERMISSION,
        subCommands = {
            StaffListCommand.class,
            StaffInvSee.class,
            StaffEnderchestSee.class,
        })
public class StaffCommandParent extends CommandParent {
    private final PlayerTransformer transformer;
    private final ConfigurationContainer<BukkitMessages> messages;

    @Inject
    public StaffCommandParent(
            @NotNull final PlayerTransformer transformer,
            @NotNull final ConfigurationContainer<BukkitMessages> messages) {
        this.transformer = transformer;
        this.messages = messages;
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args) {
        if (args.isEmpty()) {
            if (sender instanceof StaffPlayerWrapper staff) {
                staff.toggleStaffMode();
                return;
            }

            if (sender.hasPermission("nookure.staff") && sender instanceof PlayerWrapper player) {
                transformer.player2Staff(player.getUniqueId());
                player.sendMiniMessage(messages.get().youAreNowAnStaff());
                return;
            }

            sender.sendMiniMessage(messages.get().onlyStaffMembersCanExecuteThisCommand());
            return;
        }

        super.onCommand(sender, label, args);
    }

    @Override
    public void prepare() {
        super.prepare();
        getSubCommandData().remove("help");
        getSubCommands().remove("help");
        getSubCommandData().put(" ", new CommandData() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return CommandData.class;
            }

            @Override
            public String name() {
                return "";
            }

            @Override
            public String[] aliases() {
                return new String[0];
            }

            @Override
            public String permission() {
                return Permissions.STAFF_MODE_PERMISSION;
            }

            @Override
            public String usage() {
                return "/staff";
            }

            @Override
            public String description() {
                return "Enter in staff mode";
            }

            @Override
            public Class<? extends Command>[] subCommands() {
                //noinspection unchecked
                return new Class[0];
            }
        });
    }
}
