package com.nookure.staff.api.command;

import com.google.inject.Inject;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.util.transformer.PlayerTransformer;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a command that can be executed by a {@link com.nookure.staff.api.StaffPlayerWrapper}.
 * <p>
 * This class is abstract and must be extended.
 * The class must be annotated with {@link CommandData}.
 * </p>
 */
public abstract class StaffCommand extends Command {
    private final PlayerTransformer transformer;
    private final ConfigurationContainer<BukkitMessages> messages;

    @Inject
    public StaffCommand(
            @NotNull final PlayerTransformer transformer,
            @NotNull final ConfigurationContainer<BukkitMessages> messages) {
        this.transformer = transformer;
        this.messages = messages;
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args) {
        if (sender instanceof StaffPlayerWrapper) {
            onStaffCommand((StaffPlayerWrapper) sender, label, args);
            return;
        }

        if (sender.hasPermission("nookure.staff") && sender instanceof PlayerWrapper player) {
            transformer.player2Staff(player.getUniqueId());
            player.sendMiniMessage(messages.get().youAreNowAnStaff());
            return;
        }

        sender.sendMiniMessage(messages.get().onlyStaffMembersCanExecuteThisCommand());
    }

    /**
     * This method is called when the command is executed
     * by a {@link com.nookure.staff.api.StaffPlayerWrapper}.
     *
     * @param sender The sender of the command
     * @param label  The label of the command
     * @param args   The arguments of the command
     */
    protected abstract void onStaffCommand(
            @NotNull StaffPlayerWrapper sender, @NotNull String label, @NotNull List<String> args);
}
