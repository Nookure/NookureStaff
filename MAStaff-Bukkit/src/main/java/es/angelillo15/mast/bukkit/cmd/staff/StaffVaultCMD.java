package es.angelillo15.mast.bukkit.cmd.staff;

import es.angelillo15.mast.api.IStaffPlayer;
import es.angelillo15.mast.api.cmd.LegacySubCommand;
import es.angelillo15.mast.api.managers.StaffPlayersManagers;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffVaultCMD extends LegacySubCommand {
    @Override
    public String getName() {
        return "vault";
    }

    @Override
    public String getDescription() {
        return "Opens the staff vault";
    }

    @Override
    public String getSyntax() {
        return "/staff vault";
    }

    @Override
    public String getPermission() {
        return "mast.staff.vault";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        if (!(StaffPlayersManagers.isStaffPlayer((Player) sender))) {
            return;
        }

        IStaffPlayer staffPlayer = StaffPlayersManagers.getStaffPlayer((Player) sender);

        staffPlayer.openStaffVault();
    }
}
