package ru.mishaneyt.leave.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import ru.mishaneyt.leave.config.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class CommandsTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> line = new ArrayList<>();

        if (args.length == 1) {
            line.add("help");
            line.add("give");
            line.add("reload");
            return line;
        }

        else if (args.length == 2) {
            if ("give".equalsIgnoreCase(args[0])) {
                for (Player all : Bukkit.getOnlinePlayers())
                    line.add(all.getName());
                return line;
            }
        }

        else if (args.length == 3) {
            if ("give".equalsIgnoreCase(args[0])) {
                if (ConfigManager.getItems().getConfigurationSection("Items") != null)
                    line.addAll(ConfigManager.getItems().getConfigurationSection("Items").getValues(false).keySet());
                return line;
            }
        }
        return null;
    }
}