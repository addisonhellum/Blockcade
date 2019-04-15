package us.blockcade.core.util.gui.book;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import us.blockcade.core.util.format.ChatUtil;

public class BookUtil {

    public static BookBuilder writtenBook() {
        return new BookBuilder();
    }

    public static BookBuilder getBookBuilder(ItemStack book) {
        ItemMeta meta = book.getItemMeta();
        if (!(meta instanceof BookMeta))
            throw new IllegalArgumentException("Argument isn't a book!");

        return new BookBuilder((BookMeta) meta);
    }

    public static void openBook(Player player, ItemStack book) {
        getBookBuilder(book).open(player);
    }

    // TESTING & PRESENTATION

    private static ItemStack createPresentationBook(Player p) {
        return BookUtil.writtenBook()
                .author("Blockcade")
                .title("Cage Matches Info.")
                .pages(
                        new PageBuilder()
                                .add(TextBuilder.of("CAGE MATCHES").color(ChatColor.DARK_BLUE).style(ChatColor.BOLD).build())
                                .newLine()
                                .add(
                                        TextBuilder.of("Click the links below for more information:")
                                                .color(ChatColor.BLACK).build()
                                )
                                .newLine().newLine()
                                .add(
                                        TextBuilder.of(" ➤ ")
                                                .color(ChatColor.DARK_GRAY).build()
                                )
                                .add(
                                        TextBuilder.of("[HOW TO PLAY]")
                                                .color(ChatColor.DARK_GREEN)
                                                .onClick(TextBuilder.ClickAction.runCommand("/howtoplay"))
                                                .onHover(TextBuilder.HoverAction.showText("Click to view How to Play"))
                                                .build()
                                )
                                .newLine().newLine()
                                .add(
                                        TextBuilder.of(" ➤ ")
                                                .color(ChatColor.DARK_GRAY).build()
                                )
                                .add(
                                        TextBuilder.of("[LEADERBOARD]")
                                                .color(ChatColor.GOLD)
                                                .onClick(TextBuilder.ClickAction.runCommand("/leaderboard"))
                                                .onHover(TextBuilder.HoverAction.showText("Click to view Leaderboard"))
                                                .build()
                                )
                                .newLine().newLine()
                                .add(
                                        TextBuilder.of(" ➤ ")
                                                .color(ChatColor.DARK_GRAY).build()
                                )
                                .add(
                                        TextBuilder.of("[UNLOCKABLES]")
                                                .color(ChatColor.DARK_PURPLE)
                                                .onClick(TextBuilder.ClickAction.runCommand("/unlockables"))
                                                .onHover(TextBuilder.HoverAction.showText("Click to view Unlockables"))
                                                .build()
                                )
                                .newLine().newLine()
                                .add(
                                        TextBuilder.of(" ➤ ")
                                                .color(ChatColor.DARK_GRAY).build()
                                )
                                .add(
                                        TextBuilder.of("[GAME SETTINGS]")
                                                .color(ChatColor.DARK_AQUA)
                                                .onClick(TextBuilder.ClickAction.runCommand("/gamesettings"))
                                                .onHover(TextBuilder.HoverAction.showText("Click to view Game Settings"))
                                                .build()
                                )
                                .build()
                )
                .build();
    }

    public static void openPresentationBook(Player player) {
        BookUtil.openBook(player, createPresentationBook(player));
    }

}
