package us.blockcade.core.commands.administrative;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import us.blockcade.core.Main;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.format.ColorUtil;
import us.blockcade.core.util.gui.ItemStackBuilder;
import us.blockcade.core.util.gui.menu.GuiMenu;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;
import us.blockcade.core.util.userdata.events.PlayerRankUpdateEvent;
import us.blockcade.core.util.userdata.rank.Rank;
import us.blockcade.core.util.userdata.rank.RankManager;

import java.util.Arrays;

public class SetrankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setrank")) {
            if (s instanceof Player) {
                Player player = (Player) s;
                if (!BlockcadeUsers.getBPlayer(player).getRank().hasPermission(Rank.ADMIN)) {
                    player.sendMessage(ChatUtil.format("&cYou do not have the rank required for this!"));
                    return false;
                }
            }

            if (args.length == 0) {
                if (!(s instanceof Player)) {
                    s.sendMessage(ChatUtil.format("&cUsage: /setrank [Username] <Rank Title>"));
                    return false;
                }
                Player player = (Player) s;

                GuiMenu menu = new GuiMenu("Select a Player", 6);
                menu.lockFrom(0, 17);
                menu.set(4, new ItemStackBuilder(Material.WOOL).withName("&aSetting Rank of Player")
                .withLore("Select the player to set the rank of.").withData(14), true);

                menu.set(0, new ItemStackBuilder(Material.BARRIER).withName("&cCancel").build(), true);

                for (Player ap : Bukkit.getOnlinePlayers()) {
                    BPlayer bp = BlockcadeUsers.getBPlayer(ap);
                    if (bp.getRank().getId() < BlockcadeUsers.getBPlayer(player).getRank().getId()) {
                        ItemStack icon = BlockcadeUsers.getBPlayer(ap).getSkullItem();
                        menu.add(icon).bindLeft(icon, "setrank " + ap.getName());
                    }
                }

                menu.freezeItems(true);
                menu.display(player);

            } else if (args.length == 1) {
                if (!(s instanceof Player)) {
                    s.sendMessage(ChatUtil.format("&cUsage: /setrank [Username] <Rank Title>"));
                    return false;
                }
                Player player = (Player) s;

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                if (!BlockcadeUsers.userHasRecord(target.getUniqueId())) {
                    player.sendMessage(ChatUtil.format("&cThat player has not been registered in our system."));
                    return false;
                }

                BPlayer bp = BlockcadeUsers.getBPlayer(target.getUniqueId());

                ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                SkullMeta meta = (SkullMeta) skull.getItemMeta();
                meta.setOwner(target.getName());
                meta.setDisplayName(bp.getRank().getPrefix() + bp.getName());
                skull.setItemMeta(meta);

                GuiMenu menu = new GuiMenu("Select a Rank");
                menu.lockFrom(0, 17);
                menu.set(0, new ItemStackBuilder(Material.BARRIER).withName("&cCancel").build(), true);
                menu.set(4, skull, true);

                for (Rank rank : Lists.reverse(Arrays.asList(Rank.values()))) {
                    if (rank.getName() != "Owner") {
                        ItemStack icon = new ItemStackBuilder(Material.WOOL).withName(rank.getChatColor() + "" +
                                ChatColor.BOLD + rank.getName()).withAmount(rank.getId())
                                .withData(ColorUtil.getDataValue(rank.getColor()));
                        menu.add(icon);
                        menu.bindLeft(icon, "setrank " + target.getName() + " " + rank.getName());
                    }
                }

                menu.freezeItems(true);
                menu.display(player);

            } else if (args.length == 2) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                Rank rank;

                try {
                    rank = RankManager.fromString(args[1]);
                } catch (NullPointerException e) {
                    s.sendMessage(ChatUtil.format("&cThe rank \"" + args[0] + "\" does not exist!"));
                    return false;
                }

                if (!BlockcadeUsers.userHasRecord(target.getUniqueId())) {
                    s.sendMessage(ChatUtil.format("&cThat player has not been registered in our system."));
                    return false;
                }

                BPlayer bp = BlockcadeUsers.getBPlayer(target.getUniqueId());
                if (s instanceof Player) {
                    Player player = (Player) s;
                    if (bp.getRank().getId() > BlockcadeUsers.getBPlayer(player).getRank().getId()) {
                        player.sendMessage(ChatUtil.format("&cYou may not set the rank of a player whose rank is " +
                                "higher than your own! You must do this from Console."));
                        return false;
                    }
                }

                bp.setRank(rank);
                bp.updateCache();

                PlayerRankUpdateEvent rankUpdateEvent = new PlayerRankUpdateEvent(bp, s, bp.getRank(), rank);
                Main.getInstance().getServer().getPluginManager().callEvent(rankUpdateEvent);
            }
        }

        return false;
    }

}
