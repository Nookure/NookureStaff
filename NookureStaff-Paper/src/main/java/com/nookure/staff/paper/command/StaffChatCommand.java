package com.nookure.staff.paper.command;

import com.google.inject.Inject;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.command.CommandData;
import com.nookure.staff.api.command.StaffCommand;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitConfig;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.util.ServerUtils;
import com.nookure.staff.api.util.transformer.PlayerTransformer;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@CommandData(
        name = "staffchat",
        permission = Permissions.STAFF_CHAT,
        aliases = {"sc"},
        usage = "/staffchat <message>",
        description = "Send a message to all staff members")
public class StaffChatCommand extends StaffCommand {
    private final ServerUtils serverUtils;
    private final ConfigurationContainer<BukkitMessages> messages;
    private final ConfigurationContainer<BukkitConfig> config;

    @Inject
    public StaffChatCommand(
            @NotNull final PlayerTransformer transformer,
            @NotNull final ConfigurationContainer<BukkitMessages> messages,
            @NotNull final ServerUtils serverUtils,
            @NotNull final ConfigurationContainer<BukkitConfig> config) {
        super(transformer, messages);
        this.serverUtils = serverUtils;
        this.messages = messages;
        this.config = config;
    }

    @Override
    protected void onStaffCommand(
            @NotNull StaffPlayerWrapper sender, @NotNull String label, @NotNull List<String> args) {
        if (args.isEmpty()) {
            if (sender.isStaffChatAsDefault()) {
                sender.setStaffChatAsDefault(false);
                sender.sendMiniMessage(messages.get().staffChat.disable());
                return;
            }

            sender.setStaffChatAsDefault(true);
            sender.sendMiniMessage(messages.get().staffChat.enable());

            return;
        }

        String messageArgs = String.join(" ", args);

        String message = messages.get()
                .staffChat
                .format()
                .replace("{player}", sender.getName())
                .replace("{server}", config.get().getServerName())
                .replace("{message}", messageArgs);

        serverUtils.broadcast(message, Permissions.STAFF_CHAT);
    }
}
