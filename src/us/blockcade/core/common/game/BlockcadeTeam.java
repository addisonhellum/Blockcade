package us.blockcade.core.common.game;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BlockcadeTeam {

    private UUID uuid;
    private String name;
    private ChatColor spigotColor;
    private Color color;

    private Set<UUID> players = new HashSet<>();

    public BlockcadeTeam(String name) {
        this.name = name;
        this.spigotColor = GameManager.getTeamChatColor(name);
        this.color = GameManager.getTeamColor(name);

        this.uuid = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ChatColor getSpigotColor() {
        return spigotColor;
    }

    public Color getColor() {
        return color;
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setSpigotColor(ChatColor spigotColor) {
        this.spigotColor = spigotColor;
    }

    public boolean hasPlayer(Player player) {
        return players.contains(player.getUniqueId());
    }

    public void addPlayer(Player player) {
        if (!hasPlayer(player))
            players.add(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        if (hasPlayer(player))
            players.remove(player.getUniqueId());
    }

}
