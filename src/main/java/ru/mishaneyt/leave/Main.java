package ru.mishaneyt.leave;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.mishaneyt.leave.command.Commands;
import ru.mishaneyt.leave.config.ConfigManager;
import ru.mishaneyt.leave.listeners.Listeners;
import ru.mishaneyt.leave.logger.Logger;

import java.util.Map;

public final class Main extends JavaPlugin {
    private static Main instance;

    private final Map<Player, Integer> cooldown = Maps.newHashMap();

    @Override
    public void onEnable() {
        long time = System.currentTimeMillis();
        instance = this;

        ConfigManager configManager = new ConfigManager(this);
        configManager.checkConfigs();

        new Commands(this);
        new Listeners(this);

        Logger.info("§2-----------------------------------");
        Logger.info("§6 mytLeaveItems - §fуспешно запущен (§a" + (System.currentTimeMillis() - time) + "ms§f)");
        Logger.info("§f Спасибо что используете мой плагин :3");
        Logger.info("§2-----------------------------------");
    }

    public static synchronized Main getInstance() {
        return instance;
    }

    public Map<Player, Integer> getCooldown() {
        return this.cooldown;
    }
}