package us.blockcade.core.util.gui.book;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import us.blockcade.core.util.format.ChatUtil;

public class TextBuilder {

    private static final boolean canTranslateDirectly;

    static {
        boolean success = true;
        try {
            ChatColor.BLACK.asBungee();
        } catch (NoSuchMethodError e) {
            success = false;
        }
        canTranslateDirectly = success;
    }

    private String text = "";
    private ClickAction onClick = null;
    private HoverAction onHover = null;
    private ChatColor color = ChatColor.BLACK;

    private ChatColor[] style;

    public String getText() {
        return text;
    }

    public TextBuilder text(String text) {
        this.text = text;
        return this;
    }

    public TextBuilder onClick(ClickAction action) {
        onClick = action;
        return this;
    }

    public TextBuilder onHover(HoverAction action) {
        onHover = action;
        return this;
    }

    public ChatColor[] getStyle() {
        return style;
    }

    public void setStyle(ChatColor[] style) {
        this.style = style;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    /**
     * Sets the color of the text, or takes the previous color (if null is passed)
     * @param color the color of the text
     * @return the calling TextBuilder's instance
     */
    public TextBuilder color(ChatColor color) {
        if(color != null && !color.isColor())
            throw new IllegalArgumentException("Argument isn't a color!");
        this.color = color;
        return this;
    }

    /**
     * Sets the style of the text
     * @param style the style of the text
     * @return the calling TextBuilder's instance
     */
    public TextBuilder style(ChatColor... style) {
        for(ChatColor c : style)
            if(!c.isFormat())
                throw new IllegalArgumentException("Argument isn't a style!");
        this.style = style;
        return this;
    }

    /**
     * Creates the component representing the built text
     * @return the component representing the built text
     */
    public BaseComponent build() {
        TextComponent res = new TextComponent(text);
        if(onClick != null)
            res.setClickEvent(new ClickEvent(onClick.action(), onClick.value()));
        if(onHover != null)
            res.setHoverEvent(new HoverEvent(onHover.action(), onHover.value()));
        if(color != null) {
            if (canTranslateDirectly)
                res.setColor(color.asBungee());
            else
                res.setColor(net.md_5.bungee.api.ChatColor.getByChar(color.getChar()));
        }
        if(style != null) {
            for(ChatColor c : style) {
                switch (c) {
                    case MAGIC:
                        res.setObfuscated(true);
                        break;
                    case BOLD:
                        res.setBold(true);
                        break;
                    case STRIKETHROUGH:
                        res.setStrikethrough(true);
                        break;
                    case UNDERLINE:
                        res.setUnderlined(true);
                        break;
                    case ITALIC:
                        res.setItalic(true);
                        break;
                }
            }
        }
        return res;
    }

    /**
     * Creates a new TextBuilder with the parameter as his initial text
     * @param text initial text
     * @return a new TextBuilder with the parameter as his initial text
     */
    public static TextBuilder of(String text) {
        return new TextBuilder().text(text);
    }

    /**
     * A class representing the actions a client can do when a component is clicked
     */
    public interface ClickAction {
        /**
         * Get the Chat-Component action
         * @return the Chat-Component action
         */
        ClickEvent.Action action();

        /**
         * The value paired to the action
         * @return the value paired tot the action
         */
        String value();

        /**
         * Creates a command action: when the player clicks, the command passed as parameter gets executed with the clicker as sender
         * @param command the command to be executed
         * @return a new ClickAction
         */
        static ClickAction runCommand(String command) {
            return new SimpleClickAction(ClickEvent.Action.RUN_COMMAND, command);
        }

        /**
         * Creates a suggest_command action: when the player clicks, the book closes and the chat opens with the parameter written into it
         * @param command the command to be suggested
         * @return a new ClickAction
         */
        static ClickAction suggestCommand(String command) {
            return new SimpleClickAction(ClickEvent.Action.SUGGEST_COMMAND, command);
        }

        /**
         * Creates a open_utl action: when the player clicks the url passed as argument will open in the browser
         * @param url the url to be opened
         * @return a new ClickAction
         */
        static ClickAction openUrl(String url) {
            if(url.startsWith("http://") || url.startsWith("https://"))
                return new SimpleClickAction(ClickEvent.Action.OPEN_URL, url);
            else
                throw new IllegalArgumentException("Invalid url: \"" + url + "\", it should start with http:// or https://");
        }

        class SimpleClickAction implements ClickAction {

            private final ClickEvent.Action action;
            private final String value;

            public SimpleClickAction(ClickEvent.Action action, String value) {
                this.action = action;
                this.value = value;
            }

            public ClickEvent.Action action() {
                return action;
            }

            public String value() {
                return value;
            }
        }
    }

    /**
     * A class representing the actions a client can do when a component is hovered
     */
    public interface HoverAction {
        /**
         * Get the Chat-Component action
         * @return the Chat-Component action
         */
        HoverEvent.Action action();
        /**
         * The value paired to the action
         * @return the value paired tot the action
         */
        BaseComponent[] value();


        /**
         * Creates a show_text action: when the component is hovered the text used as parameter will be displayed
         * @param text the text to display
         * @return a new HoverAction instance
         */
        static HoverAction showText(BaseComponent... text) {
            return new SimpleHoverAction(HoverEvent.Action.SHOW_TEXT, text);
        }

        /**
         * Creates a show_text action: when the component is hovered the text used as parameter will be displayed
         * @param text the text to display
         * @return a new HoverAction instance
         */
        static HoverAction showText(String text) {
            return new SimpleHoverAction(HoverEvent.Action.SHOW_TEXT, new TextComponent(ChatUtil.format("&a" + text)));
        }

        /**
         * Creates a show_item action: when the component is hovered some item information will be displayed
         * @param item a component array representing item to display
         * @return a new HoverAction instance
         */
        static HoverAction showItem(BaseComponent... item) {
            return new SimpleHoverAction(HoverEvent.Action.SHOW_ITEM, item);
        }

        /**
         * Creates a show_item action: when the component is hovered some item information will be displayed
         * @param item the item to display
         * @return a new HoverAction instance
         */
        static HoverAction showItem(ItemStack item) {
            return new SimpleHoverAction(HoverEvent.Action.SHOW_ITEM, NmsBookHelper.itemToComponents(item));
        }

        class SimpleHoverAction implements HoverAction {
            private final HoverEvent.Action action;
            private final BaseComponent[] value;

            public HoverEvent.Action action() {
                return action;
            }

            public BaseComponent[] value() {
                return value;
            }

            public SimpleHoverAction(HoverEvent.Action action, BaseComponent... value) {
                this.action = action;
                this.value = value;
            }
        }
    }

}