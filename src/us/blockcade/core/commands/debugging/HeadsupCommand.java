package us.blockcade.core.commands.debugging;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HeadsupCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("headsup")) {

            if (s instanceof Player) {
                Player p = (Player) s;
                if (!p.isOp()) {
                    return false;
                }
            }

            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "HEADS UP! " + ChatColor.YELLOW + "The " +
                    "server needs a mandatory reload for maintenance/development purposes. If now is not a good time, " +
                    "speak up in the chat.");
            Bukkit.broadcastMessage(ChatColor.RED + "Restarting in 10 seconds...");
            Bukkit.broadcastMessage(" ");
        }

        return false;
    }

}
