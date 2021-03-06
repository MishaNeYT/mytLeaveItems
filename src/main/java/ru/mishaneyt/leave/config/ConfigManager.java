package ru.mishaneyt.leave.config;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import ru.mishaneyt.leave.Main;
import ru.mishaneyt.leave.utils.Logger;
import ru.mishaneyt.leave.utils.Utils;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final Main main;

    public ConfigManager(Main main) {
        this.main = main;
    }

    private static final File file_config = new File(Main.getInstance().getDataFolder(), "config.yml");
    private static final FileConfiguration config = YamlConfiguration.loadConfiguration(file_config);

    private static final File file_items = new File(Main.getInstance().getDataFolder(), "items.yml");
    private static final FileConfiguration items = YamlConfiguration.loadConfiguration(file_items);

    private static final File file_messages = new File(Main.getInstance().getDataFolder(), "messages.yml");
    private static final FileConfiguration messages = YamlConfiguration.loadConfiguration(file_messages);

    public static FileConfiguration getConfig() {
        return config;
    }

    public static FileConfiguration getItems() {
        return items;
    }

    public static FileConfiguration getMessages() {
        return messages;
    }

    public void checkConfigurations() {
        if (!file_config.exists()) {
            this.main.saveResource("config.yml", false);
            Logger.info("§aКонфигурация config.yml - успешно создан.");
        }
        if (!file_items.exists()) {
            this.main.saveResource("items.yml", false);
            Logger.info("§aКонфигурация items.yml - успешно создан.");
        }
        if (!file_messages.exists()) {
            this.main.saveResource("messages.yml", false);
            Logger.info("§aКонфигурация messages.yml - успешно создан.");
        }
    }

    public void saveConfigs() {
        try {
            getConfig().save(file_config);
            getItems().save(file_items);
            getMessages().save(file_messages);

        } catch (IOException ex) {
            Logger.error("Не удалось сохранить конфигурации..");
        }
    }

    public void loadConfigurations() {
        this.checkConfigurations();

        try {
            getConfig().load(file_config);
            getItems().load(file_items);
            getMessages().load(file_messages);
            saveConfigs();

        } catch (IOException | InvalidConfigurationException ex) {
            Logger.error("Не удалось перезагрузить конфигурации..");
        }
    }

    public void reloadPlugin(CommandSender sender) {
        this.checkConfigurations();

        try {
            getConfig().load(file_config);
            getItems().load(file_items);
            getMessages().load(file_messages);
            saveConfigs();

            if (ConfigManager.getConfig().getBoolean("Settings.AdvanceReload")) {
                PluginManager pm = Bukkit.getPluginManager();
                pm.disablePlugin(this.main);
                pm.enablePlugin(this.main);
            }

            sender.sendMessage(Utils.replace(ConfigManager.getMessages().getString("Messages.Command.Reload")));

        } catch (IOException | InvalidConfigurationException ex) {
            Logger.error("Не удалось перезагрузить конфигурации..");
        }
    }
}