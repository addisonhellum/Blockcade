package us.blockcade.core.commands.moderation;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.blockcade.core.Main;
import us.blockcade.core.common.punishment.Punishment;
import us.blockcade.core.common.punishment.PunishmentManager;
import us.blockcade.core.common.punishment.PunishmentPreset;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.gui.ItemStackBuilder;
import us.blockcade.core.util.gui.menu.GuiMenu;
import us.blockcade.core.util.math.TimeUtil;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;
import us.blockcade.core.util.userdata.rank.Rank;

public class MuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mute")) {
            if (s instanceof Player) {
                Player player = (Player) s;
                BPlayer bplayer = BlockcadeUsers.getBPlayer(player);

                if (!bplayer.getRank().hasPermission(Rank.MOD)) {
                    player.sendMessage(ChatUtil.format("&cYou are unauthorized to perform this command."));
                    return false;
                }
            }

            if (args.length == 0) {
                s.sendMessage(ChatUtil.format("&cPlease specify the username of the player you are trying to " +
                        "punish. You can use tab to auto-fill."));

            } else if (args.length == 1) {
                if (!(s instanceof Player)) {
                    s.sendMessage(ChatUtil.format("&cUsage: /punish [Username] [Type] <Reason>"));
                    return false;
                }
                Player player = (Player) s;

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                String name = target.getName();

                GuiMenu menu = new GuiMenu("Mute: " + name);

                menu.set(0, new ItemStackBuilder(Material.ARROW).withName("&bGo Back").build());
                menu.bindLeft(0, "punish " + name);

                menu.set(1, 3, new ItemStackBuilder(Material.BOOK).withName("&6Use Mute Preset").build());
                menu.bindLeft(12, "mute " + name + " preset");

                menu.set(1, 5, new ItemStackBuilder(Material.NAME_TAG).withName("&6Write Custom Mute").build());
                menu.bindLeft(14, "mute " + name + " custom");

                menu.freezeItems(true);
                menu.display(player);

            } else if (args.length == 2) {
                if (!(s instanceof Player)) {
                    s.sendMessage(ChatUtil.format("&cUsage: /punish [Username] [Type] <Reason>"));
                    return false;
                }
                Player player = (Player) s;

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                String name = target.getName();

                String function = args[1];

                if (function.equalsIgnoreCase("preset")) {
                    GuiMenu menu = new GuiMenu("Select a Mute Preset", 4);

                    menu.set(0, new ItemStackBuilder(Material.ARROW).withName("&bGo Back").build());
                    menu.bindLeft(0, "punish " + name);

                    menu.lockRow(1).lockColumn(1).lockColumn(9);

                    for (PunishmentPreset preset : PunishmentPreset.values()) {
                        if (preset.getPunishmentType().equals(Punishment.PunishmentType.MUTE)) {
                            ItemStack icon = new ItemStackBuilder(Material.INK_SACK)
                                    .withName("&6" + preset.getReason())
                                    .withLore("Duration: &b" + TimeUtil.asTimeString(preset.getDuration().asMillis())).withData(14);
                            menu.add(icon);
                            menu.bindLeft(icon, "mute " + name + " preset " + preset.getId());
                        }
                    }

                    menu.freezeItems(true);
                    menu.display(player);

                } else if (function.equalsIgnoreCase("custom")) {

                }
            } else if (args.length == 3) {
                if (!(s instanceof Player)) {
                    s.sendMessage(ChatUtil.format("&cUsage: /punish [Username] [Type] <Reason>"));
                    return false;
                }
                Player player = (Player) s;

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                BPlayer targetPlayer = BlockcadeUsers.getBPlayer(target.getUniqueId());

                String function = args[1];
                String param = args[2];

                if (function.equalsIgnoreCase("preset")) {
                    try {
                        int id = Integer.valueOf(param);
                        PunishmentPreset preset = PunishmentManager.getPreset(id);

                        Punishment punishment = new Punishment(target.getUniqueId(), player.getUniqueId(), preset);
                        punishment.execute();

                        String sender = "&c&lCONSOLE";
                        if (s instanceof Player) {
                            Player setterPlayer = (Player) s;

                            BPlayer bsetter = BlockcadeUsers.getBPlayer(setterPlayer);
                            sender = bsetter.getFormattedName();
                        }

                        Main.getBungeeUtil().messageStaff(player, "Punish", sender + " &7muted " +
                                targetPlayer.getFormattedName() + " &7for &b" + punishment.getReason());

                    } catch (Exception e) {
                        s.sendMessage(ChatUtil.format("&cIncorrect parameters given for preset. Use a number value."));
                    }
                }
            }
        }

        return false;
    }

}
