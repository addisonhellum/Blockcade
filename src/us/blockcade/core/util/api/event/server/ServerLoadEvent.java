package us.blockcade.core.util.api.event.server;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public class ServerLoadEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Plugin eventPlugin;
    private boolean cancelled = false;

    public ServerLoadEvent(Plugin plugin) {
        eventPlugin = plugin;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Plugin getPlugin() {
        return eventPlugin;
    }

    public boolean isCancelled() { return cancelled; }

    public void setCancelled(boolean value) { cancelled = true; }

}