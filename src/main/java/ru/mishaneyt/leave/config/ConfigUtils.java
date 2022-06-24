package ru.mishaneyt.leave.config;

import org.bukkit.configuration.file.FileConfiguration;
import ru.mishaneyt.leave.utils.Utils;

public class ConfigUtils {

    // Config.yml
    public static boolean FALL_DAMAGE;
    public static boolean ADVANCE_RELOAD;
    public static boolean ENABLE_TITLE;
    public static boolean ENABLE_CHAT;
    public static boolean ENABLE_COOLDOWN;
    public static int WAIT;

    // Messages.yml
    public static String PREFIX_SUCCESS;
    public static String PREFIX_ERROR;

    public static String PERMISSION;
    public static String PLAYER;
    public static String ERROR;
    public static String RELOAD;

    public static String USE;
    public static String ONLINE;
    public static String FOUND;
    public static String NUMBER;

    public static String GIVE_ITEM;
    public static String GIVE_AMOUNT;

    public static String COOLDOWN;

    public static String TITLE;
    public static String SUBTITLE;

    public void load() {
        FileConfiguration config = ConfigManager.getConfig();
        FileConfiguration messages = ConfigManager.getConfigMessages();

        // config.yml
        FALL_DAMAGE = config.getBoolean("Settings.FallDamage");
        ADVANCE_RELOAD = config.getBoolean("Settings.AdvanceReload");
        ENABLE_TITLE = config.getBoolean("Settings.Title");
        ENABLE_CHAT = config.getBoolean("Settings.Chat");
        ENABLE_COOLDOWN = config.getBoolean("Cooldown.Enable");
        WAIT = config.getInt("Cooldown.Wait");

        // messages.yml
        PREFIX_SUCCESS = messages.getString("Messages.Prefix.Success").replace("&", "§");
        PREFIX_ERROR = messages.getString("Messages.Prefix.Error").replace("&", "§");

        PERMISSION = Utils.replace(messages.getString("Messages.Command.Permission"));
        PLAYER = Utils.replace(messages.getString("Messages.Command.Player"));
        ERROR = Utils.replace(messages.getString("Messages.Command.Error"));
        RELOAD = Utils.replace(messages.getString("Messages.Command.Reload"));

        USE = Utils.replace(messages.getString("Messages.Command.Use.Help"));
        ONLINE = Utils.replace(messages.getString("Messages.Command.Use.Online"));
        FOUND = Utils.replace(messages.getString("Messages.Command.Use.Found"));
        NUMBER = Utils.replace(messages.getString("Messages.Command.Use.Number"));

        GIVE_ITEM = Utils.replace(messages.getString("Messages.Success.Give"));
        GIVE_AMOUNT = Utils.replace(messages.getString("Messages.Success.GiveAmount"));

        COOLDOWN = Utils.replace(messages.getString("Messages.Others.Cooldown"));

        TITLE = Utils.replace(messages.getString("Messages.Title.First"));
        SUBTITLE = Utils.replace(messages.getString("Messages.Title.SubTitle"));
    }
}