package us.blockcade.core.common.chat;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FrozenChat {

    private static Set<UUID> frozenChats = new HashSet<>();
    public static Set<UUID> getFrozenChats() { return frozenChats; }

    public static void freezeChat(Player player) {
        frozenChats.add(player.getUniqueId());
    }

    public static void unfreezeChat(Player player) {
        frozenChats.remove(player.getUniqueId());
    }

    public static boolean isChatFrozen(Player player) {
        return frozenChats.contains(player.getUniqueId());
    }

}
