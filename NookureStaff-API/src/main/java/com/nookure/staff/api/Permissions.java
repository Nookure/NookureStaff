package com.nookure.staff.api;

/**
 * Contains all the permissions used by the plugin.
 *
 * @since 1.0.0
 */
public final class Permissions {
    /**
     * General permission for staff members.
     * This permission is required to use any staff command.
     * With this permission you also will appear as a {@link StaffPlayerWrapper}
     * instead of a {@link PlayerWrapper}.
     */
    public static final String STAFF_PERMISSION = "nookure.staff";

    /**
     * Permission to use the staff mode.
     * This permission is required to use the staff mode.
     */
    public static final String STAFF_MODE_PERMISSION = "nookure.staff.mode";

    /**
     * Permission to bypass the blocked commands in staff mode.
     * This permission is required to bypass the blocked
     * commands in staff mode.
     */
    public static final String STAFF_MODE_COMMANDS_BYPASS = "nookure.staff.mode.commands.bypass";

    /**
     * Permission to use the admin commands.
     * This permission is required to use any admin command.
     */
    public static final String STAFF_ADMIN_PERMISSION = "nookure.staff.admin";

    /**
     * Permission to use the player list command.
     * This permission is required to use the player list command.
     */
    public static final String PLAYER_LIST_PERMISSION = "nookure.staff.player-list";

    /**
     * Permission to use the player actions command.
     * This permission is required to use the player actions command.
     */
    public static final String PLAYER_ACTIONS_PERMISSION = "nookure.staff.players.actions";

    /**
     * Permission to use the helpop command.
     * This permission is required to use the helpop command.
     * The helpop command is used to request help from staff members.
     * The staff members with the permission {@link #HELPOP_RECEIVE_PERMISSION}
     */
    public static final String HELPOP_COMMAND_PERMISSION = "nookure.staff.helpop";

    /**
     * Permission to receive helpop requests.
     * This permission is required to receive helpop requests.
     * The helpop requests are sent by players with the permission {@link #HELPOP_COMMAND_PERMISSION}.
     */
    public static final String HELPOP_RECEIVE_PERMISSION = "nookure.staff.helpop.receive";

    public static final String ACTION_BAR_PERMISSION = "nookure.staff.actionbar";

    /**
     * Permission to build in staff mode.
     * This permission is required to build in staff mode.
     */
    public static final String STAFF_MODE_BUILD = "nookure.staff.build";

    /**
     * Permission to enter in vanish.
     */
    public static final String STAFF_VANISH = "nookure.staff.vanish";

    /**
     * Permission to see the players in vanish.
     */
    public static final String STAFF_VANISH_SEE = "nookure.staff.vanish.see";

    /**
     * Permission to freeze a player.
     */
    public static final String STAFF_FREEZE = "nookure.staff.freeze";

    /**
     * Permission to see the inventory of a player.
     */
    public static final String STAFF_INVSEE = "nookure.staff.invsee";

    /**
     * Permission to see the enderchest of a player.
     */
    public static final String STAFF_ENDERCHEST = "nookure.staff.enderchest";
    /**
     * Permission to modify the inventory of a player.
     */
    public static final String STAFF_INVSEE_MODIFY = "nookure.staff.invsee.modify";

    public static final String STAFF_ENDERCHEST_MODIFY = "nookure.staff.enderchest.modify";

    /**
     * Permission to bypass the freeze.
     */
    public static final String STAFF_FREEZE_BYPASS = "nookure.staff.freeze.bypass";

    /**
     * Permission to use the staff chat.
     */
    public static final String STAFF_CHAT = "nookure.staff.staffchat";

    /**
     * Permissions to have administrator notes.
     */
    public static final String STAFF_NOTES = "nookure.staff.notes";

    public static final String STAFF_NOTES_ADMIN = "nookure.staff.notes.admin";

    public static final String STAFF_NOTES_LIST = "nookure.staff.notes.list";

    public static final String STAFF_NOTES_ADD = "nookure.staff.notes.add";

    public static final String STAFF_NOTES_REMOVE = "nookure.staff.notes.remove";

    public static final String STAFF_NOTES_EDIT = "nookure.staff.notes.edit";

    /**
     * Permission to use set pin command
     */
    public static final String SET_PIN_PERMISSION = "nookure.staff.setpin";

    public static final String CHANGE_PIN_PERMISSION = "nookure.staff.changepin";
    public static final String DELETE_PIN_PERMISSION = "nookure.staff.deletepin";

    private Permissions() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}
