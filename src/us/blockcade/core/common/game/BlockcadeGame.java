package us.blockcade.core.common.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import us.blockcade.core.util.math.Coordinate;

import java.util.*;

public class BlockcadeGame {

    private String name;
    private String[] description;

    private List<Listener> handlers = new ArrayList<>();
    private Set<String> worlds = new HashSet<>();
    private Coordinate lobbyLocation = Coordinate.fromLocation(Bukkit.getWorlds().get(0).getSpawnLocation());
    private Set<UUID> players = new HashSet<>();
    private Set<BlockcadeTeam> teams = new HashSet<>();

    private int minimumPlayers = 2;
    private int maximumPlayers = 16;

    private String instructions;

    public BlockcadeGame(String name, String[] description) {
        this.name = name;
        this.description = description;
        this.instructions = "How to Play: " + name;

        GameManager.registerGame(this);
    }

    public String getName() {
        return name;
    }

    public String[] getDescription() {
        return description;
    }

    public List<Listener> getHandlers() {
        return handlers;
    }

    public Coordinate getLobbyLocation() {
        return lobbyLocation;
    }

    public boolean hasPlayer(Player player) {
        return players.contains(player.getUniqueId());
    }

    public Set<String> getWorlds() {
        return worlds;
    }

    public Set<BlockcadeTeam> getTeams() {
        return teams;
    }

    public int getMinimumPlayers() { return minimumPlayers; }

    public int getMaximumPlayers() { return maximumPlayers; }

    public String getInstructions() { return instructions; }

    public void setName(String name) { this.name = name; }

    public void setDescription(String[] description) { this.description = description; }

    public void setMinimumPlayers(int minimumPlayers) {
        this.minimumPlayers = minimumPlayers;
    }

    public void setMaximumPlayers(int maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }

    public void setInstructions(String instructions) { this.instructions = instructions; }

    public void setLobbyLocation(Location lobby) {
        lobbyLocation = Coordinate.fromLocation(lobby);
    }

    public void registerHandler(Listener handler) {
        handlers.add(handler);
    }

    public void registerWorld(String world) {
        worlds.add(world);
    }

    public void registerTeam(BlockcadeTeam team) {
        teams.add(team);
    }

    public void sendToLobby(Player player) {
        player.teleport(getLobbyLocation().makeLocation(player.getWorld()));
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
