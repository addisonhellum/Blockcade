package us.blockcade.core.util.gui;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class TabTitleManager {

    public static void sendPlayerListTab(Player player, String header, String footer) {
        CraftPlayer craftplayer = (CraftPlayer)player;
        PlayerConnection connection =
                craftplayer.getHandle().playerConnection;
        IChatBaseComponent hj = IChatBaseComponent.ChatSerializer.a(ChatColor.translateAlternateColorCodes('&', "{\"text\": \"" + header + "\"}"));
        IChatBaseComponent fj = IChatBaseComponent.ChatSerializer.a(ChatColor.translateAlternateColorCodes('&', "{\"text\": \"" + footer + "\"}"));
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try
        {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, hj);
            headerField.setAccessible(!headerField.isAccessible());

            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, fj);
            footerField.setAccessible(!footerField.isAccessible());
        }
        catch (Exception localException) {}
        connection.sendPacket(packet);
    }

}