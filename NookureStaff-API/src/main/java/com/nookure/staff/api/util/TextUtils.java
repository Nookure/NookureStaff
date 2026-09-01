package com.nookure.staff.api.util;

import static com.nookure.staff.api.util.ServerUtils.hasClass;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public abstract class TextUtils {
    private static final boolean IS_PAPI_INSTALLED = hasClass("me.clip.placeholderapi.PlaceholderAPI");
    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;

    /**
     * Converts a string to a mini message string
     *
     * @param str the string to convert
     * @return the mini message string
     */
    public static String toMM(String str) {
        if (!str.contains("&") && !str.contains("§")) return str;

        return str.replace("§", "&")
                .replace("&0", "<reset><black>")
                .replace("&1", "<reset><dark_blue>")
                .replace("&2", "<reset><dark_green>")
                .replace("&3", "<reset><dark_aqua>")
                .replace("&4", "<reset><dark_red>")
                .replace("&5", "<reset><dark_purple>")
                .replace("&6", "<reset><gold>")
                .replace("&7", "<reset><grey>")
                .replace("&8", "<reset><dark_grey>")
                .replace("&9", "<reset><blue>")
                .replace("&a", "<reset><green>")
                .replace("&b", "<reset><aqua>")
                .replace("&c", "<reset><red>")
                .replace("&d", "<reset><light_purple>")
                .replace("&e", "<reset><yellow>")
                .replace("&f", "<reset><white>")
                .replace("&k", "<obf>")
                .replace("&l", "<b>")
                .replace("&m", "<st>")
                .replace("&n", "<u>")
                .replace("&r", "<reset>")
                .replace("&o", "<i>");
    }

    /**
     * Converts a string to a component
     *
     * @param str the string to convert
     * @return the component
     */
    public static Component toComponent(String str) {
        return MiniMessage.miniMessage().deserialize(toMM(str));
    }

    /**
     * Converts a list of strings to a list of components
     *
     * @param str the list of strings to convert
     * @return the list of components
     */
    public static List<Component> toComponent(List<String> str) {
        List<Component> components = new ArrayList<>();

        for (String s : str) {
            components.add(toComponent(s));
        }

        return components;
    }

    /**
     * Formats a time in milliseconds to a human-readable string
     *
     * @param time the time to format
     * @return the formatted time
     */
    public static String formatTime(long time) {
        StringBuilder buf = new StringBuilder();
        if (time > DAY) {
            long days = (time - time % DAY) / DAY;
            buf.append(days);
            buf.append("d");
            time = time % DAY;
        }
        if (time > HOUR) {
            long hours = (time - time % HOUR) / HOUR;
            if (!buf.isEmpty()) {
                buf.append(", ");
            }
            buf.append(hours);
            buf.append("h");
            time = time % HOUR;
        }
        if (time > MINUTE) {
            long minutes = (time - time % MINUTE) / MINUTE;
            if (!buf.isEmpty()) {
                buf.append(", ");
            }
            buf.append(minutes);
            buf.append("m");
            time = time % MINUTE;
        }
        if (time > SECOND) {
            long seconds = (time - time % SECOND) / SECOND;
            if (!buf.isEmpty()) {
                buf.append(", ");
            }
            buf.append(seconds);
            buf.append("s");
            time = time % SECOND;
        }
        return buf.toString();
    }

    public static String parsePlaceholdersWithPAPI(
            @NotNull final org.bukkit.entity.Player player, @NotNull final String message) {
        if (!IS_PAPI_INSTALLED) return message;

        return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, message);
    }
}
