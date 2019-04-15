package us.blockcade.core.util.npc;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import us.blockcade.core.Main;
import us.blockcade.core.util.effect.ParticleEffect;
import us.blockcade.core.util.gui.hologram.Hologram;

public class VillagerNPC implements Listener {

    private Entity entity;
    private Hologram hologram;

    private Location location;
    private String[] name;
    private String command = "";

    public VillagerNPC(Location location, String... name) {
        this.location = location;
        this.name = name;

        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public Entity getEntity() {
        return entity;
    }

    public Location getLocation() {
        return location;
    }

    public String[] getName() {
        return name;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void teleport(Location location) {
        this.location = location;
        entity.teleport(location);

        getHologram().destroy();
        getHologram().show(location);
    }

    public void spawn() {
        entity = location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        noAI(entity);

        Hologram hologram = new Hologram(name);
        hologram.show(getLocation());

        this.hologram = hologram;
    }

    public void despawn(int ticksDelay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                getHologram().destroy();
                getEntity().remove();

                ParticleEffect.SMOKE_LARGE.display(0.5F, 1F, 0.5F, 0, 8,
                        getLocation().clone().add(0, 1, 0), 20);
            }
        }.runTaskLater(Main.getInstance(), ticksDelay);
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (getCommand().equalsIgnoreCase("")) return;
        if (event.getRightClicked().equals(getEntity())) {
            event.getPlayer().performCommand(getCommand());
        }
    }

    @EventHandler
    public void onDamageNPC(EntityDamageEvent event) {
        if (event.getEntity().equals(getEntity()))
            event.setCancelled(true);
    }

    public static void noAI(Entity bukkitEntity) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) bukkitEntity).getHandle();
        NBTTagCompound tag = nmsEntity.getNBTTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        nmsEntity.c(tag);
        tag.setInt("NoAI", 1);
        nmsEntity.f(tag);
    }

}
