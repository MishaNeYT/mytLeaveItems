package ru.mishaneyt.leave.logger;

import org.bukkit.Bukkit;

public class Logger {

    public static void info(String m) {
        Bukkit.getConsoleSender().sendMessage("§f[mytLeaveItems] " + m);
    }

    public static void warning(String m) {
        Bukkit.getConsoleSender().sendMessage("§e[mytLeaveItems] " + m);
    }

    public static void error(String m) {
        Bukkit.getConsoleSender().sendMessage("§c[mytLeaveItems] " + m);
    }
}