package es.angelillo15.mast.api.punishments.utils;

import es.angelillo15.mast.api.cmd.sender.CommandSender;
import es.angelillo15.mast.api.data.UserData;
import es.angelillo15.mast.api.punishments.data.DataManager;
import es.angelillo15.mast.api.punishments.enums.ErrorTypes;

import java.util.UUID;

public class BanUtils {
    public static void ban(String uuid, String username, String ip, String reason, String banned_by_uuid, String banned_by_name, boolean active, long time, long until, boolean ipban) {
        DataManager.getDataManager().ban(uuid, username, ip, reason, banned_by_uuid, banned_by_name, active, time, until, ipban);
    }

    public static ErrorTypes ban(CommandSender banned_by, UserData data, String reason, long until, boolean ipban) {
        if (data == null) {
            return ErrorTypes.NULL_DATA;
        }
        if (DataManager.getDataManager().isPermBanned(data.getUsername())) {
            return ErrorTypes.PLAYER_ALREADY_PERM_BANNED;
        }

        ban(data.getUUID(), data.getUsername(), data.getLastIP(), reason, banned_by.getUniqueId(), banned_by.getName(), true, System.currentTimeMillis(), until, ipban);

        return ErrorTypes.SUCCESS;
    }

    public static boolean isBanned(String uuid) {
        return DataManager.getDataManager().isTempBanned(UUID.fromString(uuid));
    }

    public static ErrorTypes permBan(CommandSender banned_by, UserData target, String reason, boolean ipban) {
        return ban(banned_by, target, reason, 0, ipban);
    }

    public static UserData getUserData(String username) {
        UserData data = es.angelillo15.mast.api.data.DataManager.getDataManager().getUserData(username);

        if (data == null) {
            data = new UserData();
            data.setUsername(username);
        }

        return data;
    }

    public static void unban(String uuid) {
        DataManager.getDataManager().setBanActive(uuid, false);
    }
}
