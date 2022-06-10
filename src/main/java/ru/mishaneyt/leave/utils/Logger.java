package ru.mishaneyt.leave.utils;

import org.bukkit.Bukkit;

public class Logger {

    public static void info(String m) {
        Bukkit.getConsoleSender().sendMessage("§f[myLeaveItems] " + m);
    }

    public static void warning(String m) {
        Bukkit.getConsoleSender().sendMessage("§e[myLeaveItems] " + m);
    }

    public static void error(String m) {
        Bukkit.getConsoleSender().sendMessage("§c[myLeaveItems] " + m);
    }
}