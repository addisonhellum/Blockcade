package us.blockcade.core.commands.general;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import us.blockcade.core.Main;
import us.blockcade.core.util.format.ChatUtil;

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            if (!(s instanceof Player)) {
                s.sendMessage(ChatUtil.format("&cConsole, you're pissin' me off. You can't do this human stuff."));
                return false;
            }
            Player player = (Player) s;

            FileConfiguration config = Main.getInstance().getConfig();
            World w = Bukkit.getWorld(config.getString("spawnpoint.world"));
            double x = config.getDouble("spawnpoint.x");
            double y = config.getDouble("spawnpoint.y");
            double z = config.getDouble("spawnpoint.z");
            float yaw = (float) config.getDouble("spawnpoint.yaw");
            float pitch = (float) config.getDouble("spawnpoint.pitch");

            player.teleport(new Location(w, x, y, z, yaw, pitch));
        }

        return false;
    }

}
