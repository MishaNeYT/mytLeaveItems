package ru.mishaneyt.leave.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import ru.mishaneyt.leave.Main;
import ru.mishaneyt.leave.config.ConfigManager;

public class Utils {

    public static String color(String s) {
        if (s != null) return s.replace("&", "§");

        return null;
    }

    public static void sendHelp(Player p) {
        PluginDescriptionFile version = Main.getInstance().getDescription();

        for (String m : ConfigManager.getConfigMessages().getStringList("Messages.Help"))
            p.sendMessage(Utils.color(m)
                    .replace("%version%", version.getVersion()));
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }
}