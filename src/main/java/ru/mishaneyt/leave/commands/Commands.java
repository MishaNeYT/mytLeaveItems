package ru.mishaneyt.leave.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import ru.mishaneyt.leave.Main;
import ru.mishaneyt.leave.config.ConfigManager;
import ru.mishaneyt.leave.config.ConfigUtils;
import ru.mishaneyt.leave.utils.ItemUtils;
import ru.mishaneyt.leave.utils.Utils;

public class Commands implements CommandExecutor {

    public Commands(Main main) {
        main.getCommand("leaveitems").setExecutor(this);
        main.getCommand("leaveitems").setTabCompleter(new CommandsTab());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ConfigUtils.PLAYER);
            return true;
        }
        if (!sender.hasPermission("leaveitems.use")) {
            sender.sendMessage(ConfigUtils.PERMISSION);
            return true;
        }

        Player p = (Player) sender;
        PluginDescriptionFile version = Main.getInstance().getDescription();

        if (args.length == 0) {
            for (String m : ConfigManager.getConfigMessages().getStringList("Messages.Help"))
                p.sendMessage(Utils.color(m).replace("%version%", version.getVersion()));
            return true;
        }

        // /leaveitems help | reload
        if (args.length == 1) {
            if ("help".equalsIgnoreCase(args[0])) {
                for (String m : ConfigManager.getConfigMessages().getStringList("Messages.Help"))
                    p.sendMessage(Utils.color(m).replace("%version%", version.getVersion()));
                return true;
            }

            else if ("reload".equalsIgnoreCase(args[0])) {
                ConfigManager.reload(p);
                return true;
            } else sender.sendMessage(ConfigUtils.ERROR);
        }

        // /leaveitems give <игрок> <предмет>
        else if (args.length == 3) {
            if ("give".equalsIgnoreCase(args[0])) {
                Player t = Bukkit.getPlayer(args[1]);

                if (t == null) {
                    sender.sendMessage(ConfigUtils.ONLINE);
                    return true;
                }

                if (ConfigManager.getConfigItems().contains("Items." + args[2])) {
                    ItemUtils.get(t, 1, args[2]);
                    sender.sendMessage(ConfigUtils.GIVE_ITEM
                            .replace("{player}", t.getName())
                            .replace("{item}", args[2]));
                    return true;

                } else sender.sendMessage(ConfigUtils.FOUND);
            } else sender.sendMessage(ConfigUtils.ERROR);
        }

        // /leaveitems give <игрок> <предмет> <количество>
        else if (args.length == 4) {

            if ("give".equalsIgnoreCase(args[0])) {
                Player t = Bukkit.getPlayer(args[1]);

                if (!Utils.isDouble(args[3])) {
                    sender.sendMessage(ConfigUtils.NUMBER);
                    return true;
                }

                if (t == null) {
                    sender.sendMessage(ConfigUtils.ONLINE);
                    return true;
                }

                int amount = Integer.parseInt(args[3]);

                if (ConfigManager.getConfigItems().contains("Items." + args[2])) {
                    ItemUtils.get(t, amount, args[2]);
                    sender.sendMessage(ConfigUtils.GIVE_AMOUNT
                            .replace("{player}", t.getName())
                            .replace("{item}", args[2])
                            .replace("{amount}", String.valueOf(amount)));
                    return true;

                } else sender.sendMessage(ConfigUtils.FOUND);
            } else sender.sendMessage(ConfigUtils.ERROR);
        } else sender.sendMessage(ConfigUtils.USE);

        return false;
    }
}