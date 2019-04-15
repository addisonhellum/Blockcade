package us.blockcade.core.util.gui.book.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class OpenCustomBookEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    private final Player player;

    public Player getPlayer() {
        return player;
    }

    /**
     * The hand used to open the book (the previous item will be restored after the opening)
     */
    private Hand hand;

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    /**
     * The actual book to be opened
     */
    private ItemStack book;

    public ItemStack getBook() {
        return book;
    }

    public void setBook(ItemStack book) {
        this.book = book;
    }

    public OpenCustomBookEvent(Player player, ItemStack book, boolean offHand) {
        this.player = player;
        this.book = book;
        this.hand = offHand ? Hand.OFF_HAND : Hand.MAIN_HAND;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum Hand {
        MAIN_HAND, OFF_HAND
    }

}
