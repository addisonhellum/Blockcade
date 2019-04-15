package us.blockcade.core.common.game;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.blockcade.core.Main;
import us.blockcade.core.util.format.ColorUtil;

import java.util.ArrayList;
import java.util.List;

public class GameManager implements Listener {

    private static List<BlockcadeGame> games = new ArrayList<>();
    public static List<BlockcadeGame> getGames() { return games; }

    public static void registerGame(BlockcadeGame game) {
        games.add(game);
    }

    public static BlockcadeGame getGame(String name) {
        for (BlockcadeGame game : getGames())
            if (game.getName().equalsIgnoreCase(name)) return game;
        return null;
    }

    public static BlockcadeTeam getTeam(BlockcadeGame game, Player player) {
        if (!game.hasPlayer(player)) return null;
        for (BlockcadeTeam team : game.getTeams())
            if (team.hasPlayer(player)) return team;
        return null;
    }

    public static ChatColor getTeamChatColor(String teamName) {
        ChatColor chatColor;
        try {
            String formatted = teamName.replace(" ", "_").toUpperCase();
            chatColor = ChatColor.valueOf(formatted);
        } catch (Exception e) {
            chatColor = ChatColor.YELLOW;
        }

        return chatColor;
    }

    public static Color getTeamColor(String teamName) {
        return ColorUtil.getColor(teamName);
    }

    public static void activateGame(BlockcadeGame game) {
        for (Listener handler : game.getHandlers())
            Main.getInstance().getServer().getPluginManager().registerEvents(handler, Main.getInstance());
    }

    // TODO: Remove - Testing purposes
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.getName().equals("Araos")) return;

        //BlockcadeGame game = new BlockcadeGame("Testing", new String[]{"Blockcade Development"});
    }

}
