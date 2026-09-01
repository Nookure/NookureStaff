package com.nookure.staff.api.util;

import java.io.Serializable;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a location serializable object.
 */
public final class LocationWrapper implements Serializable {
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private final String world;

    /**
     * Constructor for LocationWrapper.
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     * @param z     the z coordinate
     * @param yaw   the yaw
     * @param pitch the pitch
     * @param world the world
     */
    public LocationWrapper(double x, double y, double z, float yaw, float pitch, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

    /**
     * Constructor for LocationWrapper.
     *
     * @param location the location
     */
    public LocationWrapper(org.bukkit.Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        this.world = location.getWorld().getName();
    }

    @Nullable public static org.bukkit.Location toLocation(@Nullable LocationWrapper locationWrapper) {
        if (locationWrapper == null) {
            return null;
        }

        org.bukkit.World world = org.bukkit.Bukkit.getWorld(locationWrapper.getWorld());

        if (world == null) {
            return null;
        }

        return new org.bukkit.Location(
                org.bukkit.Bukkit.getWorld(locationWrapper.getWorld()),
                locationWrapper.getX(),
                locationWrapper.getY(),
                locationWrapper.getZ(),
                locationWrapper.getYaw(),
                locationWrapper.getPitch());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public String getWorld() {
        return world;
    }
}
