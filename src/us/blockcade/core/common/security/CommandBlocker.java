package us.blockcade.core.common.security;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;
import us.blockcade.core.util.userdata.rank.Rank;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommandBlocker implements Listener {

    private Map<UUID, Long> lastCommandTimes = new HashMap<>();

    @EventHandler
    public void onBlockedCmd(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        BPlayer bplayer = BlockcadeUsers.getBPlayer(player);
        String msg = event.getMessage();

        if (msg.startsWith("/?") || msg.startsWith("/pl") || msg.startsWith("/plugins") || msg.startsWith("/bukkit:pl")) {
            if (!bplayer.hasAccess(Rank.ADMIN)) {
                event.setCancelled(true);
                player.sendMessage(ChatUtil.format("&cYou are not allowed to do this!"));
            }
        }
    }

    @EventHandler
    public void onSpamBlock(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        long current = System.currentTimeMillis();

        if (lastCommandTimes.containsKey(uuid)) {
            long timestamp = lastCommandTimes.get(uuid);
            if (current - timestamp >= 2000) { // 2 seconds in between
                lastCommandTimes.replace(uuid, current);
                // Allow command through

            } else {
                if (BlockcadeUsers.getBPlayer(player).hasAccess(Rank.ADMIN)) return;
                player.sendMessage(ChatUtil.format("&cPlease do not spam commands!"));
                event.setCancelled(true);
                return;
            }

        } else {
            lastCommandTimes.put(uuid, current);
        }
    }

}
