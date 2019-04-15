package us.blockcade.core.util.userdata.events;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.rank.Rank;

public class PlayerRankUpdateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private BPlayer bplayer;
    private CommandSender whoset;
    private Rank rankFrom;
    private Rank rankTo;
    private boolean cancelled = false;

    public PlayerRankUpdateEvent(BPlayer player, Rank rankFrom, Rank rankTo) {
        this.bplayer = player;
        this.whoset = Bukkit.getConsoleSender();
        this.rankFrom = rankFrom;
        this.rankTo = rankTo;
    }

    public PlayerRankUpdateEvent(BPlayer player, CommandSender setter, Rank rankFrom, Rank rankTo) {
        this.bplayer = player;
        this.whoset = setter;
        this.rankFrom = rankFrom;
        this.rankTo = rankTo;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public BPlayer getPlayer() {
        return bplayer;
    }

    public CommandSender getWhoSet() { return whoset; }

    public Rank fromRank() {
        return rankFrom;
    }

    public Rank toRank() { return rankTo; }

    public boolean isCancelled() { return cancelled; }

    public void setCancelled(boolean value) { cancelled = true; }

}
