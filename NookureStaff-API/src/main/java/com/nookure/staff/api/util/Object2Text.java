package com.nookure.staff.api.util;

public interface Object2Text {
    /**
     * Replace the text with all the objects
     *
     * @param text    The text to replace
     * @param objects The objects to get the text replacements from
     * @return The replaced text
     */
    static String replaceText(String text, Object2Text... objects) {
        for (Object2Text object : objects) {
            text = object.replaceText(text);
        }

        return text;
    }

    /**
     * Replace the text with the object
     *
     * @param text The text to replace
     * @return The replaced text
     */
    String replaceText(String text);
}
