package us.blockcade.core.util;

import org.bukkit.Bukkit;

public enum ServerPackage {

    MINECRAFT("net.minecraft.server." + getServerVersion()),
    CRAFTBUKKIT("org.bukkit.craftbukkit." + getServerVersion());

    private final String path;

    ServerPackage(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }

    public Class<?> getClass(String className) throws ClassNotFoundException {
        return Class.forName(this.toString() + "." + className);
    }

    public static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

}