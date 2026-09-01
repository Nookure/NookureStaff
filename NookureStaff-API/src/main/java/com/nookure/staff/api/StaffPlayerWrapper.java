package com.nookure.staff.api;

import com.nookure.staff.api.extension.StaffPlayerExtension;
import com.nookure.staff.api.item.StaffItem;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface StaffPlayerWrapper extends PlayerWrapper {
    /**
     * toggle the staff mode
     * and save the inventory.
     */
    default void toggleStaffMode() {
        toggleStaffMode(false);
    }

    /**
     * Toggle the staff mode.
     *
     * @param silentJoin if true, the inventory will be saved
     *                   and the player will enter silently.
     */
    void toggleStaffMode(boolean silentJoin);

    /**
     * Check if the player is in staff mode.
     *
     * @return true if the player is in staff mode
     */
    boolean isInStaffMode();

    /**
     * Toggle the vanish mode.
     */
    void toggleVanish();

    /**
     * Check if the player is in vanish mode.
     *
     * @return true if the player is in vanish mode
     * @see #toggleVanish()
     */
    boolean isInVanish();

    /**
     * Check if the player is in staff chat.
     *
     * @return true if the player is in staff chat mode
     * @see #setStaffChatAsDefault(boolean) ()
     */
    boolean isStaffChatAsDefault();

    /**
     * Set the player's staff chat mode.
     *
     * @param staffChatAsDefault if true, the player will be in staff chat mode
     *                           by default
     * @param saveInDB           if true, the player's staff chat mode will be saved
     */
    void setStaffChatAsDefault(boolean staffChatAsDefault, boolean saveInDB);

    /**
     * Set the player's staff chat mode.
     *
     * @param staffChatAsDefault if true, the player will be in staff chat mode
     */
    default void setStaffChatAsDefault(boolean staffChatAsDefault) {
        setStaffChatAsDefault(staffChatAsDefault, true);
    }

    /**
     * Reload/load the player's items.
     */
    void setItems();

    /**
     * Save the player's items.
     */
    void saveInventory();

    /**
     * Enable the vanish mode.
     *
     * @param silent if true, the message will not be sent
     */
    void enableVanish(boolean silent);

    /**
     * Disable the vanish mode.
     *
     * @param silent if true, the message will not be sent
     */
    void disableVanish(boolean silent);

    /**
     * Enable the vanish mode.
     * The message will be sent.
     */
    default void enableVanish() {
        enableVanish(false);
    }

    /**
     * Disable the vanish mode.
     * The message will be sent.
     */
    default void disableVanish() {
        disableVanish(false);
    }

    /**
     * Restore the player's items.
     */
    void restoreInventory();

    /**
     * Clear the player's items.
     */
    void clearInventory();

    /**
     * Enable the player's perks.
     */
    void disablePlayerPerks();

    /**
     * Disable the player's perks.
     */
    void enablePlayerPerks();

    /**
     * Toggle the night vision.
     */
    void toggleNightVision();

    /**
     * Check if the player is in staff mode or vanish mode.
     *
     * @return true if the player is in staff mode or vanish mode
     */
    default boolean isStaffModeOrVanish() {
        return isInStaffMode() || isInVanish();
    }

    /**
     * Return the player's staff mode extension.
     *
     * @param extension the extension class that you want to get
     * @return the extension
     */
    <T extends StaffPlayerExtension> Optional<T> getExtension(Class<T> extension);

    /**
     * Get the player's items.
     *
     * @return the player's items
     */
    @NotNull Map<Integer, StaffItem> getItems();

    @NotNull Map<Class<? extends StaffPlayerExtension>, StaffPlayerExtension> getExtensions();
}
