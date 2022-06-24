package ru.mishaneyt.leave.utils;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.mishaneyt.leave.Main;
import ru.mishaneyt.leave.config.ConfigUtils;

public class Cooldown {
    private final Main main;

    public Cooldown(Main main) {
        this.main = main;
    }

    public void addCooldown(Player p) {
        this.main.getCooldown().put(p, ConfigUtils.WAIT);

        new BukkitRunnable() {
            int i = ConfigUtils.WAIT;

            public void run() {
                main.getCooldown().put(p, this.i);
                this.i--;

                if (main.getCooldown().get(p) == 0) {
                    cancel();
                    main.getCooldown().remove(p);
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L);
    }
}