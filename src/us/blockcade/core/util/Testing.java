package us.blockcade.core.util;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import us.blockcade.core.util.effect.FireworkEffectPlayer;

public class Testing implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //BPlayer bp = new BPlayer(event.getPlayer().getUniqueId());
        //bp.setRank(Rank.HELPER);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (event.isSneaking()) return;

    }

}
