package com.nookure.staff.paper.listener.staff.items;

import static java.util.Objects.requireNonNull;

import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.item.StaffItem;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public abstract class CommonPlayerInteraction {
    private final HashMap<UUID, Long> lastClick = new HashMap<>();

    public Optional<StaffItem> getItem(@NotNull ItemStack item, @NotNull StaffPlayerWrapper player) {
        requireNonNull(item, "Item cannot be null");
        requireNonNull(player, "Player cannot be null");

        if (!item.hasItemMeta()) {
            return Optional.empty();
        }

        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

        if (container.has(StaffItem.key, PersistentDataType.INTEGER)) {
            return Optional.ofNullable(player.getItems().get(container.get(StaffItem.key, PersistentDataType.INTEGER)));
        }

        return Optional.empty();
    }

    public synchronized boolean canUseItem(@NotNull StaffPlayerWrapper wrapper) {
        requireNonNull(wrapper, "Wrapper cannot be null");
        return canUseItem(wrapper.getUniqueId());
    }

    public synchronized boolean canUseItem(@NotNull UUID uuid) {
        requireNonNull(uuid, "UUID cannot be null");
        if (lastClick.containsKey(uuid)) {
            if (System.currentTimeMillis() - lastClick.get(uuid) < 500) {
                removeOldClicks();
                return false;
            }
        }

        lastClick.put(uuid, System.currentTimeMillis());
        removeOldClicks();
        return true;
    }

    private void removeOldClicks() {
        lastClick.entrySet().removeIf(entry -> System.currentTimeMillis() - entry.getValue() > 500);
    }
}
