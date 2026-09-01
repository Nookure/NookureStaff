package com.nookure.staff.paper.note.command;

import com.google.inject.Inject;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.command.Command;
import com.nookure.staff.api.command.CommandData;
import com.nookure.staff.api.command.CommandSender;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.partials.messages.note.NoteMessages;
import com.nookure.staff.api.service.UserNoteService;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandData(name = "list", description = "List notes of a user", permission = Permissions.STAFF_NOTES_LIST)
public class ListNoteCommand extends Command {
    @Inject
    private UserNoteService userNoteService;

    @Inject
    private ConfigurationContainer<NoteMessages> noteMessages;

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args) {
        if (args.isEmpty()) {
            sender.sendMiniMessage(noteMessages.get().commands.getListNoteUsage());
            return;
        }

        String username = args.get(0);

        int page = 0;

        if (args.size() > 1) {
            try {
                page = Integer.parseInt(args.get(1));
            } catch (NumberFormatException e) {
                sender.sendMiniMessage("Invalid page number");
                return;
            }
        }

        userNoteService.displayNotes(sender, username, page);
    }

    @Override
    public @NotNull List<String> onTabComplete(
            @NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args) {
        return getSuggestionFilter(
                Bukkit.getOnlinePlayers().stream().map(Player::getName).toList(), args.getFirst());
    }
}
