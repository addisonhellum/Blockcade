package us.blockcade.core.util.blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import us.blockcade.core.util.math.Coordinate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionEditor {

    private BoundedArea area;
    private World world;

    private Map<Location, BlockData> originals = new HashMap<>();

    public RegionEditor(Location pos1, Location pos2) {
        area = new BoundedArea(Coordinate.fromLocation(pos1), Coordinate.fromLocation(pos2));
        world = pos1.getWorld();

        for (Block block : area.blocksBetween(world))
            originals.put(block.getLocation(), new BlockData(block.getType(), block.getData()));
    }

    public BoundedArea getArea() {
        return area;
    }

    public World getWorld() {
        return world;
    }

    public List<Block> getBlocks() {
        return getArea().blocksBetween(getWorld());
    }

    public RegionEditor replace(BlockData from, BlockData to) {
        for (Block block : getBlocks()) {
            if (block.getType().equals(from.getMaterial()) && block.getData() == from.getData()) {
                block.setType(to.getMaterial());
                block.setData(to.getData());
            }
        }
        return this;
    }

    public RegionEditor replace(Material from, Material to) {
        for (Block block : getBlocks()) {
            if (block.getType().equals(from)) {
                block.setType(to);
            }
        }
        return this;
    }

    public RegionEditor set(BlockData to) {
        for (Block block : getBlocks()) {
            block.setType(to.getMaterial());
            block.setData(to.getData());
        }
        return this;
    }

    public RegionEditor restore() {
        for (Location loc : originals.keySet()) {
            Block b = loc.getBlock();
            b.setType(originals.get(loc).getMaterial());
            b.setData(originals.get(loc).getData());
        }
        return this;
    }

}
