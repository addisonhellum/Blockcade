package us.blockcade.core.commands.general;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.common.modules.chatstar.ChatStarManager;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;
import us.blockcade.core.util.userdata.rank.Rank;

public class ChatstarCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("chatstar")) {
            if (!(s instanceof Player)) {
                s.sendMessage(ChatUtil.format("&cConsole, for the love of Notch, you can't do these things."));
                return false;
            }
            Player player = (Player) s;
            BPlayer bplayer = BlockcadeUsers.getBPlayer(player);

            if (!bplayer.hasAccess(Rank.STEEL)) {
                player.sendMessage(ChatUtil.format("&cYou are not allowed to do this!"));
                return false;
            }

            if (args.length == 2) {
                String key = "iagoarjgi4oioj2gmr";
                String idString = args[1];

                if (!args[0].equals(key)) return false;

                try {
                    int id = Integer.valueOf(idString);
                    ChatStarManager.setChatStar(player.getUniqueId(), id);

                    player.sendMessage(ChatUtil.format("&aYour chat star has been updated!"));
                }
                catch (Exception e) {
                    player.sendMessage(ChatUtil.format("&cImproper ID specified. #s 0-14 allowed."));
                }
            }
        }

        return false;
    }

}
