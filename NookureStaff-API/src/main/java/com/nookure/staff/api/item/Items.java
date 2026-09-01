package com.nookure.staff.api.item;

public enum Items {
    FREEZE,
    RANDOM_PLAYER_TELEPORT,
    ENDER_CHEST,
    THRU,
    INVSEE,
    VANISH,
    NIGHT_VISION;

    public String toString() {
        return this.name().toLowerCase();
    }
}
