package us.blockcade.core.util.userdata.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.blockcade.core.util.userdata.BPlayer;

public class PlayerLevelupEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private BPlayer bplayer;
    private int levelFrom;
    private int levelTo;
    private boolean cancelled = false;

    public PlayerLevelupEvent(BPlayer player, int levelFrom, int levelTo) {
        this.bplayer = player;
        this.levelFrom = levelFrom;
        this.levelTo = levelTo;
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

    public int fromLevel() {
        return levelFrom;
    }

    public int levelTo() {
        return levelTo;
    }

    public boolean isCancelled() { return cancelled; }

    public void setCancelled(boolean value) { cancelled = true; }

}
