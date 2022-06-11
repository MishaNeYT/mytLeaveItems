package ru.mishaneyt.leave;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.mishaneyt.leave.commands.Commands;
import ru.mishaneyt.leave.config.ConfigManager;
import ru.mishaneyt.leave.config.ConfigUtils;
import ru.mishaneyt.leave.listeners.Listeners;
import ru.mishaneyt.leave.utils.Logger;

import java.util.Map;

public final class Main extends JavaPlugin {
    private static Main instance;
    private static final Map<Player, Integer> cooldown = Maps.newHashMap();

    @Override
    public void onEnable() {
        instance = this;

        ConfigManager.checkConfigs(this);
        ConfigUtils.load();

        new Commands(this);
        new Listeners(this);

        Logger.info("§2-----------------------------------");
        Logger.info("§a mytLeaveItems - успешно запущен!");
        Logger.info("§a Спасибо что используете мой плагин :3");
        Logger.info("§2-----------------------------------");
    }

    public static Main getInstance() {
        return instance;
    }

    public static Map<Player, Integer> getCooldown() {
        return cooldown;
    }
}