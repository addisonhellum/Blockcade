package us.blockcade.core.util.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import us.blockcade.core.Main;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.sql.BlockcadeSql;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;
import us.blockcade.core.util.userdata.rank.Rank;
import us.blockcade.core.util.userdata.rank.RankManager;

import java.sql.*;
import java.util.*;

public class BungeeUtil implements PluginMessageListener {

    private static Map<String, String[]> onlinePlayers;
    private static List<String> servers;

    public void onPluginMessageReceived(final String channel, final Player player, final byte[] message) {
        if (!channel.equals("BungeeCord")) return;

        final ByteArrayDataInput in = ByteStreams.newDataInput(message);
        final String subchannel = in.readUTF();

        if (subchannel.equals("PlayerList")) {
            final String server = in.readUTF();
            final String[] playerList = in.readUTF().split(", ");

            if (BungeeUtil.onlinePlayers.containsKey(server))
                BungeeUtil.onlinePlayers.remove(server);

            BungeeUtil.onlinePlayers.put(server, playerList);
        }

        if (subchannel.equalsIgnoreCase("GetServers")) {
            final String[] serverList = in.readUTF().split(", ");
            final List<String> list = new ArrayList<String>();

            for (final String server2 : serverList)
                list.add(server2);

            BungeeUtil.servers = list;
        }
    }

    public void initialize() {
        final Plugin plugin = Main.getInstance();
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", (PluginMessageListener)new BungeeUtil());
    }

    public String[] getOnlinePlayers() {
        return BungeeUtil.onlinePlayers.get("ALL");
    }

    public String[] getOnlinePlayers(final String server) {
        return BungeeUtil.onlinePlayers.get(server);
    }

    public String getServerName(UUID uuid) {
        for (String servers : servers) {
            for (String players : getOnlinePlayers(servers)) {
                if (Bukkit.getOfflinePlayer(players).getUniqueId().equals(uuid)) {
                    return servers;
                }
            }
        } return null;
    }

    public void findOnlinePlayers(final Player anyPlayer) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF("ALL");
        anyPlayer.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
    }

    public void findConnectedServers(final Player anyPlayer) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServers");
        anyPlayer.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
    }

    public void sendPlayerToServer(final Player player, final String serverName) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        player.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
    }

    public void messagePlayer(final Player anyPlayer, final String name, final String message) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Message");
        out.writeUTF(name);
        out.writeUTF(message);
        anyPlayer.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
    }

    public void kickPlayer(final Player anyPlayer, final String name) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("KickPlayer");
        out.writeUTF(name);
        out.writeUTF("You were kicked from the server");
        anyPlayer.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
    }

    public void kickPlayer(final Player anyPlayer, final String name, final String message) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("KickPlayer");
        out.writeUTF(name);
        out.writeUTF(message);
        anyPlayer.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
    }

    static {
        BungeeUtil.onlinePlayers = new HashMap<String, String[]>();
        BungeeUtil.servers = new ArrayList<String>();
    }


    public static BungeeUtil getUtil() {
        return new BungeeUtil();
    }

    public Connection connection = BlockcadeSql.getConnection();

    public ArrayList<UUID> formatStringMembers(String string){
        ArrayList<UUID>id = new ArrayList<>();

        if (!string.contains(",")) {
            id.add(BlockcadeUsers.getBPlayer(Integer.valueOf(string)).getUuid());
            return id;
        }

        for (String uuid : string.split(",")) {
            int ids = Integer.valueOf(uuid);
            UUID uuids = BlockcadeUsers.getBPlayer(ids).getUuid();
            id.add(uuids);
        }

        return  id;
    }

    public HashSet<UUID> getIgnores(UUID uuid) {
        HashSet<UUID> uuids = new HashSet<>();
        final String query = "SELECT * FROM ignore_data where UUID='" + uuid.toString() + "'";

        try {
            final Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery(query);
            rs.next();
            if (rs.getString("ignored").contains(",")) {
                for (UUID uuidz : formatStringMembers(rs.getString("ignored"))) {
                    uuids.add(uuidz);
                }
            } else {
                uuids.add(BlockcadeUsers.getBPlayer(Integer.valueOf(rs.getString("ignored"))).getUuid());
            }

            return uuids;

        }
        catch (SQLException e) {
            e.printStackTrace();
            return uuids;
        }
    }

    public boolean isFriends(UUID uuid,UUID target) {
        return getFriends(uuid).contains(target);
    }

    public boolean isIgnored(UUID uuid,UUID target) {
        return getIgnores(uuid).contains(target);
    }
    public boolean isStaff(UUID uuid){
        return getAllStaff().contains(uuid) && BlockcadeUsers.getBPlayer(uuid).hasAccess(Rank.BUILDER);
    }

    public ArrayList<UUID> getAllStaff(){
        ArrayList<UUID> ranked = new ArrayList<>();
        final String query = "SELECT * FROM users";
        try {
            final Statement st = BlockcadeSql.getConnection().createStatement();
            final ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                if(RankManager.fromString(rs.getString("rank")).hasPermission(Rank.BUILDER))
                    ranked.add(UUID.fromString(rs.getString("uuid")));
            }
            return ranked;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<UUID>();
        }

    }

    public HashSet<UUID> getFriends(UUID uuid){
        HashSet<UUID> uuids = new HashSet<>();
        final String query = "SELECT * FROM friend_data where UUID='" + uuid.toString() + "'";

        try {
            final Statement st =connection.createStatement();
            final ResultSet rs = st.executeQuery(query);
            rs.next();
            if (rs.getString("friends").contains(",")) {
                for (UUID uuidz : formatStringMembers(rs.getString("friends"))) {
                    uuids.add(uuidz);
                }
            } else {
                uuids.add(BlockcadeUsers.getBPlayer(Integer.valueOf(rs.getString("friends"))).getUuid());
            }
            return uuids;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return uuids;
        }
    }

    public Status getStaffStatus(UUID uuid) {
        try {
            final PreparedStatement ps = connection.prepareStatement("SELECT * FROM staff_chat WHERE uuid=?");
            ps.setString(1, uuid.toString());
            final ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("toggled").equalsIgnoreCase("true")) {
                    return Status.CANT_SEE;
                } else return Status.CAN_SEE;
            }
        }
        catch (SQLException ex) {
            // Do nothing haha
        }

        return Status.CAN_SEE;
    }

    public void messageStaff(Player anyPlayer, String type, String message) {
        for (UUID uuid : getAllStaff()) {
            if (getStaffStatus(uuid).equals(BungeeUtil.Status.CAN_SEE)) {
                BPlayer bplayer = BlockcadeUsers.getBPlayer(uuid);
                if (!bplayer.hasAccess(Rank.BUILDER)) {
                    bplayer.spigot().sendMessage("no perms brah");
                }

                messagePlayer(anyPlayer, bplayer.getName(),
                        ChatUtil.format("&8[&6STAFF&8]: &8(&a" + type + "&8) &7" + message));
            }
        }
    }

    public enum Status{
        CAN_SEE,
        CANT_SEE;
    }

}
