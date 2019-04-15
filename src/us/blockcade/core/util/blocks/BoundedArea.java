package us.blockcade.core.util.blocks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import us.blockcade.core.util.math.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class BoundedArea {

    private Coordinate corner1;
    private Coordinate corner2;
    private Coordinate center;

    public BoundedArea(Coordinate pos1, Coordinate pos2) {
        this.corner1 = pos1;
        this.corner2 = pos2;
    }

    public BoundedArea(Coordinate pos1, Coordinate pos2, Coordinate center) {
        this.corner1 = pos1;
        this.corner2 = pos2;
        this.center = center;
    }

    public Coordinate getCenter() {
        return center;
    }

    public void setCenter(Coordinate center) {
        this.center = center;
    }

    public Coordinate getFirstPosition() {
        return corner1;
    }

    public Coordinate getSecondPosition() {
        return corner2;
    }

    private boolean betweenRange(double check, double val1, double val2) {
        double high = val1;
        double low = val2;

        if (val2 > val1) {
            high = val2;
            low = val1;
        }

        return check <= high && check >= low;
    }

    public boolean isWithinBounds(Location location) {
        return betweenRange(location.getX(), corner1.getX(), corner2.getX()) &&
                betweenRange(location.getY(), corner1.getY(), corner2.getY()) &&
                betweenRange(location.getZ(), corner1.getZ(), corner2.getZ());
    }

    public List<Block> blocksBetween(World world) {
        List<Block> blocks = new ArrayList<>();
        Location loc1 = corner1.makeLocation(world);
        Location loc2 = corner2.makeLocation(world);

        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

        int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

        for(int x = bottomBlockX; x <= topBlockX; x++) {
            for(int z = bottomBlockZ; z <= topBlockZ; z++) {
                for(int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

}
