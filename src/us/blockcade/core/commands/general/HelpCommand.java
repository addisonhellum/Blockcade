package us.blockcade.core.commands.general;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import us.blockcade.core.util.format.ChatUtil;

public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("help")) {
            s.sendMessage(ChatUtil.format("&eFor frequently asked questions or other general help, visit " +
                    "&bhttp://blockcade.us/forums"));
            s.sendMessage(ChatUtil.format("&eFor help from the staff team, email &asupport@blockcade.us"));
        }

        return false;
    }

}
