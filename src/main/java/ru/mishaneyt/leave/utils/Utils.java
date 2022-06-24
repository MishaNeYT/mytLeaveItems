package ru.mishaneyt.leave.utils;

import org.bukkit.entity.Player;
import ru.mishaneyt.leave.Main;
import ru.mishaneyt.leave.config.ConfigManager;
import ru.mishaneyt.leave.config.ConfigUtils;

public class Utils {

    public static String replace(String s) {
        if (s != null)
            return s.replace("&", "§")
                    .replace("%prefix-success%", ConfigUtils.PREFIX_SUCCESS).replace("%prefix-error%", ConfigUtils.PREFIX_ERROR);

        return null;
    }

    public void sendHelp(Player p) {
        for (String m : ConfigManager.getConfigMessages().getStringList("Messages.Help"))
            p.sendMessage(m.replace("&", "§").replace("%version%", Main.getInstance().getDescription().getVersion()));
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