package es.angelillo15.mast.api.punishments.cmd.cache;

import es.angelillo15.mast.api.TextUtils;
import es.angelillo15.mast.api.cache.BanCache;
import com.nookure.mast.api.cmd.SubCommand;
import com.nookure.mast.api.cmd.sender.CommandSender;
import es.angelillo15.mast.api.config.punishments.Messages;

public class BansCache extends SubCommand {
  @Override
  public String getName() {
    return "bans";
  }

  @Override
  public String getDescription() {
    return "Clears the bans cache";
  }

  @Override
  public String getSyntax() {
    return "/clearcache bans";
  }

  @Override
  public String getPermission() {
    return "mastaff.punishments.clearcache.bans";
  }

  @Override
  public void onCommand(CommandSender sender, String label, String[] args) {
    BanCache.clearCache();
    sender.sendMessage(
        TextUtils.colorize(Messages.Default.prefix() + "&f " + "Bans cache cleared"));
  }
}
