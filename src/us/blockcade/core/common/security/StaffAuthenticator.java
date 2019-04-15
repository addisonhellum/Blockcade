package us.blockcade.core.common.security;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import us.blockcade.core.Main;
import us.blockcade.core.common.chat.FrozenChat;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.sql.BlockcadeSql;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;
import us.blockcade.core.util.userdata.rank.Rank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StaffAuthenticator implements Listener {

    private static Set<UUID> authenticating = new HashSet<>();
    private static Set<UUID> signingup = new HashSet<>();

    public static void createAuthTable() {
        String query = "CREATE TABLE IF NOT EXISTS staff_auth ("
                + " id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                + " uuid VARCHAR(60) NOT NULL,"
                + " passcode INTEGER )";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            st.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasAuthRecord(UUID uuid) {
        try {
            String name = Bukkit.getOfflinePlayer(uuid).getName();
            Statement st = BlockcadeSql.getConnection().createStatement();
            ResultSet result = st.executeQuery("SELECT * FROM staff_auth WHERE uuid='" + uuid.toString() + "'");

            if (result.next()) { return true; }
            else { return false; }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void registerAuth(UUID uuid, int passcode) {
        String query = "INSERT INTO staff_auth ("
                + " uuid, passcode ) VALUES ( '" + uuid.toString() + "', " + passcode + " )";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            st.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean authenticate(UUID uuid, int passcode) {
        String query = "select * from staff_auth where uuid='" + uuid.toString() + "'";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                int storedPasscode = rs.getInt("passcode");
                if (passcode == storedPasscode) return true;
                else return false;
            }
            st.close();
        } catch (Exception e) {
            return false;
        } return false;
    }

    public static void updateAddress(UUID uuid, String address) {
        String query = "update users set ip='" + address + "' where uuid='" + uuid.toString() + "'";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            st.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String border = ChatColor.GOLD + "-----------------------------------------------------";

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        BPlayer bplayer = BlockcadeUsers.getBPlayer(player);

        String address = player.getAddress().getHostString();

        if (bplayer.getRank() == null) return;
        if (bplayer.getRank().hasPermission(Rank.BUILDER)) {
            if (hasAuthRecord(player.getUniqueId())) {
                if (!bplayer.getAddress().equalsIgnoreCase(address)) {
                    FrozenChat.freezeChat(player);
                    authenticating.add(player.getUniqueId());

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.sendMessage(" ");
                            player.sendMessage(border);
                            player.sendMessage(ChatUtil.format("&bYou are attempting to sign in from a location that you do not " +
                                    "typically sign in from. Please verify your identity with your Blockcade Staff PIN:"));
                            player.sendMessage(" ");
                        }
                    }.runTaskLater(Main.getInstance(), 10);
                }
            } else {
                FrozenChat.freezeChat(player);
                signingup.add(player.getUniqueId());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendMessage(" ");
                        player.sendMessage(border);
                        player.sendMessage(ChatUtil.format("&eYou have not created a Blockcade PIN yet. We use this " +
                                "PIN number to verify your identity if we suspect your account may have been " +
                                "compromised. Please enter your desired PIN now:"));
                        player.sendMessage(" ");
                    }
                }.runTaskLater(Main.getInstance(), 10);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (signingup.contains(player.getUniqueId())) {
            String input = event.getMessage();
            event.setCancelled(true);
            try {
                int passcode = Integer.valueOf(input);
                registerAuth(player.getUniqueId(), passcode);

                signingup.remove(player.getUniqueId());
                FrozenChat.unfreezeChat(player);

                player.sendMessage(border);
                player.sendMessage(ChatUtil.format("&aThank you for registering your Blockcade PIN. Don't forget " +
                        "this number! If you forget it you will need to contact staff to regain access to your " +
                        "account. Thank you."));
                player.sendMessage(" ");
                player.sendMessage(ChatUtil.format("&eYour PIN: &b" + passcode));

            } catch (Exception e) {
                player.sendMessage(border);
                player.sendMessage(ChatUtil.format("&cYour Blockcade PIN must be numerical:"));
                player.sendMessage(" ");
            }

        }
        if (authenticating.contains(player.getUniqueId())) {
            String input = event.getMessage();
            event.setCancelled(true);
            try {
                int passcode = Integer.valueOf(input);
                if (authenticate(player.getUniqueId(), passcode)) {
                    FrozenChat.unfreezeChat(player);
                    authenticating.remove(player.getUniqueId());

                    player.sendMessage(ChatUtil.format("&aThank you for authenticating. Welcome back!"));
                    player.sendMessage(" ");

                    updateAddress(player.getUniqueId(), player.getAddress().getHostString());

                } else {
                    player.kickPlayer(ChatUtil.format("&cWe could not confirm your identity. If this is a mistake, " +
                            "please contact one of the network administrators on our discord. http://blockcade.us/discord"));
                }

            } catch (Exception e) {
                player.sendMessage(border);
                player.sendMessage(ChatUtil.format("&cPlease enter your numerical Blockcade PIN:"));
                player.sendMessage(" ");
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (authenticating.contains(player.getUniqueId()) ||
                signingup.contains(player.getUniqueId())) {

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (authenticating.contains(player.getUniqueId()) ||
                signingup.contains(player.getUniqueId())) {

            player.teleport(event.getFrom().getBlock().getLocation());
        }
    }

}
