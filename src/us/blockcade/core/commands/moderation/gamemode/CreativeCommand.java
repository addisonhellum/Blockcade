package us.blockcade.core.commands.moderation.gamemode;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;
import us.blockcade.core.util.userdata.rank.Rank;

public class CreativeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("creative")) {
            if (!(s instanceof Player)) {
                s.sendMessage(ChatUtil.format("&cSeriously? What did you think would happen. You're not a human."));
                return false;
            }
            Player player = (Player) s;
            BPlayer bplayer = BlockcadeUsers.getBPlayer(player);

            if (!bplayer.getRank().hasPermission(Rank.BUILDER)) {
                player.sendMessage(ChatUtil.format("&cYou are not allowed to do this!"));
                return false;
            }

            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage(ChatUtil.format("&aYour gamemode has been updated: &bCreative"));
        }

        return false;
    }

}
