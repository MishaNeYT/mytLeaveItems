package ru.mishaneyt.leave.utils;

import org.bukkit.entity.Player;
import ru.mishaneyt.leave.Main;
import ru.mishaneyt.leave.config.ConfigManager;

public class Utils {
    private final Main main;

    public Utils(Main main) {
        this.main = main;
    }

    public static String replace(String s) {
        String success = ConfigManager.getMessages().getString("Messages.Prefix.Success").replace("&", "§");
        String error = ConfigManager.getMessages().getString("Messages.Prefix.Error").replace("&", "§");

        if (s != null)
            return s.replace("&", "§")
                    .replace("%prefix-success%", success)
                    .replace("%prefix-error%", error);

        return null;
    }

    public static String color(String s) {
        if (s != null)
            return s.replace("&", "§");
        return null;
    }

    public void sendHelp(Player p) {
        String version = this.main.getDescription().getVersion();

        for (String m : ConfigManager.getMessages().getStringList("Messages.Help"))
            p.sendMessage(m.replace("&", "§").replace("%version%", version));
    }

    public boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }
}