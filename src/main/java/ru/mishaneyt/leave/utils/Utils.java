package ru.mishaneyt.leave.utils;

public class Utils {

    public static String color(String s) {
        if (s != null) return s.replace("&", "§");

        return null;
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