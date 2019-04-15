package us.blockcade.core.util.userdata;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import us.blockcade.core.Main;
import us.blockcade.core.util.gui.NametagUtil;
import us.blockcade.core.util.sql.BlockcadeSql;
import us.blockcade.core.util.userdata.rank.Rank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BlockcadeUsers implements Listener {

    private static Set<BPlayer> cachedBPlayers = new HashSet<>();

    public static boolean userHasRecord(UUID uuid) {
        try {
            String name = Bukkit.getOfflinePlayer(uuid).getName();
            Statement st = BlockcadeSql.getConnection().createStatement();
            ResultSet result = st.executeQuery("SELECT * FROM users WHERE username='" + name + "'");
            //st.close();

            if (result.next()) { return true; }
            else { return false; }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void registerUser(UUID uuid) {
        String name = Bukkit.getOfflinePlayer(uuid).getName();
        String rank = Rank.DEFAULT.getName();

        String ip = "";
        if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
            Player p = Bukkit.getPlayer(uuid);
            ip = p.getAddress().getHostString();
        }

        String query = "INSERT INTO users ("
                + " uuid, username, rank, coins, experience, ip ) VALUES ("
                + " '" + uuid + "', '" + name + "', '" + rank + "', " + 0 + ", " + 0 + ", '" + ip + "' )";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            st.execute(query);
            //st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createUsersTable() {
        // ID, UUID, Name, Rank, Coins, Experience, IP
        String query = "CREATE TABLE IF NOT EXISTS users ("
                + " id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                + " uuid VARCHAR(60) NOT NULL,"
                + " username VARCHAR(30) NOT NULL,"
                + " rank VARCHAR(20),"
                + " coins INTEGER,"
                + " experience INTEGER,"
                + " ip VARCHAR(35) )";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            st.execute(query);
            //st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static BPlayer getBPlayer(UUID uuid) {
        for (BPlayer b : cachedBPlayers) {
            if (b.getUuid().equals(uuid)) return b;
        }

        BPlayer generated = new BPlayer(uuid);
        cachedBPlayers.add(generated);
        return generated;
    }

    public static BPlayer getBPlayer(int id) {
        String query = "select * from users where id=" + id;

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);
            UUID uuid = UUID.fromString(rs.getString("uuid"));

            while (rs.next()) {
                return getBPlayer(uuid);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static BPlayer getBPlayer(Player player) {
        return getBPlayer(player.getUniqueId());
    }

    public static void updateBPlayerCache(UUID uuid) {
        new BukkitRunnable() {
            @Override
            public void run() {
                cachedBPlayers.remove(getBPlayer(uuid));

                if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
                    BPlayer bplayer = getBPlayer(uuid);
                    NametagUtil.setNametagColor(bplayer.spigot(), bplayer.getRank().getChatColor());
                }

                BPlayer generated = new BPlayer(uuid);
                cachedBPlayers.add(generated);
            }
        }.runTaskLater(Main.getInstance(), 20);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        updateBPlayerCache(uuid);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        cachedBPlayers.remove(getBPlayer(uuid));
    }

}
