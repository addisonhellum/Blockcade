package us.blockcade.core.util.format;

import org.bukkit.Color;

public class ColorUtil {

    public static Color[] getColors() {
        return new Color[] {Color.RED, Color.BLUE, Color.GREEN, Color.LIME, Color.YELLOW, Color.ORANGE,
                Color.AQUA, Color.WHITE, Color.BLACK, Color.TEAL, Color.SILVER, Color.FUCHSIA,
                Color.OLIVE, Color.MAROON, Color.PURPLE, Color.NAVY, Color.GRAY};
    }

    public static byte getDataValue(Color color) {
        if (color.equals(Color.RED)) return (byte) 14;
        else if (color.equals(Color.WHITE)) return (byte) 0;
        else if (color.equals(Color.BLUE)) return (byte) 11;
        else if (color.equals(Color.GREEN)) return (byte) 13;
        else if (color.equals(Color.LIME)) return (byte) 5;
        else if (color.equals(Color.PURPLE)) return (byte) 10;
        else if (color.equals(Color.TEAL)) return (byte) 9;
        else if (color.equals(Color.YELLOW)) return (byte) 4;
        else if (color.equals(Color.AQUA)) return (byte) 3;
        else if (color.equals(Color.GRAY)) return (byte) 7;
        else if (color.equals(Color.BLACK)) return (byte) 15;
        else if (color.equals(Color.FUCHSIA)) return (byte) 2;
        else if (color.equals(Color.MAROON)) return (byte) 14;
        else if (color.equals(Color.NAVY)) return (byte) 11;
        else if (color.equals(Color.OLIVE)) return (byte) 12;
        else if (color.equals(Color.ORANGE)) return (byte) 1;
        else if (color.equals(Color.SILVER)) return (byte) 8;
        else return (byte) 8;
    }

    public static Color getColor(String name) {
        if (name.equalsIgnoreCase("red")) return Color.RED;
        if (name.equalsIgnoreCase("blue")) return Color.BLUE;
        if (name.equalsIgnoreCase("green")) return Color.GREEN;
        if (name.equalsIgnoreCase("pink")) return Color.FUCHSIA;
        if (name.equalsIgnoreCase("white")) return Color.WHITE;
        if (name.equalsIgnoreCase("black")) return Color.BLACK;
        if (name.equalsIgnoreCase("aqua")) return Color.AQUA;
        if (name.equalsIgnoreCase("cyan")) return Color.TEAL;
        if (name.equalsIgnoreCase("orange")) return Color.ORANGE;
        if (name.equalsIgnoreCase("gold")) return Color.ORANGE;
        if (name.equalsIgnoreCase("brown")) return Color.OLIVE;
        if (name.equalsIgnoreCase("lime")) return Color.LIME;
        if (name.equalsIgnoreCase("yellow")) return Color.YELLOW;
        if (name.equalsIgnoreCase("purple")) return Color.PURPLE;
        if (name.equalsIgnoreCase("teal")) return Color.TEAL;
        if (name.equalsIgnoreCase("olive")) return Color.OLIVE;
        if (name.equalsIgnoreCase("silver")) return Color.SILVER;
        if (name.equalsIgnoreCase("gray")) return Color.GRAY;
        if (name.equalsIgnoreCase("fuchsia")) return Color.FUCHSIA;
        if (name.equalsIgnoreCase("navy")) return Color.NAVY;
        if (name.equalsIgnoreCase("maroon")) return Color.MAROON;
        return Color.YELLOW;
    }

    public static String getNameByColor(Color color) {
        if (color.equals(Color.SILVER)) return "Silver";
        if (color.equals(Color.ORANGE)) return "Orange";
        if (color.equals(Color.OLIVE)) return "Olive";
        if (color.equals(Color.NAVY)) return "Navy";
        if (color.equals(Color.MAROON)) return "Maroon";
        if (color.equals(Color.FUCHSIA)) return "Fuchsia";
        if (color.equals(Color.BLACK)) return "Black";
        if (color.equals(Color.GRAY)) return "Gray";
        if (color.equals(Color.AQUA)) return "Aqua";
        if (color.equals(Color.YELLOW)) return "Yellow";
        if (color.equals(Color.TEAL)) return "Teal";
        if (color.equals(Color.PURPLE)) return "Purple";
        if (color.equals(Color.LIME)) return "Lime";
        if (color.equals(Color.GREEN)) return "Green";
        if (color.equals(Color.BLUE)) return "Blue";
        if (color.equals(Color.RED)) return "Red";
        if (color.equals(Color.WHITE)) return "White";
        return "Unknown";
    }

}
