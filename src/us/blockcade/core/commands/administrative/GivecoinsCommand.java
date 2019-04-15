package us.blockcade.core.commands.administrative;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;
import us.blockcade.core.util.userdata.rank.Rank;

public class GivecoinsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("givecoins")) {
            if (s instanceof Player) {
                Player player = (Player) s;
                BPlayer bplayer = BlockcadeUsers.getBPlayer(player);

                if (!bplayer.hasAccess(Rank.ADMIN)) {
                    player.sendMessage(ChatUtil.format("&cYou are not allowed to do this!"));
                    return false;
                }
            }

            if (args.length == 0) {
                s.sendMessage(ChatUtil.format("&cUsage: /givecoins <Username> <Coins>"));
                return false;

            } else if (args.length == 1) {
                s.sendMessage(ChatUtil.format("&cUsage: /givecoins <Username> <Coins>"));
                return false;

            } else if (args.length == 2) {
                String name = args[0];
                String coinsString = args[1];

                OfflinePlayer target = Bukkit.getOfflinePlayer(name);

                if (!BlockcadeUsers.userHasRecord(target.getUniqueId())) {
                    s.sendMessage(ChatUtil.format("&cThat player has not been registered in our system."));
                    return false;
                }

                try {
                    int coins = Integer.valueOf(coinsString);
                    BPlayer bp = BlockcadeUsers.getBPlayer(target.getUniqueId());
                    bp.incrementCoins(coins);

                    s.sendMessage(ChatUtil.format("&aYou gave " + coins + " coins to " + bp.getName()));

                    if (target.isOnline()) {
                        bp.spigot().sendMessage(ChatUtil.format("&6You were granted &b" + coins + " coins&6!"));
                    }
                }
                catch (Exception e) {
                    s.sendMessage(ChatUtil.format("&cUsage: /givecoins <Username> <Coins>"));
                    return false;
                }

            }

        }

        return false;
    }

}
