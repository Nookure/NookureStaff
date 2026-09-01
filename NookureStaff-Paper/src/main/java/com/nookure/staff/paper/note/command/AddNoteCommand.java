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

@CommandData(name = "add", description = "Add a note to a user", permission = Permissions.STAFF_NOTES_ADD)
public class AddNoteCommand extends Command {
    @Inject
    private UserNoteService userNoteService;

    @Inject
    private ConfigurationContainer<NoteMessages> noteMessages;

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args) {
        if (args.size() < 3) {
            sender.sendMiniMessage(noteMessages.get().commands.getAddNoteUsage());
        }

        String target = args.getFirst();
        boolean showOnJoin;
        boolean showOnlyToAdmins = false;
        String note;

        try {
            showOnJoin = Boolean.parseBoolean(args.get(1));
        } catch (IllegalArgumentException e) {
            sender.sendMiniMessage("Invalid value for show on join");
            return;
        }

        if (args.getLast().equalsIgnoreCase("true") || args.getLast().equalsIgnoreCase("false")) {
            showOnlyToAdmins = Boolean.parseBoolean(args.getLast());
            note = String.join(" ", args.subList(2, args.size() - 1));
        } else {
            note = String.join(" ", args.subList(2, args.size()));
        }

        userNoteService.addNote(sender, target, note, showOnJoin, showOnlyToAdmins);
    }

    @Override
    public @NotNull List<String> onTabComplete(
            @NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args) {
        return switch (args.size()) {
            case 1 ->
                Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.startsWith(args.getFirst() == null ? "" : args.getFirst()))
                        .toList();
            case 2 -> getSuggestionFilter(List.of("<show on join>", "true", "false"), args.get(1));
            case 3 -> getSuggestionFilter(List.of("<note>"), args.get(2));
            default -> {
                if (sender.hasPermission(Permissions.STAFF_NOTES_ADMIN)) {
                    yield getSuggestionFilter(
                            List.of("<note> | <show only to admins>", "true", "false"), args.getLast());
                } else {
                    yield getSuggestionFilter(List.of("<note>"), args.getLast());
                }
            }
        };
    }
}
