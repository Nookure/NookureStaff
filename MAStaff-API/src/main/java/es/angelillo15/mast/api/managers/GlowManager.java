package es.angelillo15.mast.api.managers;

import lombok.Getter;
import me.MrGraycat.eGlow.API.EGlowAPI;
import me.MrGraycat.eGlow.EGlow;
import org.bukkit.ChatColor;
import java.util.ArrayList;
import java.util.HashMap;

public class GlowManager {
    @Getter
    private static HashMap<String, ChatColor> colors = new HashMap<>();

    /**
     * @param group the name of the color
     * @param color the color
     */
    public static void addColor(String group, ChatColor color) {
        colors.put(group, color);
    }

    /**
     * @param group the name of the color to get
     * @return the color
     */
    public static ChatColor getColor(String group) {
        return colors.get(group) == null ? ChatColor.GREEN : colors.get(group);
    }

    /**
     * Get all colors
     * @return ArrayList
     */
    public static ArrayList<ChatColor> getColorList() {
        return new ArrayList<>(colors.values());
    }

    /**
     * clear all colors
     */
    public static void clearColorList() {
        colors.clear();
    }

    /**
     * @param group to delete
     */
    public static void removeColor(String group) {
        colors.remove(group);
    }
}
