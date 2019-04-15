package us.blockcade.core.commands.administrative;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;
import us.blockcade.core.util.userdata.rank.Rank;

import java.util.UUID;

public class RecacheCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("recache")) {
            if (s instanceof Player) {
                Player player = (Player) s;
                BPlayer bplayer = BlockcadeUsers.getBPlayer(player);

                if (!bplayer.hasAccess(Rank.ADMIN)) {
                    player.sendMessage(ChatUtil.format("&cYou are not allowed to do this!"));
                    return false;
                }

                for (Player p : Bukkit.getOnlinePlayers()) {
                    UUID uuid = p.getUniqueId();
                    BlockcadeUsers.updateBPlayerCache(uuid);
                }

                s.sendMessage(ChatUtil.format("&eUpdated all online player caches."));
            }
        }

        return false;
    }

}
