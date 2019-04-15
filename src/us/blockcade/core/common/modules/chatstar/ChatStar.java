package us.blockcade.core.common.modules.chatstar;

import org.bukkit.ChatColor;

public enum ChatStar {

    GRAY("Gray", ChatColor.GRAY, 0),
    RED("Red", ChatColor.RED, 1),
    DARK_RED("Dark Red", ChatColor.DARK_RED, 2),
    BLUE("Blue", ChatColor.BLUE, 3),
    AQUA("Light Blue", ChatColor.AQUA, 4),
    DARK_BLUE("Dark Blue", ChatColor.DARK_BLUE, 5),
    PURPLE("Purple", ChatColor.DARK_PURPLE, 6),
    LIGHT_PURPLE("Light Purple", ChatColor.LIGHT_PURPLE, 7),
    YELLOW("Yellow", ChatColor.YELLOW, 8),
    GOLD("Gold", ChatColor.GOLD, 9),
    GREEN("Green", ChatColor.GREEN, 10),
    DARK_GREEN("Dark Green", ChatColor.DARK_GREEN, 11),
    CYAN("Cyan", ChatColor.DARK_AQUA, 12),
    WHITE("White", ChatColor.WHITE, 13),
    DARK_GRAY("Dark Gray", ChatColor.DARK_GRAY, 14),
    BLACK("Black", ChatColor.BLACK, 15);

    private String name;
    private ChatColor color;
    private int id;

    ChatStar(String name, ChatColor color, int id) {
        this.name = name;
        this.color = color;
        this.id = id;
    }

    public String getName() { return name; }

    public ChatColor getColor() { return color; }

    public int getId() { return id; }

    public String getText() {
        return getColor() + "◆";
    }

}
