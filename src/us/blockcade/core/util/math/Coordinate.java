package us.blockcade.core.util.math;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Coordinate {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    private boolean is3D = false;
    private boolean orientation = false;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.is3D = true;
    }

    public Coordinate(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.yaw = yaw;
        this.pitch = pitch;

        this.is3D = true;
        this.orientation = true;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public boolean is3D() {
        return is3D;
    }

    public boolean isOrientated() {
        return orientation;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public Location makeLocation(String worldName) {
        try {
            World world = Bukkit.getWorld(worldName);

            if (!is3D()) return world.getSpawnLocation();
            if (!isOrientated()) return new Location(world, getX(), getY(), getZ());
            else return new Location(world, getX(), getY(), getZ(), getYaw(), getPitch());
        }
        catch (Exception e) {
            System.out.println("Error making Coordinate location for world \"" + worldName + "\". Perhaps" +
                    " that world does not exist!");

        } return null;
    }

    public Location makeLocation(World world) {
        if (!is3D()) return world.getSpawnLocation();
        if (!isOrientated()) return new Location(world, getX(), getY(), getZ());
        else return new Location(world, getX(), getY(), getZ(), getYaw(), getPitch());
    }

    public static Coordinate fromLocation(Location location) {
        double x = location.getBlockX() + 0.5;
        double y = location.getBlockY() + 1;
        double z = location.getBlockZ() + 0.5;
        float yaw = location.getYaw();
        float pitch = location.getPitch();

        return new Coordinate(x, y, z, yaw, pitch);
    }

    @Override
    public String toString() {
        return "x:" + getX() + ",y:" + getY() + ",z:" + getZ() + ",yaw:" + getYaw() + ",pitch:" + getPitch();
    }

}
