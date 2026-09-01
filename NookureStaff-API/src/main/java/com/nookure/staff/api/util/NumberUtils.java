package com.nookure.staff.api.util;

public abstract class NumberUtils {
    public static Long parseToMillis(String s) {
        long parsed = 0;
        try {
            parsed = Long.parseLong(s.substring(0, s.length() - 1));
        } catch (NumberFormatException e) {
            parsed = Long.parseLong(s.substring(0, s.length() - 2));
        } catch (Exception e) {
            throw new NumberFormatException("Invalid time");
        }

        if (s.endsWith("s")) {
            return parsed * 1000;
        }

        if (s.endsWith("m")) {
            return parsed * 1000 * 60;
        }

        if (s.endsWith("h")) {
            return parsed * 1000 * 60 * 60;
        }

        if (s.endsWith("d") || s.endsWith("D")) {
            return parsed * 1000 * 60 * 60 * 24;
        }

        if (s.endsWith("w")) {
            return parsed * 1000 * 60 * 60 * 24 * 7;
        }

        if (s.endsWith("M")) {
            return parsed * 1000 * 60 * 60 * 24 * 30;
        }

        if (s.endsWith("y") || s.endsWith("a") || s.endsWith("A")) {
            return parsed * 1000 * 60 * 60 * 24 * 365;
        }

        if (s.endsWith("mo") || s.endsWith("MO")) {
            return parsed * 1000 * 60 * 60 * 24 * 30;
        }

        return Long.parseLong(s);
    }
}
