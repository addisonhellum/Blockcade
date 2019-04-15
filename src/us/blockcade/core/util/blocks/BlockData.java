package us.blockcade.core.util.blocks;

import org.bukkit.Material;

public class BlockData {

    private byte data;
    private Material material;

    public BlockData(Material material, byte data) {
        this.material = material;
        this.data = data;
    }

    public Material getMaterial() {
        return material;
    }

    public byte getData() {
        return data;
    }

    @Override
    public String toString() {
        return "" + material.name() + "," + data;
    }

    public static BlockData fromString(String string) {
        String[] args = string.split(",");
        return new BlockData(Material.valueOf(args[0]), Byte.parseByte(args[1]));
    }

}
