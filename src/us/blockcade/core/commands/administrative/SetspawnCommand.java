package us.blockcade.core.commands.administrative;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import us.blockcade.core.Main;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;
import us.blockcade.core.util.userdata.rank.Rank;

public class SetspawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setspawn")) {
            if (!(s instanceof Player)) {
                s.sendMessage(ChatUtil.format("&cConsole, for the last time, you can't run these commands."));
                return false;
            }
            Player player = (Player) s;
            BPlayer bplayer = BlockcadeUsers.getBPlayer(player);

            if (!bplayer.getRank().hasPermission(Rank.ADMIN)) {
                player.sendMessage(ChatUtil.format("&cYou are not allowed to do this!"));
                return false;
            }

            Location loc = player.getLocation();

            FileConfiguration config = Main.getInstance().getConfig();
            config.set("spawnpoint.world", loc.getWorld().getName());
            config.set("spawnpoint.x", loc.getBlockX() + 0.5);
            config.set("spawnpoint.y", loc.getBlockY() + 1);
            config.set("spawnpoint.z", loc.getBlockZ() + 0.5);
            config.set("spawnpoint.yaw", loc.getYaw());
            config.set("spawnpoint.pitch", loc.getPitch());
            Main.getInstance().saveConfig();

            player.sendMessage(ChatUtil.format("&aSuccess! Spawn location has been updated to your current location."));
        }

        return false;
    }

}
