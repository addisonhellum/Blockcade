package us.blockcade.core.common.format;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.gui.TabTitleManager;

public class HeaderHandler implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        TabTitleManager.sendPlayerListTab(player,
                ChatUtil.format("&bYou are playing on &a&lBlockcade.us"),
                ChatUtil.format("&6Purchase ranks @ &b&lstore.blockcade.us"));
    }

}
