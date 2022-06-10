package ru.mishaneyt.leave.config;

import org.bukkit.configuration.file.FileConfiguration;
import ru.mishaneyt.leave.utils.Utils;

public class ConfigUtils {

    public static boolean FALL_DAMAGE;
    public static boolean ENABLE_TITLE;
    public static boolean ENABLE_CHAT;
    public static boolean ENABLE_COOLDOWN;
    public static int WAIT;

    public static String PERMISSION;
    public static String PLAYER;
    public static String USE;
    public static String ERROR;

    public static String GIVE_ITEM;
    public static String GIVE_AMOUNT;
    public static String COOLDOWN;
    public static String RELOAD;
    public static String ONLINE;
    public static String FOUND;
    public static String NUMBER;

    public static String TITLE;
    public static String SUBTITLE;

    public static String CHAT;

    public static void load() {
        FileConfiguration config = ConfigManager.getConfig();
        FileConfiguration messages = ConfigManager.getConfigMessages();

        // config.yml
        FALL_DAMAGE = config.getBoolean("Settings.FallDamage");
        ENABLE_TITLE = config.getBoolean("Settings.Title");
        ENABLE_CHAT = config.getBoolean("Settings.Chat");
        ENABLE_COOLDOWN = config.getBoolean("Cooldown.Enable");
        WAIT = config.getInt("Cooldown.Wait");

        // messages.yml
        PERMISSION = Utils.color(messages.getString("Messages.Permission"));
        PLAYER = Utils.color(messages.getString("Messages.Player"));
        USE = Utils.color(messages.getString("Messages.Use"));
        ERROR = Utils.color(messages.getString("Messages.Error"));

        GIVE_ITEM = Utils.color(messages.getString("Messages.Command.GiveItem"));
        GIVE_AMOUNT = Utils.color(messages.getString("Messages.Command.GiveAmount"));
        COOLDOWN = Utils.color(messages.getString("Messages.Others.Cooldown"));
        RELOAD = Utils.color(messages.getString("Messages.Others.Reload"));
        ONLINE = Utils.color(messages.getString("Messages.Others.Online"));
        FOUND = Utils.color(messages.getString("Messages.Others.Found"));
        NUMBER = Utils.color(messages.getString("Messages.Others.Number"));

        TITLE = Utils.color(messages.getString("SendTitle.Title"));
        SUBTITLE = Utils.color(messages.getString("SendTitle.SubTitle"));

        CHAT = Utils.color(messages.getString("SendChat.Message"));
    }
}