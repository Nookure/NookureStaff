package es.angelillo15.mast.bukkit.utils;

import es.angelillo15.mast.api.event.FreezeMessageEvent;
import es.angelillo15.mast.api.managers.FreezeManager;
import es.angelillo15.mast.bukkit.MAStaff;
import es.angelillo15.mast.bukkit.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FreezeUtils {
    public static void setupMessageSender(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(MAStaff.getPlugin(), () -> {
            FreezeManager.getFrozenPlayers().forEach(player -> {
                Bukkit.getPluginManager().callEvent(new FreezeMessageEvent(player));
            });
        }, 0, 20*5);
    }

    public static void toggleFrozen(Player staff, Player target){
        if(FreezeManager.isFrozen(target)){
            FreezeManager.removePlayer(target);
            target.sendMessage(Messages.GET_FREEZE_UNFROZEN_MESSAGE());
            StaffUtils.asyncStaffBroadcastMessage(Messages.GET_FREEZE_UNFROZEN_BY_MESSAGE().replace("{player}", target.getName()).replace("{staff}", staff.getName()));
        }else{
            FreezeManager.addPlayer(target);
            target.sendMessage(Messages.GET_FREEZE_FROZEN_MESSAGE());
            StaffUtils.asyncStaffBroadcastMessage(Messages.GET_FREEZE_FROZEN_BY_MESSAGE().replace("{player}", target.getName()).replace("{staff}", staff.getName()));
        }
    }
}