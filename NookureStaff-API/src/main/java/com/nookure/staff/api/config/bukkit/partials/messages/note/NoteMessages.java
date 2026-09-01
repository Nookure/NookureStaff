package com.nookure.staff.api.config.bukkit.partials.messages.note;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class NoteMessages {
    @Setting
    @Comment("""
      The message to send to the player when a note is successfully created.
      """)
    private String successfullyCreated = "{prefix} <green>Successfully created a note for <red>{player.name}!</red>";

    @Setting
    @Comment("Message indicating that data is being saved.")
    private String savingData = "{prefix} <gray>Saving data, please wait ⌚...";

    @Setting
    @Comment("Message indicating that a note is being deleted.")
    private String deletingNote = "{prefix} <gray>Deleting note, please wait ⌚...";

    @Setting
    @Comment("Message confirming that a note has been successfully deleted.")
    private String noteDeleted = "{prefix} <green>Successfully deleted the note with the id {note.id}.";

    @Setting
    @Comment("Message indicating that data is being loaded.")
    private String loadingData = "{prefix} <gray>Loading data, please wait ⌚...";

    @Setting
    @Comment("""
        The header of the note display message.
        Here you can access to all the note fields using the {note.$field} placeholder.
        You can also access to the player fields using the {player.$field} placeholder.
        Review the placeholders in the documentation.
      """)
    private String noteDisplayHeader = """
      🗡 User Note <red><u>#{note.id}</u></red> for <hover:show_text:'<red>Name:</red> {player.name}
      <red>UUID:</red> {player.uuid}
      '><red><u>{player.name}</u></red></hover>
      """;

    @Setting
    @Comment("""
      The body of the note display message.
      Here you can access all the note fields using the {note.$field} placeholder.
      You can also access the player fields using the {player.$field} placeholder.
      Review the placeholders in the documentation.
      """)
    private String noteDisplayBody = """
      ⛏ <red>Active:</red> {note.active}
      ⛏ <red>Note:</red> {note.note}
      ⛏ <red>Show On Join:</red> {note.showOnJoin}
      """;

    @Setting
    @Comment("""
      The body of the note display message for administrator.
      A player is considered an administrator if he has the permission:
       https://javadocs.nookure.com/jd/NookureStaff/constant-values#com.nookure.staff.api.Permissions.STAFF_FREEZE
      Here you can access all the note fields using the {note.$field} placeholder.
      You can also access the player fields using the {player.$field} placeholder.
      Review the placeholders in the documentation.
      """)
    private String noteDisplayBodyAdmin = """
      ⛏ <red>Active:</red> {note.active}
      ⛏ <red>Note:</red> {note.note}
      ⛏ <red>Show On Join:</red> {note.showOnJoin}
      ⛏ <red>Show Only To Administrators:</red> {note.showOnlyToAdministrators}
      """;

    @Setting
    @Comment("""
      The footer of the note display message.
      Here you can access all the note fields using the {note.$field} placeholder.
      You can also access the player fields using the {player.$field} placeholder.
      Review the placeholders in the documentation.
      Includes commands for deleting the note and toggling the show on join status.
      """)
    private String noteDisplayFooter = """
      <b>⚡</b> <click:suggest_command:'/note remove {note.id}'><hover:show_text:'<red>Delete'><u><red>🗑</red></u></hover></click> <b>│</b> <click:run_command:'/note toggle-show {note.id}'><hover:show_text:'<yellow>Toggle Show on Join'><u><yellow>👁</yellow></u></hover></click>
      """;

    @Setting
    @Comment("Message indicating that a note was not found.")
    private String noteNotFound = "{prefix} <red>Could not find the note with the id {note.id}.";

    @Setting
    @Comment("Message indicating that the user does not have any notes.")
    private String userWithoutNotes = "{prefix} <red>{player.name} does not have any notes.";

    @Setting
    @Comment("""
      Pagination control for previous page.
      Includes a clickable element to navigate to the previous page of notes.
      """)
    private String paginationPrevious =
            "<click:run_command:/note list {player.name} {prev_page}><hover:show_text:'<red>Previous Page'><red>«</red></hover></click> ";

    @Setting
    @Comment("""
      Pagination control for next page.
      Includes a clickable element to navigate to the next page of notes.
      """)
    private String paginationNext =
            " <click:run_command:/note list {player.name} {next_page}><hover:show_text:'<red>Next Page'><red>»</red></hover></click>";

    @Setting
    @Comment("""
      Header for pagination display.
      Includes player name, current page number, and total number of pages.
      """)
    private String paginationHeader =
            "<gray>Notes for <red>{player.name}</red> <gray>Page <red>{page}</red> <gray>of <red>{total_pages}</red>.";

    @Setting
    @Comment("""
      Pagination control for specific page number.
      Includes a clickable element to navigate to a specific page of notes.
      """)
    private String paginationFooterNumber =
            "<click:run_command:/note list {player.name} {page}><hover:show_text:'<red>Go to Page {page}'><red>{page}</red></hover></click>";

    @Setting
    @Comment("Current page number in the pagination footer, underlined for emphasis.")
    private String currentPaginationNumber = "<u><red>{page}</red></u>";

    @Setting
    @Comment("Separator used in various parts of the note messages.")
    private String separator = "<gray> │ </gray>";

    @Setting
    @Comment("Message indicating that the show on join status of a note has been toggled on.")
    private String setShowOnJoinOn =
            "{prefix} <green>Successfully toggled on the show on join status for the note with the id {note.id}.";

    @Setting
    @Comment("Message indicating that the show on join status of a note has been toggled off.")
    private String setShowOnJoinOff =
            "{prefix} <green>Successfully toggled off the show on join status for the note with the id {note.id}.";

    public final NoteCommandsMessages commands = new NoteCommandsMessages();

    public String loadingData() {
        return loadingData;
    }

    public String noteDisplayHeader() {
        return noteDisplayHeader;
    }

    public String noteDisplayBody() {
        return noteDisplayBody;
    }

    public String noteDisplayBodyAdmin() {
        return noteDisplayBodyAdmin;
    }

    public String noteDisplayFooter() {
        return noteDisplayFooter;
    }

    public String savingData() {
        return savingData;
    }

    public String successfullyCreated() {
        return successfullyCreated;
    }

    public String noteNotFound() {
        return noteNotFound;
    }

    public String deletingNote() {
        return deletingNote;
    }

    public String noteDeleted() {
        return noteDeleted;
    }

    public String userWithoutNotes() {
        return userWithoutNotes;
    }

    public String paginationPrevious() {
        return paginationPrevious;
    }

    public String paginationNext() {
        return paginationNext;
    }

    public String paginationHeader() {
        return paginationHeader;
    }

    public String paginationFooterNumber() {
        return paginationFooterNumber;
    }

    public String separator() {
        return separator;
    }

    public String currentPaginationNumber() {
        return currentPaginationNumber;
    }

    public String setShowOnJoinOn() {
        return setShowOnJoinOn;
    }

    public String setShowOnJoinOff() {
        return setShowOnJoinOff;
    }
}
