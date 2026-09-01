package com.nookure.staff.paper.note.command;

import com.google.inject.Inject;
import com.nookure.core.inv.paper.PaperNookureInventoryEngine;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.command.Command;
import com.nookure.staff.api.command.CommandData;
import com.nookure.staff.api.command.CommandSender;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.config.bukkit.partials.messages.note.NoteMessages;
import com.nookure.staff.api.model.NoteModel;
import com.nookure.staff.api.model.PlayerModel;
import com.nookure.staff.api.service.UserNoteService;
import com.nookure.staff.command.sender.ConsoleCommandSender;
import com.nookure.staff.paper.PaperPlayerWrapper;
import com.nookure.staff.paper.inventory.InventoryList;
import io.ebean.Database;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandData(name = "inventory", description = "Open the notes inventory")
public class InventoryCommand extends Command {
    @Inject
    private UserNoteService userNoteService;

    @Inject
    private ConfigurationContainer<BukkitMessages> messages;

    @Inject
    private ConfigurationContainer<NoteMessages> noteMessages;

    @Inject
    private AtomicReference<PaperNookureInventoryEngine> engine;

    @Inject
    private AtomicReference<Database> db;

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMiniMessage("<red>You must be a player to use this command");
            return;
        }

        PaperPlayerWrapper player = (PaperPlayerWrapper) sender;

        if (args.isEmpty()) {
            sender.sendMiniMessage(noteMessages.get().commands.getInventoryUsage());
            return;
        }

        String username = args.getFirst();
        PlayerModel playerModel = userNoteService.getByUsername(username);

        if (playerModel == null) {
            sender.sendMiniMessage(messages.get().playerNotFound(), "player", username);
            return;
        }

        List<NoteModel> notes =
                db.get().find(NoteModel.class).where().eq("player", playerModel).findList();

        if (!player.hasPermission(Permissions.STAFF_NOTES_ADMIN)) {
            notes = notes.stream()
                    .filter(note -> !note.getShowOnlyToAdministrators())
                    .toList();
        }

        engine.get()
                .openAsync(player.getPlayer(), InventoryList.NOTE_LIST, "player", player, "page", 1, "notes", notes);
    }

    @Override
    public @NotNull List<String> onTabComplete(
            @NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args) {
        return getSuggestionFilter(
                Bukkit.getOnlinePlayers().stream().map(Player::getName).toList(), args.getFirst());
    }
}
