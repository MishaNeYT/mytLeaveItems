package ru.mishaneyt.leave.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.mishaneyt.leave.Main;
import ru.mishaneyt.leave.config.ConfigManager;
import ru.mishaneyt.leave.config.ConfigUtils;
import ru.mishaneyt.leave.utils.ItemBuilder;
import ru.mishaneyt.leave.utils.Utils;

public class Commands implements CommandExecutor {
    private final Main main;

    public Commands(Main main) {
        this.main = main;

        this.main.getCommand("leaveitems").setExecutor(this);
        this.main.getCommand("leaveitems").setTabCompleter(new CommandsTab());
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
        Utils utils = new Utils();

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
                configManager.reload(p);
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
                    ItemBuilder itemBuilder = new ItemBuilder(t, args[2], 1);

                    itemBuilder.get();
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

                if (!utils.isDouble(args[3])) {
                    sender.sendMessage(ConfigUtils.NUMBER);
                    return true;
                }

                if (t == null) {
                    sender.sendMessage(ConfigUtils.ONLINE);
                    return true;
                }

                int amount = Integer.parseInt(args[3]);

                if (ConfigManager.getConfigItems().contains("Items." + args[2])) {
                    ItemBuilder itemBuilder = new ItemBuilder(t, args[2], amount);

                    itemBuilder.get();
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