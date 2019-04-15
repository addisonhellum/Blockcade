package us.blockcade.core.common.format;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;
import us.blockcade.core.util.userdata.rank.Rank;

import java.util.Random;

public class StatusMessages implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        BPlayer bplayer = BlockcadeUsers.getBPlayer(player);

        if (!bplayer.getRank().hasPermission(Rank.STEEL)) {
            event.setJoinMessage(null);
            return;
        }

        event.setJoinMessage(bplayer.getRank().getPrefix() + " " + bplayer.getName() + ChatColor.GOLD + " joined the lobby!");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BPlayer bplayer = BlockcadeUsers.getBPlayer(player);

        if (!bplayer.getRank().hasPermission(Rank.BUILDER)) {
            event.setQuitMessage(null);
            return;
        }

        event.setQuitMessage(bplayer.getRank().getPrefix() + " " + bplayer.getName() + ChatColor.GOLD + " left the lobby!");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        BPlayer bplayer = BlockcadeUsers.getBPlayer(player);

        String deathString;
        String[] nexus = new String[] {
                "got oof'd!", "was flattened!", "was rofl-stomped!",
                "ate the dirt!", "went up in smoke!", "got shredded!",
                "needs some milk!", "kicked the bucket!"
        };

        Random r = new Random();
        deathString = nexus[r.nextInt(nexus.length)];

        event.setDeathMessage(bplayer.getRank().getChatColor() + bplayer.getName() + ChatColor.GRAY + " " + deathString);
    }

}
