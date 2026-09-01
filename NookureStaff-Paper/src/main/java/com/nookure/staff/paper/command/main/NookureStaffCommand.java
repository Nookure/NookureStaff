package com.nookure.staff.paper.command.main;

import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.command.CommandData;
import com.nookure.staff.api.command.CommandParent;

@CommandData(
        name = "NookureStaff",
        aliases = {"nkstaff", "ns"},
        description = "Main command for NookureStaff",
        permission = Permissions.STAFF_ADMIN_PERMISSION,
        subCommands = {ReloadSubCommand.class})
public class NookureStaffCommand extends CommandParent {}
