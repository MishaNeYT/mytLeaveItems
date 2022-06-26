package ru.mishaneyt.leave.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.mishaneyt.leave.Main;
import ru.mishaneyt.leave.config.ConfigManager;
import ru.mishaneyt.leave.utils.ItemBuilder;
import ru.mishaneyt.leave.utils.Utils;

public class Commands implements CommandExecutor {
    private final Main main;

    public Commands(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.replace(ConfigManager.getMessages().getString("Messages.Command.Player")));
            return true;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("leaveitems.use")) {
            p.sendMessage(Utils.replace(ConfigManager.getMessages().getString("Messages.Command.Permission")));
            return true;
        }

        Utils utils = new Utils(this.main);

        if (args.length == 0) {
            utils.sendHelp(p);
            return true;
        }

        // /leaveitems help | reload
        if (args.length == 1) {
            if ("help".equalsIgnoreCase(args[0])) {
                utils.sendHelp(p);
                return true;
            }

            else if ("reload".equalsIgnoreCase(args[0])) {
                ConfigManager configManager = new ConfigManager(this.main);
                configManager.reloadPlugin(p);
                return true;

            } else sender.sendMessage(Utils.replace(ConfigManager.getMessages().getString("Messages.Command.Error")));
        }

        // /leaveitems give <игрок> <предмет>
        else if (args.length == 3) {
            if ("give".equalsIgnoreCase(args[0])) {
                Player t = Bukkit.getPlayer(args[1]);

                if (t == null) {
                    sender.sendMessage(Utils.replace(ConfigManager.getMessages().getString("Messages.Command.Use.Online")));
                    return true;
                }

                if (ConfigManager.getItems().contains("Items." + args[2])) {
                    ItemBuilder itemBuilder = new ItemBuilder(t, args[2], 1);

                    itemBuilder.get();
                    sender.sendMessage(Utils.replace(ConfigManager.getMessages().getString("Messages.Success.Give")
                            .replace("{player}", t.getName())
                            .replace("{item}", args[2])));
                    return true;

                } else sender.sendMessage(Utils.replace(ConfigManager.getMessages().getString("Messages.Command.Use.Found")));
            } else sender.sendMessage(Utils.replace(ConfigManager.getMessages().getString("Messages.Command.Error")));
        }

        // /leaveitems give <игрок> <предмет> <количество>
        else if (args.length == 4) {

            if ("give".equalsIgnoreCase(args[0])) {
                Player t = Bukkit.getPlayer(args[1]);

                if (!utils.isDouble(args[3])) {
                    sender.sendMessage(Utils.replace(ConfigManager.getMessages().getString("Messages.Command.Use.Number")));
                    return true;
                }

                if (t == null) {
                    sender.sendMessage(Utils.replace(ConfigManager.getMessages().getString("Messages.Command.Use.Online")));
                    return true;
                }

                int amount = Integer.parseInt(args[3]);

                if (ConfigManager.getItems().contains("Items." + args[2])) {
                    ItemBuilder itemBuilder = new ItemBuilder(t, args[2], amount);

                    itemBuilder.get();
                    sender.sendMessage(Utils.replace(ConfigManager.getMessages().getString("Messages.Success.GiveAmount")
                            .replace("{player}", t.getName())
                            .replace("{item}", args[2])
                            .replace("{amount}", String.valueOf(amount))));
                    return true;

                } else sender.sendMessage(Utils.replace(ConfigManager.getMessages().getString("Messages.Command.Use.Found")));
            } else sender.sendMessage(Utils.replace(ConfigManager.getMessages().getString("Messages.Command.Error")));
        } else utils.sendHelp(p);
        return false;
    }
}