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
import org.jetbrains.annotations.NotNull;

@CommandData(
        name = "toggle-show",
        description = "Toggle show on join for a note",
        permission = Permissions.STAFF_NOTES_EDIT)
public class ToggleShowCommand extends Command {
    @Inject
    private UserNoteService userNoteService;

    @Inject
    private ConfigurationContainer<NoteMessages> noteMessages;

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args) {
        if (args.isEmpty()) {
            sender.sendMiniMessage(noteMessages.get().commands.getToggleShowUsage());
            return;
        }

        long id;

        try {
            id = Long.parseLong(args.getFirst());
        } catch (NumberFormatException e) {
            sender.sendMiniMessage("Invalid note id");
            return;
        }

        userNoteService.toggleShowOnJoin(sender, id);
    }
}
