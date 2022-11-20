package es.angelillo15.mast.bungee.cmd;

import es.angelillo15.mast.bungee.config.ConfigLoader;
import es.angelillo15.mast.bungee.config.Messages;
import es.angelillo15.mast.bungee.utils.TextUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.Map;

public class HelpopCMD extends Command {
    private static Map<String, Long> cooldowns = new HashMap<>();

    public HelpopCMD() {
        super("helpop", "mast.helpop", "helpop");
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        int cooldownTime = ConfigLoader.getConfig().getConfig().getInt("Helpop.cooldown");
        if (sender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(!(p.hasPermission("mast.helpop"))){
                return;
            }

            if(cooldowns.containsKey(p.getName())){

                int timeLeft = cooldownTime + (int) ((cooldowns.get(p.getName()) - System.currentTimeMillis() / 1000));

                if(cooldowns.get(p.getName()) - System.currentTimeMillis() <= 0){
                    cooldowns.remove(p.getName());
                } else {

                    p.sendMessage(new TextComponent(TextUtils.colorize(Messages.getHelpopCooldown().replace("{time}", String.valueOf(timeLeft)))));
                    return;
                }
            }

            p.sendMessage(new TextComponent(TextUtils.colorize(Messages.getHelpopMessage())));

            cooldowns.put(p.getName(), System.currentTimeMillis() / 1000);

        }
    }
}
