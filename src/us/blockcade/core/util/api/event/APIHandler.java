package us.blockcade.core.util.api.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import us.blockcade.core.util.api.event.server.ServerLoadEvent;
import us.blockcade.core.util.api.event.server.ServerUnloadEvent;

public class APIHandler implements Listener {

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        Plugin plugin = event.getPlugin();
        // Placeholder
    }

    @EventHandler
    public void onServerUnload(ServerUnloadEvent event) {
        Plugin plugin = event.getPlugin();
        // Placeholder
    }

}
