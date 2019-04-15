package us.blockcade.core.util.userdata;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import us.blockcade.core.Main;
import us.blockcade.core.common.modules.chatstar.ChatStar;
import us.blockcade.core.common.modules.chatstar.ChatStarManager;
import us.blockcade.core.common.punishment.Punishment;
import us.blockcade.core.common.punishment.PunishmentManager;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.gui.NametagUtil;
import us.blockcade.core.util.sql.BlockcadeSql;
import us.blockcade.core.util.userdata.events.PlayerLevelupEvent;
import us.blockcade.core.util.userdata.rank.Rank;
import us.blockcade.core.util.userdata.rank.RankManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class BPlayer {

    private Player player;

    private int userId;
    private String username;
    private String rankTitle;
    private UUID uuid;
    private int coins;
    private int experience;
    private String address;

    public BPlayer(UUID uuid) {
        this.uuid = uuid;
        if (Bukkit.getOfflinePlayer(uuid).isOnline())
            player = Bukkit.getPlayer(uuid);

        String query = "select id,username,rank,coins,experience,ip from users "
                + "where uuid='" + uuid.toString() + "'";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                this.userId = rs.getInt("id");
                this.username = rs.getString("username");
                this.rankTitle = rs.getString("rank");
                this.coins = rs.getInt("coins");
                this.experience = rs.getInt("experience");
                this.address = rs.getString("ip");
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Player spigot() { return player; }

    public int getUserId() { return userId; }
    public UUID getUuid() { return uuid; }
    public String getName() { return username; }
    public Rank getRank() { return RankManager.fromString(rankTitle); }
    public int getCoins() { return coins; }
    public int getExperience() { return experience; }
    public String getAddress() { return address; }

    public void updateCache() {
        if (Bukkit.getOfflinePlayer(uuid).isOnline())
            player = Bukkit.getPlayer(uuid);

        String query = "select id,username,rank,coins,experience,ip from users "
                + "where uuid='" + uuid.toString() + "'";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                this.userId = rs.getInt("id");
                this.username = rs.getString("username");
                this.rankTitle = rs.getString("rank");
                this.coins = rs.getInt("coins");
                this.experience = rs.getInt("experience");
                this.address = rs.getString("ip");
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        NametagUtil.setNametagColor(spigot(), getRank().getChatColor());
    }

    public ChatStar getChatStar() {
        return ChatStarManager.getChatStar(getUuid());
    }

    public String getFormattedName() {
        return getRank().getChatColor() + getName();
    }

    public int getLevel() {
        return Math.floorDiv(getExperience(), 2500) + 1;
    }

    public float getLevelProgress() {
        int experienceGained = getExperience() % 2500;
        return experienceGained / 2500F;
    }

    public boolean hasAccess(Rank rank) {
        return getRank().hasPermission(rank);
    }

    public void setRank(Rank rank) {
        String query = "UPDATE users SET rank='" + rank.getName() + "' WHERE uuid='" + getUuid() + "'";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            st.execute(query);

            this.rankTitle = rank.getName();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (Bukkit.getOfflinePlayer(getUuid()).isOnline()) {
            spigot().playSound(spigot().getLocation(), Sound.LEVEL_UP, 8, 8);
            spigot().sendMessage(ChatUtil.format("&aYour rank has been updated!"));
        }

        BlockcadeUsers.updateBPlayerCache(getUuid());
    }

    public void setCoins(int amount) {
        String query = "UPDATE users SET coins=" + amount + " WHERE uuid='" + getUuid() + "'";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            st.execute(query);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementCoins(int amount) {
        setCoins(getCoins() + amount);
    }

    public void decrementCoins(int amount) {
        setCoins(getCoins() - amount);
    }

    public void setExperience(int value) {
        String query = "UPDATE users SET experience=" + value + " WHERE uuid='" + getUuid() + "'";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            st.execute(query);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void giveExperience(int value) {
        int experienceRemaining = 2500 - (getExperience() % 2500);
        if (value > 0 && value >= experienceRemaining) {
            PlayerLevelupEvent levelEvent = new PlayerLevelupEvent(this, getLevel(), getLevel() + 1);
            Main.getInstance().getServer().getPluginManager().callEvent(levelEvent);

            if (levelEvent.isCancelled()) return;
        }

        setExperience(getExperience() + value);
    }

    public ItemStack getSkullItem() {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        meta.setOwner(getName());
        meta.setDisplayName(getRank().getPrefix() + getName());

        skull.setItemMeta(meta);
        return skull;
    }

    public Punishment isBanned() {
        for (Punishment p : PunishmentManager.getPunishments(getUuid()))
            if (p.getType().equals(Punishment.PunishmentType.BAN) && !p.isExpired()) return p;
        return null;
    }

    public Punishment isMuted() {
        for (Punishment p : PunishmentManager.getPunishments(getUuid()))
            if (p.getType().equals(Punishment.PunishmentType.MUTE) && !p.isExpired()) return p;
        return null;
    }

}
