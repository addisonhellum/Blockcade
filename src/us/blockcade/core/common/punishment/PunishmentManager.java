package us.blockcade.core.common.punishment;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.blockcade.core.util.math.TimeUtil;
import us.blockcade.core.util.sql.BlockcadeSql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class PunishmentManager implements Listener {

    private static Map<UUID, ArrayList<Punishment>> cachedPunishments = new HashMap<>();

    public static void createPunishmentTable() {
        String query = "CREATE TABLE IF NOT EXISTS punishments (" +
                " id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                "uuid VARCHAR(60) NOT NULL, " +
                "punishment_type VARCHAR(30), " +
                "issuer_uuid VARCHAR(60), " +
                "reason VARCHAR(100), " +
                "issued_at LONG, " +
                "expires_at LONG, " +
                "expired BOOLEAN )";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            st.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executePunishment(Punishment punishment) {
        UUID uuid = punishment.getPunished().getUuid();
        String type = punishment.getType().name();
        UUID issuer = punishment.getPunisher().getUuid();
        String reason = punishment.getReason();
        long issued = punishment.getWhenIssued();
        long expires = punishment.getWhenExpires();
        boolean expired = punishment.isExpired();

        String query = "INSERT INTO punishments ("
                + " uuid, punishment_type, issuer_uuid, reason, issued_at, expires_at, expired ) VALUES " +
                "( '" + uuid.toString() + "', '" + type + "', '" + issuer + "', '" + reason + "', " + issued + ", "
                + expires +  ", " + expired + " )";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            st.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Punishment> getPunishments(UUID uuid) {
        if (cachedPunishments.containsKey(uuid)) {
            return cachedPunishments.get(uuid);
        } else {
            String query = "select * from punishments where uuid='" + uuid + "'";

            try {
                Statement st = BlockcadeSql.getConnection().createStatement();
                ResultSet rs = st.executeQuery(query);

                ArrayList<Punishment> punishments = new ArrayList<>();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String reason = rs.getString("reason");
                    Punishment.PunishmentType type = Punishment.getType(rs.getString("punishment_type"));
                    UUID issuer = UUID.fromString(rs.getString("issuer_uuid"));
                    long issued = rs.getLong("issued_at");
                    long expires = rs.getLong("expires_at");
                    boolean expired = rs.getBoolean("expired");

                    Punishment punishment = new Punishment(id, uuid, type, issuer, reason, issued, expires, expired);
                    punishments.add(punishment);
                }

                return punishments;
            } catch (SQLException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
    }

    public static void expirePunishment(int id) {
        String query = "update punishments set expired=true where id=" + id;

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            st.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Punishment> getPunishmentHistory(UUID uuid, Punishment.PunishmentType type) {
        List<Punishment> history = new ArrayList<>();
        for (Punishment p : getPunishments(uuid))
            if (p.getType().equals(type)) history.add(p);
        return history;
    }

    public static PunishmentPreset getPreset(int id) {
        for (PunishmentPreset preset : PunishmentPreset.values())
            if (preset.getId() == id) return preset;
        return null;
    }

    public static List<Report> getReportsAgainst(UUID uuid) {
        String query = "select * from reports where uuid='" + uuid + "'";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);

            List<Report> reports = new ArrayList<>();

            while (rs.next()) {
                String against = rs.getString("uuid");
                String issuer = rs.getString("whoreported");
                String hack = rs.getString("hack");
                String date = TimeUtil.asTimeString(rs.getLong("date"));
                String reason = rs.getString("reason");

                Report report = new Report(against, issuer, hack, date, reason);
                reports.add(report);
            }

            return reports;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        List<Punishment> punishments = getPunishments(player.getUniqueId());

        if (punishments.size() == 0) return;

        for (Punishment p : punishments) {
            if (p.getTimeRemaining() <= 0 && !p.isExpired()) {
                expirePunishment(p.getId());
            }
        }
    }

}
