package us.blockcade.core.util.effect;

import java.lang.reflect.Method;

import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * FireworkEffectPlayer v1.0
 *
 * FireworkEffectPlayer provides a thread-safe and (reasonably) version independant way to instantly explode a FireworkEffect at a given location.
 * You are welcome to use, redistribute, modify and destroy your own copies of this source with the following conditions:
 *
 * 1. No warranty is given or implied.
 * 2. All damage is your own responsibility.
 * 3. You provide credit publicly to the original source should you release the plugin.
 *
 * @author codename_B
 */
public class FireworkEffectPlayer {

    public void playFirework(World world, Location loc, FireworkEffect fe) {
        Firework firework = (Firework) world.spawn(loc, Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.clearEffects();
        meta.addEffect(fe);
        firework.setFireworkMeta(meta);
        firework.detonate();
    }

    /**
     * Internal method, used as shorthand to grab our method in a nice friendly manner
     * @param cl
     * @param method
     * @return Method (or null)
     */
    private static Method getMethod(Class<?> cl, String method) {
        for(Method m : cl.getMethods()) {
            if(m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }

}