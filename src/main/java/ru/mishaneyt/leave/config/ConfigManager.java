package ru.mishaneyt.leave.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import ru.mishaneyt.leave.Main;
import ru.mishaneyt.leave.utils.Logger;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private static final File file_config = new File(Main.getInstance().getDataFolder(), "config.yml");
    private static final FileConfiguration configuration_config = YamlConfiguration.loadConfiguration(file_config);

    private static final File file_items = new File(Main.getInstance().getDataFolder(), "items.yml");
    private static final FileConfiguration configuration_items = YamlConfiguration.loadConfiguration(file_items);

    private static final File file_messages = new File(Main.getInstance().getDataFolder(), "messages.yml");
    private static final FileConfiguration configuration_messages = YamlConfiguration.loadConfiguration(file_messages);

    public static FileConfiguration getConfig() {
        return configuration_config;
    }

    public static FileConfiguration getConfigItems() {
        return configuration_items;
    }

    public static FileConfiguration getConfigMessages() {
        return configuration_messages;
    }

    public static void checkConfigs(Main main) {
        if (!file_config.exists()) {
            main.saveResource("config.yml", false);
            Logger.info("§aКонфигурация config.yml - успешно создан.");
        }
        if (!file_items.exists()) {
            main.saveResource("items.yml", false);
            Logger.info("§aКонфигурация items.yml - успешно создан.");
        }
        if (!file_messages.exists()) {
            main.saveResource("messages.yml", false);
            Logger.info("§aКонфигурация messages.yml - успешно создан.");
        }
    }

    public static void reload(Player p) {
        try {
            getConfig().load(file_config);
            getConfigItems().load(file_items);
            getConfigMessages().load(file_messages);

            if (ConfigUtils.ADVANCE_RELOAD) {
                PluginManager pm = Bukkit.getPluginManager();
                pm.disablePlugin(Main.getInstance());
                pm.enablePlugin(Main.getInstance());
            }

            p.sendMessage(ConfigUtils.RELOAD);

        } catch (IOException | InvalidConfigurationException ex) {
            Logger.warning("Не удалось перезагрузить конфиг " + file_config);
            Logger.warning("Не удалось перезагрузить конфиг " + file_items);
            Logger.warning("Не удалось перезагрузить конфиг " + file_messages);
        }
    }
}