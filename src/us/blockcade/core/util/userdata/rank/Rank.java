package us.blockcade.core.util.userdata.rank;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.List;

public enum Rank {

    OWNER(9, "Owner", ChatColor.DARK_RED, ChatColor.AQUA, Color.RED),
    ADMIN(8, "Administrator", ChatColor.RED, ChatColor.AQUA, Color.RED),
    SR_MOD(7, "SrModerator", ChatColor.GREEN, ChatColor.WHITE, Color.LIME),
    MOD(6, "Moderator", ChatColor.DARK_GREEN, ChatColor.WHITE, Color.GREEN),
    BUILDER(5, "Builder", ChatColor.BLUE, ChatColor.WHITE, Color.BLUE),
    VIP(4, "VIP", ChatColor.DARK_PURPLE, ChatColor.WHITE, Color.PURPLE),
    GEM(3, "Gem", ChatColor.DARK_AQUA, ChatColor.WHITE, Color.TEAL),
    GOLD(2, "Gold", ChatColor.GOLD, ChatColor.WHITE, Color.ORANGE),
    STEEL(1, "Steel", ChatColor.WHITE, ChatColor.WHITE, Color.WHITE),
    DEFAULT(0, "Member", ChatColor.YELLOW, ChatColor.GRAY, Color.YELLOW);

    private int rankId;
    private String rankName;
    private ChatColor rankChatColor;
    private ChatColor rankTextColor;
    private Color rankColor;

    private Rank(int id, String name, ChatColor rankColor, ChatColor textColor, Color color) {
        this.rankId = id;
        this.rankName = name;
        this.rankChatColor = rankColor;
        this.rankTextColor = textColor;
        this.rankColor = color;
    }

    public int getId() { return this.rankId; }
    public String getName() { return this.rankName; }
    public ChatColor getChatColor() { return this.rankChatColor; }
    public ChatColor getTextColor() { return this.rankTextColor; }
    public Color getColor() { return this.rankColor; }
    
    public String getPrefix() {
        String shortenedName = getName();
        if (getName().equalsIgnoreCase("administrator")) shortenedName = "Admin";
        if (getName().equalsIgnoreCase("srmoderator")) shortenedName = "SrMod";
        if (getName().equalsIgnoreCase("moderator")) shortenedName = "Mod";

        if (getId() != 0) {
            return getChatColor() + "" + ChatColor.BOLD + shortenedName + getChatColor() + "" + getChatColor();
        } else {
            return getChatColor() + "";
        }
    }

    public boolean hasPermission(Rank rank) {
        return getId() >= rank.getId();
    }

}
