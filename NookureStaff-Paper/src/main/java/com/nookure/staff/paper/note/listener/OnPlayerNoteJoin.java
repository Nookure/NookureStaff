package com.nookure.staff.paper.note.listener;

import com.google.inject.Inject;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.api.model.NoteModel;
import com.nookure.staff.api.model.PlayerModel;
import com.nookure.staff.api.service.UserNoteService;
import com.nookure.staff.api.util.Scheduler;
import io.ebean.Database;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerNoteJoin implements Listener {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Inject
    private AtomicReference<Database> database;

    @Inject
    private Scheduler scheduler;

    @Inject
    private UserNoteService userNoteService;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        scheduler.async(() -> {
            PlayerModel player = database.get()
                    .find(PlayerModel.class)
                    .where()
                    .eq("uuid", event.getPlayer().getUniqueId())
                    .findOne();

            if (player == null) {
                return;
            }

            database.get()
                    .find(NoteModel.class)
                    .where()
                    .eq("player", player)
                    .findList()
                    .forEach(noteModel -> {
                        if (noteModel.getShowOnJoin()) {
                            getFilteredPlayerWrappers(noteModel.getShowOnlyToAdministrators())
                                    .forEach(playerWrapper ->
                                            userNoteService.displayNote(playerWrapper, player, noteModel));
                        }
                    });
        });
    }

    private Stream<PlayerWrapper> getFilteredPlayerWrappers(boolean showOnlyToAdministrators) {
        return playerWrapperManager.stream().filter(playerWrapper -> {
            if (playerWrapper instanceof StaffPlayerWrapper) {
                if (showOnlyToAdministrators) {
                    return playerWrapper.hasPermission(Permissions.STAFF_NOTES_ADMIN);
                }

                return true;
            }

            return false;
        });
    }
}
