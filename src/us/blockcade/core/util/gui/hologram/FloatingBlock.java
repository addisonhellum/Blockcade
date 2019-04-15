package us.blockcade.core.util.gui.hologram;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import us.blockcade.core.Main;

import java.util.UUID;

public class FloatingBlock {

    private UUID uuid = UUID.randomUUID();

    private Material material;
    private byte data;

    private Hologram hologram;
    private ArmorStand floater;

    public FloatingBlock(Material material) {
        this.material = material;
    }

    public FloatingBlock(Material material, String... lines) {
        this.material = material;

        this.hologram = new Hologram(lines);
    }

    public FloatingBlock(Material material, byte data) {
        this.material = material;
        this.data = data;
    }

    public FloatingBlock(Material material, byte data, String... lines) {
        this.material = material;
        this.data = data;

        this.hologram = new Hologram(lines);
    }

    public FloatingBlock spawn(Location location) {
        if (hologram != null) hologram.show(location);

        ItemStack skull = new ItemStack(material, 1, data);
        ArmorStand e = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        e.setVisible(false);
        e.setGravity(false);
        e.teleport(e.getLocation());
        e.setCanPickupItems(false);
        e.setRemoveWhenFarAway(false);
        e.getEquipment().setHelmet(skull);
        e.setSmall(false );

        this.floater = e;

        new BukkitRunnable() {
            double ticks = 0;
            Location loc = e.getLocation();

            @Override
            public void run() {
                if (getFloaterEntity() == null) cancel();

                ticks++;

                double change = (Math.cos(ticks/10));
                PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook packet = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(
                        e.getEntityId(),
                        (byte) (0), // X
                        (byte) ((change*2)), // Y
                        (byte) (0), // Z
                        (byte) (loc.getYaw()+7/360), // YAW
                        (byte) ((loc.getPitch() + 180) / 360),true); // PITCH
                loc.setYaw(loc.getYaw()+7);
                loc.setY(change);

                for (Player p : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(),0, 1);

        return this;
    }

    public UUID getUniqueId() { return uuid; }

    public Hologram getHologram() {
        return hologram;
    }

    public Material getMaterial() {
        return material;
    }

    public ArmorStand getFloaterEntity() {
        return floater;
    }

    public byte getData() {
        return data;
    }

    public void despawn() {
        ArmorStand armorStand = getFloaterEntity();
        armorStand.remove();

        Hologram hologram = getHologram();
        hologram.destroy();

        this.floater = null;
    }

}
