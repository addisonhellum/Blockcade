package us.blockcade.core.commands.administrative;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.Main;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.gui.ActionBar;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;
import us.blockcade.core.util.userdata.rank.Rank;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AoCommand implements CommandExecutor {

    private static int taskId = -1;
    private static Set<UUID> active = new HashSet<>();

    public static boolean hasOverride(Player player) {
        return active.contains(player.getUniqueId());
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ao")) {
            if (!(s instanceof Player)) {
                s.sendMessage(ChatUtil.format("&cSorry Console, only us muggles require the powers of the AO."));
                return false;
            }
            Player player = (Player) s;
            BPlayer bplayer = BlockcadeUsers.getBPlayer(player);

            if (!bplayer.getRank().hasPermission(Rank.ADMIN)) {
                player.sendMessage(ChatUtil.format("&cYou do not have the rank required for this!"));
                return false;
            }

            String border = ChatColor.GOLD + "-----------------------------------------------------";

            if (active.contains(player.getUniqueId())) {
                active.remove(player.getUniqueId());
                player.sendMessage(border);
                player.sendMessage(ChatUtil.format("&aAdmin Override: &cDisabled"));
                player.sendMessage(ChatUtil.format("&eYou will now experience the server as a normal player would."));
                player.sendMessage(border);

            } else {
                if (taskId == -1) {
                    taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            for (UUID uuid : active) {
                                OfflinePlayer o = Bukkit.getOfflinePlayer(uuid);
                                if (o.isOnline()) {
                                    Player p = Bukkit.getPlayer(uuid);
                                    ActionBar.sendActionBarMessage(p, ChatUtil.format("&aAdmin Override: &bActive"));
                                }
                            }
                        }
                    }, 0, 20);
                }

                active.add(player.getUniqueId());
                player.sendMessage(border);
                player.sendMessage(ChatUtil.format("&aAdmin Override: &bEnabled"));
                player.sendMessage(ChatUtil.format("&eYou can now bypass any server regulation restrictions."));
                player.sendMessage(border);
            }
        }

        return false;
    }

}
