package us.blockcade.core.common;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import us.blockcade.core.Main;
import us.blockcade.core.common.modules.chatstar.ChatStarManager;
import us.blockcade.core.util.gui.Title;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;
import us.blockcade.core.util.userdata.events.PlayerLevelupEvent;
import us.blockcade.core.util.userdata.events.PlayerRankUpdateEvent;
import us.blockcade.core.util.userdata.rank.Rank;

import java.util.UUID;

public class MiscHandler implements Listener {

    private Location getSpawnpoint() {
        FileConfiguration config = Main.getInstance().getConfig();
        World w = Bukkit.getWorld(config.getString("spawnpoint.world"));
        double x = config.getDouble("spawnpoint.x");
        double y = config.getDouble("spawnpoint.y");
        double z = config.getDouble("spawnpoint.z");
        float yaw = (float) config.getDouble("spawnpoint.yaw");
        float pitch = (float) config.getDouble("spawnpoint.pitch");

        return new Location(w, x, y, z, yaw, pitch);
    }

    @EventHandler
    public void onSpawnpointJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.teleport(getSpawnpoint());
    }

    @EventHandler
    public void onSpawnpointRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(getSpawnpoint());
    }

    @EventHandler
    public void onVoidDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;

            if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
                event.setCancelled(true);
                player.teleport(getSpawnpoint());
            }
        }
    }

    @EventHandler
    public void onLevelup(PlayerLevelupEvent event) {
        Player player = event.getPlayer().spigot();
        BlockcadeUsers.updateBPlayerCache(player.getUniqueId());

        Title msg = new Title("&e&lLEVEL UP!", "&3" + event.fromLevel() + " &f➤ &b" + event.levelTo());

        new BukkitRunnable() {
            int index = 0;
            String title;

            @Override
            public void run() {

                String c1 = "&e"; // Accent color
                String c2 = "&6"; // Base color

                if (index == 0) title = c1 + "&lEL";
                if (index == 1) title = c1 + "&lV" + c2 + "&lEL ";
                if (index == 2) title = c1 + "&lE" + c2 + "&lVEL " + c1 + "&lU";
                if (index == 3) title = c1 + "&lL" + c2 + "&lEVEL U" + c1 + "&lP";
                if (index == 4) title = c2 + "&lLEVEL UP!";
                if (index == 8) title = c1 + "&lLEVEL UP!";
                if (index == 12) title = c2 + "&lLEVEL UP!";
                if (index == 16) title = c1 + "&lLEVEL UP!";
                if (index == 20) title = c2 + "&lLEVEL UP!";
                if (index == 45) title = c2 + "&lLEV" + c1 + "&lEL " + c2 + "&lUP!";
                if (index == 48) title = c2 + "&lLE" + c1 + "&lV    " + c2 + "&lUP!";
                if (index == 51) title = c2 + "&lL" + c1 + "&lE     " + c1 + "&lU" + c2 + "&lP!";
                if (index == 54) title = c1 + "&lL       " + c1 + "&lP!";

                if (index >= 55) {
                    msg.setTitle("").setSubtitle("").send(player);
                    title = "";
                    cancel();
                }

                if (title != "")
                    msg.setTitle(title).send(player);

                index++;

            }
        }.runTaskTimer(Main.getInstance(), 0, 1);
    }

    @EventHandler
    public void onRankUpdate(PlayerRankUpdateEvent event) {
        BPlayer player = event.getPlayer();
        CommandSender setter = event.getWhoSet();
        Rank previousRank = event.fromRank();
        Rank updatedRank = event.toRank();

        Title title = new Title(updatedRank.getChatColor() + updatedRank.getName(), "&eYour rank has been updated.");
        title.setStay(80).send(player.spigot());

        String sender = "&c&lCONSOLE";
        if (setter instanceof Player) {
            Player setterPlayer = (Player) setter;

            BPlayer bsetter = BlockcadeUsers.getBPlayer(setterPlayer);
            sender = bsetter.getFormattedName();
        }

        Main.getBungeeUtil().messageStaff(player.spigot(), "Rank", sender + " &7set " + previousRank.getChatColor() +
                player.getName() + "&7's rank to " + updatedRank.getChatColor() + updatedRank.getName());
    }

    @EventHandler
    public void onCosmeticInit(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        if (!ChatStarManager.hasChatStarRecord(uuid))
            ChatStarManager.registerUser(uuid);
    }

}
