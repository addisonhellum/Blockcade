package us.blockcade.core.util.gui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NametagUtil {

    public static void registerRankTeams(Scoreboard scoreboard) {
        for (ChatColor color : ChatColor.values()) {
            String name = color.name();

            if (scoreboard.getTeam(name) == null) {
                scoreboard.registerNewTeam(name);
                scoreboard.getTeam(name).setPrefix(color + "");
            }
        }
    }

    public static void setNametagColor(Player player, ChatColor color) {
        Scoreboard scoreboard = player.getScoreboard();
        registerRankTeams(scoreboard);

        for (Team team : scoreboard.getTeams())
            if (team.hasEntry(player.getName()))
                team.removeEntry(player.getName());

        scoreboard.getTeam(color.name()).addEntry(player.getName());
    }

}
