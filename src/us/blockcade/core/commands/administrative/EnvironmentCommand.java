package us.blockcade.core.commands.administrative;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import us.blockcade.core.Main;
import us.blockcade.core.common.environment.EnvironmentHandler;
import us.blockcade.core.util.format.ChatUtil;
import us.blockcade.core.util.gui.ItemStackBuilder;
import us.blockcade.core.util.gui.menu.GuiMenu;
import us.blockcade.core.util.userdata.BPlayer;
import us.blockcade.core.util.userdata.BlockcadeUsers;
import us.blockcade.core.util.userdata.rank.Rank;

public class EnvironmentCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("environment")) {
            if (s instanceof Player) {
                Player player = (Player) s;
                BPlayer bplayer = BlockcadeUsers.getBPlayer(player);

                if (!bplayer.getRank().hasPermission(Rank.ADMIN)) {
                    player.sendMessage(ChatUtil.format("&cYou do not have the rank required for this!"));
                    return false;
                }
            }

            if (args.length == 0) {
                if (!(s instanceof Player)) {
                    s.sendMessage(ChatUtil.format("Usage: /environment [Attribute Name]"));
                    return false;
                }
                Player player = (Player) s;

                GuiMenu menu = new GuiMenu("Environmental Controls", 6);
                menu.set(0, new ItemStackBuilder(Material.BARRIER).withName("&cClose").build());
                menu.set(4, new ItemStackBuilder(Material.PAPER).withName("&aManage Environmental Settings")
                .withLore(new String[] { "Click on an icon to toggle its status." }).build());

                ItemStack weather = new ItemStackBuilder(Material.WATCH).withName("&bCan the weather change?")
                        .withLore(new String[] { "Status: &e" + EnvironmentHandler.hasWeather() }).build();
                ItemStack hunger = new ItemStackBuilder(Material.COOKED_BEEF).withName("&bCan players get hungry?")
                        .withLore(new String[] { "Status: &e" + EnvironmentHandler.hasHunger() }).build();
                ItemStack physics = new ItemStackBuilder(Material.SAND).withName("&bDo block physics apply?")
                        .withLore(new String[] { "Status: &e" + EnvironmentHandler.canDoPhysics() }).build();
                ItemStack explode = new ItemStackBuilder(Material.TNT).withName("&bCan things go boom?")
                        .withLore(new String[] { "Status: &e" + EnvironmentHandler.hasExplosions() }).build();
                ItemStack mobs = new ItemStackBuilder(Material.MOB_SPAWNER).withName("&bCan mobs spawn?")
                        .withLore(new String[] { "Status: &e" + EnvironmentHandler.hasMobs() }).build();
                ItemStack fall = new ItemStackBuilder(Material.GOLD_BOOTS).withName("&bCan players take fall damage?")
                        .withLore(new String[] { "Status: &e" + EnvironmentHandler.hasFallDamage() }).build();
                ItemStack damage = new ItemStackBuilder(Material.DIAMOND_CHESTPLATE).withName("&bCan players take any damage?")
                        .withLore(new String[] { "Status: &e" + EnvironmentHandler.hasAllDamage() }).build();
                ItemStack pvp = new ItemStackBuilder(Material.IRON_SWORD).withName("&bCan players smack other players?")
                        .withLore(new String[] { "Status: &e" + EnvironmentHandler.hasPvP() }).build();
                ItemStack place = new ItemStackBuilder(Material.GRASS).withName("&bCan players place blocks?")
                        .withLore(new String[] { "Status: &e" + EnvironmentHandler.canPlaceBlocks() }).build();
                ItemStack broke = new ItemStackBuilder(Material.DIAMOND_PICKAXE).withName("&bCan players break blocks?")
                        .withLore(new String[] { "Status: &e" + EnvironmentHandler.canBreakBlocks() }).build();
                ItemStack decay = new ItemStackBuilder(Material.ICE).withName("&bCan blocks decay?")
                        .withLore(new String[] { "Status: &e" + EnvironmentHandler.canDecay() }).build();

                menu.set(2, 1, weather);
                menu.set(2, 2, hunger);
                menu.set(2, 4, physics);
                menu.set(2, 5, explode);
                menu.set(2, 7, mobs);
                menu.set(4, 1, fall);
                menu.set(4, 2, damage);
                menu.set(4, 3, pvp);
                menu.set(4, 5, place);
                menu.set(4, 6, broke);
                menu.set(4, 7, decay);

                menu.bindLeft(weather, "environment weather");
                menu.bindLeft(hunger, "environment hunger");
                menu.bindLeft(physics, "environment physics");
                menu.bindLeft(explode, "environment explode");
                menu.bindLeft(mobs, "environment mobs");
                menu.bindLeft(fall, "environment fall");
                menu.bindLeft(damage, "environment damage");
                menu.bindLeft(pvp, "environment pvp");
                menu.bindLeft(place, "environment place");
                menu.bindLeft(broke, "environment break");
                menu.bindLeft(decay, "environment decay");

                menu.freezeItems(true);
                menu.display(player);

            } else if (args.length == 1) {
                String toggle = args[0];

                if (toggle.equalsIgnoreCase("weather"))
                    EnvironmentHandler.setWeather(!EnvironmentHandler.hasWeather());
                if (toggle.equalsIgnoreCase("decay"))
                    EnvironmentHandler.setDecay(!EnvironmentHandler.canDecay());
                if (toggle.equalsIgnoreCase("physics"))
                    EnvironmentHandler.setPhysics(!EnvironmentHandler.canDoPhysics());
                if (toggle.equalsIgnoreCase("mobs"))
                    EnvironmentHandler.setSpawnMobs(!EnvironmentHandler.hasMobs());
                if (toggle.equalsIgnoreCase("fall"))
                    EnvironmentHandler.setFallDamage(!EnvironmentHandler.hasFallDamage());
                if (toggle.equalsIgnoreCase("hunger"))
                    EnvironmentHandler.setHunger(!EnvironmentHandler.hasHunger());
                if (toggle.equalsIgnoreCase("damage"))
                    EnvironmentHandler.setAllDamage(!EnvironmentHandler.hasAllDamage());
                if (toggle.equalsIgnoreCase("pvp"))
                    EnvironmentHandler.setPvP(!EnvironmentHandler.hasPvP());
                if (toggle.equalsIgnoreCase("place"))
                    EnvironmentHandler.setPlaceBlocks(!EnvironmentHandler.canPlaceBlocks());
                if (toggle.equalsIgnoreCase("break"))
                    EnvironmentHandler.setBreakBlocks(!EnvironmentHandler.canBreakBlocks());
                if (toggle.equalsIgnoreCase("explode"))
                    EnvironmentHandler.setExplosions(!EnvironmentHandler.hasExplosions());

                if (s instanceof Player) {
                    Player player = (Player) s;

                    player.closeInventory();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.performCommand("environment");
                        }
                    }.runTaskLater(Main.getInstance(), 10);
                }

                s.sendMessage(ChatUtil.format("&6Environment feature: &a" + toggle + " &6has been toggled."));
            }
        }

        return false;
    }

}
