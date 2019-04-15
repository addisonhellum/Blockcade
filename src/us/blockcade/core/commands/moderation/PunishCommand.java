package us.blockcade.core.commands.moderation;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.blockcade.core.common.punishment.Punishment;
import us.blockcade.core.common.punishment.PunishmentManager;
import us.blockcade.core.common.punishment.Report;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.gui.ItemStackBuilder;
import us.blockcade.core.util.gui.menu.GuiMenu;
import us.blockcade.core.util.math.TimeUtil;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;
import us.blockcade.core.util.userdata.rank.Rank;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PunishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("punish")) {
            if (s instanceof Player) {
                Player player = (Player) s;
                if (!BlockcadeUsers.getBPlayer(player).getRank().hasPermission(Rank.MOD)) {
                    player.sendMessage(ChatUtil.format("&cYou do not have the rank required for this!"));
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

                GuiMenu menu = new GuiMenu("Punishment: " + name, 4);

                menu.set(1, 1, new ItemStackBuilder(Material.INK_SACK)
                        .withName("&6Mute: " + name).withData(14))
                    .bindLeft(10, "mute " + name);
                menu.set(1, 3, new ItemStackBuilder(Material.INK_SACK)
                        .withName("&cKick: " + name).withData(1))
                        .bindLeft(12, "kick " + name);
                menu.set(1, 5, new ItemStackBuilder(Material.INK_SACK)
                        .withName("&3Ban: " + name).withData(6))
                        .bindLeft(14, "ban " + name);
                menu.set(2, 1, new ItemStackBuilder(Material.BOOK)
                        .withName("&6Mute History: " + name).build())
                        .bindLeft(19, "punish " + name + " mute_history");
                menu.set(2, 3, new ItemStackBuilder(Material.BOOK)
                        .withName("&cKick History: " + name).build())
                        .bindLeft(21, "punish " + name + " kick_history");
                menu.set(2, 5, new ItemStackBuilder(Material.BOOK)
                        .withName("&3Ban History: " + name).build())
                        .bindLeft(23, "punish " + name + " ban_history");
                menu.set(1, 7, new ItemStackBuilder(Material.PAPER)
                        .withName("&bView Reports Against " + name).build())
                        .bindLeft(16, "punish " + name + " report_history");

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
                GuiMenu menu = new GuiMenu("Punishment History: " + name, 4);

                menu.set(0, new ItemStackBuilder(Material.ARROW).withName("&bGo Back").build());
                menu.bindLeft(0, "punish " + name);

                menu.lockRow(1).lockColumn(1).lockColumn(8);

                if (function.equalsIgnoreCase("mute_history")) {
                    List<Punishment> punishments = PunishmentManager.getPunishmentHistory(target.getUniqueId(), Punishment.PunishmentType.MUTE);
                    if (punishments.size() == 0)
                        menu.set(13, new ItemStackBuilder(Material.STAINED_GLASS_PANE).withName("&aSqueaky Clean!")
                                .withLore(new String[] { "This player has not received", "any mutes." }).withData(5));

                    for (Punishment p : punishments) {
                        BPlayer punisher = p.getPunisher();

                        Date issued = new Date(p.getWhenIssued());
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US);

                        String[] lore;
                        if (p.isExpired()) {
                            lore = new String[] {
                                    "Punishment ID: " + p.getId(), "",
                                    "Muted by: " + punisher.getRank().getChatColor() + punisher.getName(),
                                    "Issued: &b" + sdf.format(issued), "", "&cPunishment Expired"
                            };
                        } else {
                            lore = new String[] {
                                    "Punishment ID: " + p.getId(), "",
                                    "Muted by: " + punisher.getRank().getChatColor() + punisher.getName(),
                                    "Issued: &b" + sdf.format(issued), "", "Time Remaining: &a" +
                                    TimeUtil.asTimeString(p.getTimeRemaining()), "", "&cRight-Click this record to manually",
                                    "&cexpire the punishment."
                            };
                        }

                        ItemStack icon = new ItemStackBuilder(Material.INK_SACK).withName("&6" + p.getReason()).withLore(lore).withData(14);
                        menu.add(icon).bindRight(icon, "punish " + name + " expire_mute " + p.getId());
                    }

                } else if (function.equalsIgnoreCase("kick_history")) {
                    List<Punishment> punishments = PunishmentManager.getPunishmentHistory(target.getUniqueId(), Punishment.PunishmentType.KICK);
                    if (punishments.size() == 0)
                        menu.set(13, new ItemStackBuilder(Material.STAINED_GLASS_PANE).withName("&aSqueaky Clean!")
                        .withLore(new String[] { "This player has not received", "any kicks." }).withData(5));

                    for (Punishment p : punishments) {
                        BPlayer punisher = p.getPunisher();

                        Date issued = new Date(p.getWhenIssued());
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US);

                        menu.add(new ItemStackBuilder(Material.INK_SACK)
                                .withName("&c" + p.getReason()).withLore(new String[] {
                                        "Punishment ID: " + p.getId(), "",
                                        "Kicked by: " + punisher.getRank().getChatColor() + punisher.getName(),
                                        "Issued: &b" + sdf.format(issued)
                                }).withData(1));
                    }

                } else if (function.equalsIgnoreCase("ban_history")) {
                    List<Punishment> punishments = PunishmentManager.getPunishmentHistory(target.getUniqueId(), Punishment.PunishmentType.BAN);
                    if (punishments.size() == 0)
                        menu.set(13, new ItemStackBuilder(Material.STAINED_GLASS_PANE).withName("&aSqueaky Clean!")
                                .withLore(new String[] { "This player has not received", "any bans." }).withData(5));

                    for (Punishment p : punishments) {
                        BPlayer punisher = p.getPunisher();

                        Date issued = new Date(p.getWhenIssued());
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US);

                        String[] lore;
                        if (p.isExpired()) {
                            lore = new String[] {
                                    "Punishment ID: " + p.getId(), "",
                                    "Banned by: " + punisher.getRank().getChatColor() + punisher.getName(),
                                    "Issued: &b" + sdf.format(issued), "", "&cPunishment Expired"
                            };
                        } else {
                            lore = new String[] {
                                    "Punishment ID: " + p.getId(), "",
                                    "Banned by: " + punisher.getRank().getChatColor() + punisher.getName(),
                                    "Issued: &b" + sdf.format(issued), "", "Time Remaining: &a" +
                                    TimeUtil.asTimeString(p.getTimeRemaining()), "", "&cRight-Click this record to manually",
                                    "&cexpire the punishment."
                            };
                        }

                        ItemStack icon = new ItemStackBuilder(Material.INK_SACK).withName("&3" + p.getReason()).withLore(lore).withData(6);
                        menu.add(icon).bindRight(icon, "punish " + name + " expire_ban " + p.getId());
                    }

                } else if (function.equalsIgnoreCase("report_history")) {
                    List<Report> reports = PunishmentManager.getReportsAgainst(target.getUniqueId());
                    if (reports.size() == 0)
                        menu.set(13, new ItemStackBuilder(Material.STAINED_GLASS_PANE).withName("&aSqueaky Clean!")
                                .withLore(new String[] { "This player has not received", "any reports." }).withData(5));

                    for (Report r : reports) {
                        BPlayer reporter = BlockcadeUsers.getBPlayer(r.getWhoFiled());
                        String hack = ChatUtil.toTitleCase(r.getHack().replace("_", " ").toLowerCase());

                        ItemStack icon = new ItemStackBuilder(Material.PAPER).withName("&b" + hack)
                                .withLore(new String[] {
                                        "Original Report: ", "&e" + r.getReason(), "",
                                        "Reported by: " + reporter.getRank().getChatColor() + reporter.getName(),
                                        "Issued: &a" + r.getDate()
                                }).build();
                        menu.add(icon);
                    }
                }

                menu.freezeItems(true);
                menu.display(player);

            } else if (args.length == 3) {
                if (!(s instanceof Player)) {
                    s.sendMessage(ChatUtil.format("&cUsage: /punish [Username] [Type] <Reason>"));
                    return false;
                }
                Player player = (Player) s;

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                String name = target.getName();

                String function = args[1];
                String param = args[2];

                if (function.equalsIgnoreCase("expire_ban")) {
                    try {
                        int id = Integer.valueOf(param);
                        PunishmentManager.expirePunishment(id);

                        player.sendMessage(ChatUtil.format("&aSuccess! Ban #" + id + " against " + name + " was expired."));
                        player.performCommand("punish " + name + " ban_history");
                    } catch (Exception e) {
                        player.sendMessage(ChatUtil.format("&cIncorrect parameters given for preset. Use a number value."));
                    }

                } else if (function.equalsIgnoreCase("expire_mute")) {
                    try {
                        int id = Integer.valueOf(param);
                        PunishmentManager.expirePunishment(id);

                        player.sendMessage(ChatUtil.format("&aSuccess! Mute #" + id + " against " + name + " was expired."));
                        player.performCommand("punish " + name + " mute_history");
                    } catch (Exception e) {
                        player.sendMessage(ChatUtil.format("&cIncorrect parameters given for preset. Use a number value."));
                    }
                }
            }
        }

        return false;
    }

}
