package us.blockcade.core.util.effect;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.blockcade.core.Main;

import java.util.ArrayList;
import java.util.List;

public class ExpCountdown {

    private int seconds;
    private List<String> currentNames = new ArrayList<>();

    public ExpCountdown(int seconds) {
        this.seconds = seconds;
    }

    public int getTotalSeconds() {
        return seconds;
    }

    public double getDecrement() {
        return 1.0 / getTotalSeconds();
    }

    public double getProgress(int seconds) {
        return 1.0 - (seconds * getDecrement());
    }

    public boolean isRunning(Player player) {
        return currentNames.contains(player.getName());
    }

    public void run(Player player) {
        if (isRunning(player))
            currentNames.remove(player.getName());

        new BukkitRunnable() {
            int sec = getTotalSeconds();

            @Override
            public void run() {
                if (sec == 0) {
                    currentNames.remove(player.getName());
                    cancel();
                }

                player.setLevel(sec);
                player.setExp(((float) (1 - getProgress(sec))));
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 2, 1);

                sec--;
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }

}
