package us.blockcade.core.common.game.achievements;

import org.bukkit.entity.Player;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;

public class Achievement {

    public String getName() {
        return "Achievement";
    }

    public String getDescription() {
        return "Description goes here.";
    }

    public int getCoinsReward() {
        return 50;
    }

    public int getExperienceReward() {
        return 50;
    }

    public String toString() {
        return "";
    }

    public void grant(Player player) {
        BPlayer bplayer = BlockcadeUsers.getBPlayer(player);

        player.sendMessage(toString());
        bplayer.giveExperience(getExperienceReward());
        bplayer.incrementCoins(getCoinsReward());
    }

}
