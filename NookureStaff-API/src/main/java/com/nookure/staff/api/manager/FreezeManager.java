package com.nookure.staff.api.manager;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.inject.Singleton;
import com.nookure.staff.api.PlayerWrapper;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class FreezeManager {
    private final BiMap<UUID, FreezeContainer> freezeContainers = HashBiMap.create();

    /**
     * Freezes a player.
     *
     * @param target the player
     * @param staff  the staff member
     * @param time   the time in milliseconds
     */
    public void freeze(@NotNull PlayerWrapper target, @NotNull PlayerWrapper staff, long time) {
        requireNonNull(target, "Target cannot be null");
        requireNonNull(staff, "Staff cannot be null");

        addFreezeContainer(target.getUniqueId(), new FreezeContainer(staff.getUniqueId(), target.getUniqueId(), time));
    }

    /**
     * Gets the freeze container of a player by its unique id.
     *
     * @param target    the unique id of the player
     * @param container the freeze container
     */
    public void addFreezeContainer(@NotNull UUID target, @NotNull FreezeContainer container) {
        requireNonNull(target, "Target cannot be null");
        requireNonNull(container, "Container cannot be null");

        synchronized (freezeContainers) {
            freezeContainers.put(target, container);
        }
    }

    /**
     * Gets the freeze container of a player by its unique id.
     *
     * @param target the unique id of the player
     * @return the freeze container
     */
    @NotNull public Optional<FreezeContainer> getFreezeContainer(@NotNull UUID target) {
        requireNonNull(target, "Target cannot be null");

        synchronized (freezeContainers) {
            return Optional.ofNullable(freezeContainers.get(target));
        }
    }

    /**
     * Remove the freeze container of a player by its unique id.
     *
     * @param target the unique id of the player
     */
    public void removeFreezeContainer(@NotNull UUID target) {
        requireNonNull(target, "Target cannot be null");

        synchronized (freezeContainers) {
            freezeContainers.remove(target);
        }
    }

    /**
     * Checks if a player is frozen.
     *
     * @param target the unique id of the player
     * @return true if the player is frozen
     */
    public boolean isFrozen(@NotNull UUID target) {
        requireNonNull(target, "Target cannot be null");

        synchronized (freezeContainers) {
            return freezeContainers.containsKey(target);
        }
    }

    /**
     * Checks if a player is frozen.
     *
     * @param target the player
     * @return true if the player is frozen
     */
    public boolean isFrozen(@NotNull Player target) {
        requireNonNull(target, "Target cannot be null");

        synchronized (freezeContainers) {
            return freezeContainers.containsKey(target.getUniqueId());
        }
    }

    /**
     * Checks if a player is frozen.
     *
     * @param target the player wrapper
     * @return true if the player is frozen
     */
    public boolean isFrozen(@NotNull PlayerWrapper target) {
        requireNonNull(target, "Target cannot be null");

        synchronized (freezeContainers) {
            return freezeContainers.containsKey(target.getUniqueId());
        }
    }

    /**
     * Gets the freeze containers as a stream
     *
     * @return a stream of freeze containers
     */
    public Stream<FreezeContainer> stream() {
        return freezeContainers.values().stream();
    }

    public Iterator<FreezeContainer> iterator() {
        return freezeContainers.values().iterator();
    }

    public static final class FreezeContainer {
        private final UUID staff;
        private final UUID target;
        private long timeLeft;
        private boolean hasTalked;

        public FreezeContainer(UUID staff, UUID target, long timeLeft) {
            this.staff = staff;
            this.target = target;
            this.timeLeft = timeLeft;
            this.hasTalked = false;
        }

        public UUID staff() {
            return staff;
        }

        public UUID target() {
            return target;
        }

        public long timeLeft() {
            return timeLeft;
        }

        public boolean hasTalked() {
            return hasTalked;
        }

        public void setTimeLeft(long timeLeft) {
            this.timeLeft = timeLeft;
        }

        public void setHasTalked(boolean hasTalked) {
            this.hasTalked = hasTalked;
        }
    }
}
