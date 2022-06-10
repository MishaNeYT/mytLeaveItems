package ru.mishaneyt.leave.utils;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.mishaneyt.leave.Main;
import ru.mishaneyt.leave.config.ConfigUtils;

public class Cooldown {

    public static void addCooldown(Player p) {
        Main.getCooldown().put(p, ConfigUtils.WAIT);

        new BukkitRunnable() {
            int i = ConfigUtils.WAIT;

            public void run() {
                Main.getCooldown().put(p, this.i);
                this.i--;

                if (Main.getCooldown().get(p) == 0) {
                    cancel();
                    Main.getCooldown().remove(p);
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L);
    }

    public static void sendMessage(Player p) {
        p.sendMessage(ConfigUtils.COOLDOWN.replace("{time}", String.valueOf(Main.getCooldown().get(p))));
    }
}