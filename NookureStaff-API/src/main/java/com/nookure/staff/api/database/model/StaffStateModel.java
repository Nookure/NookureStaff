package com.nookure.staff.api.database.model;

import com.google.auto.value.AutoBuilder;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * The StaffStateModel class.
 * <p>
 * This class is used to represent the state of a staff member.
 * It contains the UUID of the player, whether the player is in staff mode, and whether the player is vanished.
 * This class is immutable, in order to change the state of a player, a new instance of the StaffStateModel must be created.
 */
public record StaffStateModel(@NotNull UUID uuid, boolean staffMode, boolean vanished, boolean staffChatEnabled) {
    /**
     * Constructor for the StaffStateModel.
     *
     * @param uuid The UUID of the player.
     */
    public StaffStateModel(UUID uuid) {
        this(uuid, false, false, false);
    }

    /**
     * Builder for the StaffStateModel.
     */
    @AutoBuilder(ofClass = StaffStateModel.class)
    public abstract static class Builder {
        /**
         * Returns a new instance of the StaffStateModel Builder.
         *
         * @return A new instance of the StaffStateModel Builder.
         */
        public static Builder builder() {
            return new AutoBuilder_StaffStateModel_Builder();
        }

        /**
         * Returns a new instance of the StaffStateModel Builder.
         *
         * @param staffStateModel The StaffStateModel to copy.
         * @return A new instance of the StaffStateModel Builder.
         */
        public static Builder builder(StaffStateModel staffStateModel) {
            return builder()
                    .uuid(staffStateModel.uuid())
                    .staffMode(staffStateModel.staffMode())
                    .staffChatEnabled(staffStateModel.staffChatEnabled())
                    .vanished(staffStateModel.vanished());
        }

        /**
         * Sets the UUID of the player.
         *
         * @param uuid The UUID of the player.
         * @return The Builder instance.
         */
        public abstract Builder uuid(@NotNull UUID uuid);

        /**
         * Sets whether the player is in staff mode.
         *
         * @param staffMode Whether the player is in staff mode.
         * @return The Builder instance.
         */
        public abstract Builder staffMode(boolean staffMode);

        /**
         * Sets whether the player is vanished.
         *
         * @param vanished Whether the player is vanished.
         * @return The Builder instance.
         */
        public abstract Builder vanished(boolean vanished);

        /**
         * Sets whether the player is vanished.
         *
         * @param staffChatEnabled Whether the player is vanished.
         * @return The Builder instance.
         */
        public abstract Builder staffChatEnabled(boolean staffChatEnabled);

        /**
         * Builds the StaffStateModel.
         *
         * @return The StaffStateModel.
         */
        public abstract StaffStateModel build();
    }

    public static Builder builder() {
        return Builder.builder();
    }

    public static Builder builder(StaffStateModel staffStateModel) {
        return Builder.builder(staffStateModel);
    }

    public UUID uuid() {
        return uuid;
    }

    public boolean staffMode() {
        return staffMode;
    }

    public boolean vanished() {
        return vanished;
    }

    @Override
    public String toString() {
        return "StaffStateModel{" + "uuid="
                + uuid + ", staffMode="
                + staffMode + ", vanished="
                + vanished + ", staffChatEnabled="
                + staffChatEnabled + '}';
    }
}
