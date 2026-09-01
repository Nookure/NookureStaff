package com.nookure.staff.paper.note.command;

import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.command.CommandData;
import com.nookure.staff.api.command.CommandParent;

@CommandData(
        name = "note",
        description = "Manage user notes",
        permission = Permissions.STAFF_NOTES,
        aliases = {"notes"},
        subCommands = {
            AddNoteCommand.class,
            RemoveNoteCommand.class,
            ListNoteCommand.class,
            ToggleShowCommand.class,
            InventoryCommand.class
        })
public class ParentNoteCommand extends CommandParent {}
