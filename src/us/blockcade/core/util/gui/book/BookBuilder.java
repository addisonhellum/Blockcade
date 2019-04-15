package us.blockcade.core.util.gui.book;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.gui.book.events.OpenCustomBookEvent;

import java.util.Arrays;
import java.util.List;

public class BookBuilder {

    private BookMeta meta;

    public BookBuilder() {
        meta = (BookMeta) new ItemStack(Material.WRITTEN_BOOK).getItemMeta();
        meta.setTitle("Untitled");
        meta.setAuthor("Blockcade");
    }

    public BookBuilder(BookMeta meta) {
        this.meta = meta;
    }

    public BookMeta getMeta() { return meta; }

    public BookBuilder title(String title) {
        title = ChatUtil.format(title);

        meta.setTitle(title);
        meta.setDisplayName(title);
        return this;
    }

    public BookBuilder author(String author) {
        meta.setAuthor(author);
        return this;
    }

    public BookBuilder withLore(String... lore) {
        meta.setLore(Arrays.asList(lore));
        return this;
    }

    public BookBuilder page(int index, String contents) {
        meta.setPage(index, contents);
        return this;
    }

    public BookBuilder pagesRaw(String... pages) {
        meta.setPages(pages);
        return this;
    }

    public BookBuilder pagesRaw(List<String> pages) {
        meta.setPages(pages);
        return this;
    }

    public BookBuilder pages(BaseComponent[]... pages) {
        NmsBookHelper.setPages(meta, pages);
        return this;
    }

    public BookBuilder pages(List<BaseComponent[]> pages) {
        NmsBookHelper.setPages(meta, pages.toArray(new BaseComponent[0][]));
        return this;
    }

    public ItemStack build() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        book.setItemMeta(getMeta());

        return book;
    }

    public void open(Player player) {
        OpenCustomBookEvent event = new OpenCustomBookEvent(player, build(), false);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        player.closeInventory();

        ItemStack hand = player.getItemInHand();

        player.setItemInHand(event.getBook());
        player.updateInventory();

        NmsBookHelper.openBook(player, event.getBook(), event.getHand() == OpenCustomBookEvent.Hand.OFF_HAND);

        player.setItemInHand(hand);
        player.updateInventory();
    }

}
