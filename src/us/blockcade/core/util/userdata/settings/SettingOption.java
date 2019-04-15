package us.blockcade.core.util.userdata.settings;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.gui.ItemStackBuilder;
import us.blockcade.core.util.sql.BlockcadeSql;
import us.blockcade.core.util.userdata.BlockcadeUsers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.UUID;

public enum SettingOption {

    NIGHT_MODE("Night Mode", Material.WATCH, "Set the time of day you see when you join a server."),
    PRIVATE_MESSAGES("Private Messages", Material.STAINED_GLASS_PANE, "Toggle whether you allow other players to send you private messages."),
    FRIEND_REQUESTS("Friend Requests", Material.STAINED_GLASS_PANE, "Toggle whether you allow other players to send you a friend request."),
    PARTY_REQUESTS("Party Requests", Material.STAINED_GLASS_PANE, "Toggle whether you allow other players to send you a party invitation."),
    CLAN_REQUESTS("Clan Requests", Material.STAINED_GLASS_PANE, "Toggle whether you allow other players to send you a clan invitation."),
    DUEL_REQUESTS("Duel Requests", Material.IRON_SWORD, "Toggle whether you allow other players to request to duel you."),
    AUTO_JOIN("Auto-Join", Material.REDSTONE, "Toggle whether you will automatically queue for another game after you finish a match.");

    private String name;
    private Material material;
    private String description;

    SettingOption(String name, Material material, String description) {
        this.name = name;
        this.material = material;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public String getDescription() {
        return description;
    }

    public String getSqlName() {
        return this.name().toLowerCase();
    }

    public boolean getStatus(String player) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        UUID uuid = offlinePlayer.getUniqueId();

        if (!BlockcadeUsers.userHasRecord(uuid)) return false;
        String query = "select " + getSqlName() + " from player_settings where uuid='" + uuid.toString() + "'";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean getStatus(UUID uuid) {
        return getStatus(Bukkit.getOfflinePlayer(uuid).getName());
    }

    public boolean toggleStatus(String player) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        UUID uuid = offlinePlayer.getUniqueId();

        if (!BlockcadeUsers.userHasRecord(uuid)) return false;
        boolean status = getStatus(uuid);

        String query = "update player_settings set " + getSqlName() + "=" + !status + " where uuid='" + uuid.toString() + "'";

        try {
            Statement st = BlockcadeSql.getConnection().createStatement();
            st.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return !status;
    }

    public ItemStack getIcon(Player player) {
        String name = player.getName();
        String desc = ChatUtil.wordWrap(getDescription(), 40, Locale.US);

        ItemStack icon = new ItemStackBuilder(getMaterial()).withName("&a" + getName())
                .withLore(new String[] { "Status: &b" + getStatus(name), "", desc, "", "&eClick to toggle " + getName() })
                .build();

        return icon;
    }

}
