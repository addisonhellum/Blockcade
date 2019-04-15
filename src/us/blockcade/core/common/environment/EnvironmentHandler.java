package us.blockcade.core.common.environment;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import us.blockcade.core.Main;
import us.blockcade.core.commands.administrative.AoCommand;

public class EnvironmentHandler implements Listener {

    private static FileConfiguration config = Main.getInstance().getConfig();

    public static boolean hasWeather() { return config.getBoolean("environmental.allow-weather"); }
    public static boolean canDecay() { return config.getBoolean("environmental.allow-decay"); }
    public static boolean canDoPhysics() { return config.getBoolean("environmental.use-physics"); }
    public static boolean hasMobs() { return config.getBoolean("environmental.spawn-mobs"); }
    public static boolean hasFallDamage() { return config.getBoolean("environmental.allow-fall-damage"); }
    public static boolean hasHunger() { return config.getBoolean("environmental.allow-hunger"); }
    public static boolean hasAllDamage() { return config.getBoolean("environmental.allow-all-damage"); }
    public static boolean hasPvP() { return config.getBoolean("environmental.allow-pvp"); }
    public static boolean canPlaceBlocks() { return config.getBoolean("environmental.allow-place-blocks"); }
    public static boolean canBreakBlocks() { return config.getBoolean("environmental.allow-break-blocks"); }
    public static boolean hasExplosions() { return config.getBoolean("environmental.allow-explosions"); }

    private static void save() {
        Main.getInstance().saveConfig();
    }

    public static void setWeather(boolean value) {
        config.set("environmental.allow-weather", value);
        save();
    }

    public static void setDecay(boolean value) {
        config.set("environmental.allow-decay", value);
        save();
    }

    public static void setPhysics(boolean value) {
        config.set("environmental.use-physics", value);
        save();
    }

    public static void setSpawnMobs(boolean value) {
        config.set("environmental.spawn-mobs", value);
        save();
    }

    public static void setFallDamage(boolean value) {
        config.set("environmental.allow-fall-damage", value);
        save();
    }

    public static void setHunger(boolean value) {
        config.set("environmental.allow-hunger", value);
        save();
    }

    public static void setAllDamage(boolean value) {
        config.set("environmental.allow-all-damage", value);
        save();
    }

    public static void setPvP(boolean value) {
        config.set("environmental.allow-pvp", value);
        save();
    }

    public static void setPlaceBlocks(boolean value) {
        config.set("environmental.allow-place-blocks", value);
        save();
    }

    public static void setBreakBlocks(boolean value) {
        config.set("environmental.allow-break-blocks", value);
        save();
    }

    public static void setExplosions(boolean value) {
        config.set("environmental.allow-explosions", value);
        save();
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        if (!hasWeather()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onDecay(LeavesDecayEvent event) {
        if (!canDecay()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onMelt(BlockFadeEvent event) {
        if (!canDecay()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPhysics(BlockPhysicsEvent event) {
        if (!canDoPhysics()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onHostileSpawn(EntitySpawnEvent event) {
        if (hasMobs()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (hasFallDamage()) return;
        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause.equals(EntityDamageEvent.DamageCause.FALL))
            event.setCancelled(true);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (hasHunger()) return;
        event.setCancelled(true);
        Player player = (Player) event.getEntity();
        player.setFoodLevel(20);
    }

    @EventHandler
    public void onAllDamage(EntityDamageEvent event) {
        if (hasAllDamage()) return;
        //if (GameHandler.started) return;
        if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent event) {
        if (hasPvP()) return;
        //if (GameHandler.started) return;
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (AoCommand.hasOverride((Player) event.getDamager())) return;
            event.setCancelled(true);
        }
        if (event.getDamager().getType().equals(EntityType.ARROW)) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player && event.getEntity() instanceof Player)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (canBreakBlocks()) return;
        if (AoCommand.hasOverride(event.getPlayer())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (canPlaceBlocks()) return;
        if (AoCommand.hasOverride(event.getPlayer())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (hasExplosions()) return;
        event.setCancelled(true);
        event.getLocation().getWorld().playEffect(event.getLocation(), Effect.EXPLOSION_LARGE, 1F);
        event.getLocation().getWorld().playSound(event.getLocation(), Sound.EXPLODE, 10F, 10F);
    }

}
