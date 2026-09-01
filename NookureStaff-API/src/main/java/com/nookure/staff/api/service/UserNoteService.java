package com.nookure.staff.api.service;

import com.nookure.staff.api.command.CommandSender;
import com.nookure.staff.api.model.NoteModel;
import com.nookure.staff.api.model.PlayerModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This service is used to manage the user notes.
 */
public interface UserNoteService {
    /**
     * Adds a note to a user.
     * <p>
     * The note will be shown when a staff member joins the server if {@code showOnJoin} is true.
     * The note will only be shown to administrators if {@code showOnlyToAdministrators} is true.
     * <br>
     * The note will be saved in the database.
     * <br>
     * If the target user does not exist in the player database, the note will not be saved,
     * send a message to the staff member who executed the command.
     * <br>
     * The note will be propagated to all the servers in the network via the messaging service.
     * </p>
     *
     * @param staff                    the staff member who is adding the note
     * @param targetUsername           the username of the user who will receive the note
     * @param note                     the note to add
     * @param showOnJoin               whether the note should be shown when a staff member joins the server
     * @param showOnlyToAdministrators whether the note should only be shown to administrators
     */
    void addNote(
            @NotNull CommandSender staff,
            @NotNull String targetUsername,
            @NotNull String note,
            boolean showOnJoin,
            boolean showOnlyToAdministrators);

    /**
     * Remove a note.
     *
     * @param staff the staff member who is removing the note
     * @param id    the id of the note to remove
     */
    void removeNote(@NotNull CommandSender staff, @NotNull Long id);

    /**
     * Display the notes of a user.
     *
     * @param staff          the staff member who is requesting the notes
     * @param targetUsername the username of the user whose notes will be displayed
     * @param page           the page of the notes to display
     */
    void displayNotes(@NotNull CommandSender staff, @NotNull String targetUsername, int page);

    /**
     * Display the notes of a user.
     *
     * @param staff          the staff member who is requesting the notes
     * @param targetUsername the username of the user whose notes will be displayed
     */
    default void displayNotes(@NotNull CommandSender staff, @NotNull String targetUsername) {
        displayNotes(staff, targetUsername, 0);
    }

    /**
     * Display a note to a staff member.
     *
     * @param staff  the staff member who is requesting the note
     * @param player the player who has the note
     * @param note   the note to display
     */
    void displayNote(@NotNull CommandSender staff, @NotNull PlayerModel player, @NotNull NoteModel note);

    /**
     * Toggle the show on join status of a note.
     *
     * @param staff the staff member who is toggling the show on join status
     * @param id    the id of the note to toggle
     */
    void toggleShowOnJoin(@NotNull CommandSender staff, @NotNull Long id);

    /**
     * Get a player by username.
     *
     * @param targetUsername the username of the player
     */
    @Nullable PlayerModel getByUsername(String targetUsername);
}
