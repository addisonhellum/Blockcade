package us.blockcade.core.common.punishment;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.math.TimeUtil;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;

import java.util.*;

public class PunishmentHandler implements Listener {

    public static Map<UUID, String> inputting = new HashMap<>();

    @EventHandler
    public void onBannedLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        BPlayer bplayer = BlockcadeUsers.getBPlayer(player);

        Punishment p = bplayer.isBanned();
        if (p == null) return;

        event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
        event.setKickMessage(ChatUtil.format("\n\n&7You are banned for:\n&c" + p.getReason()
        + "\n\n&7Punishment ID: &8#" + p.getId() + "\n\n&7Ban expires in: &b" + TimeUtil.asTimeString(p.getTimeRemaining())));
    }

    @EventHandler
    public void onMutedChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        BPlayer bplayer = BlockcadeUsers.getBPlayer(player);

        Punishment p = bplayer.isMuted();
        if (p == null) return;

        event.setCancelled(true);
        player.sendMessage(ChatUtil.format("&cYou are muted for " + p.getReason()));
        player.sendMessage(ChatUtil.format("&cYour mute expires in &b" + TimeUtil.asTimeString(p.getTimeRemaining())));
    }

    @EventHandler
    public void onInputCustom(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!inputting.containsKey(player.getUniqueId())) return;


    }

}
