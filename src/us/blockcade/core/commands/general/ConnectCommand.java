package us.blockcade.core.commands.general;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.Main;
import us.blockcade.core.util.format.ChatUtil;

public class ConnectCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("connect")) {
            if (!(s instanceof Player)) {
                s.sendMessage(ChatUtil.format("&cConsole, me and you are gonna have words."));
                return false;
            }
            Player player = (Player) s;

            if (args.length == 0) {
                player.sendMessage(ChatUtil.format("&cYou are not allowed to do this."));
                return false;

            } else if (args.length == 1) {
                String server = args[0];

                player.sendMessage(ChatUtil.format("&aSending you to server " + server));
                Main.getBungeeUtil().sendPlayerToServer(player, server);
            }
        }

        return false;
    }

}
