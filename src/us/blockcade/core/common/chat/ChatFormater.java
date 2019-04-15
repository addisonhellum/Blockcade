package us.blockcade.core.common.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.blockcade.core.common.modules.chatstar.ChatStar;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;
import us.blockcade.core.util.userdata.rank.Rank;

public class ChatFormater implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        BPlayer bp = BlockcadeUsers.getBPlayer(player);
        Rank rank = bp.getRank();

        ChatStar star = bp.getChatStar();
        String starText = star.getText();
        if (rank.getId() == 0) {
            starText = "";
        }

        if (rank.hasPermission(Rank.ADMIN)) {
            message = ChatUtil.format(message);
        }

        String format = rank.getPrefix() + starText + " " + bp.getFormattedName() + ChatColor.GRAY + ": " +
                rank.getTextColor() + message;
        if (rank.getId() == 0) format = bp.getFormattedName() + ChatColor.GRAY + ": " + rank.getTextColor() + message;

        event.setFormat(format.replace("%", "%%"));
    }

}
