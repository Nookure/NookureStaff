package com.nookure.staff.paper.extension.staff;

import com.google.inject.Inject;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.annotation.staff.StaffChatAsDefaultBool;
import com.nookure.staff.api.annotation.staff.StaffModeBool;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitConfig;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.database.model.StaffStateModel;
import com.nookure.staff.api.database.repository.StaffStateRepository;
import com.nookure.staff.api.event.server.BroadcastMessageExcept;
import com.nookure.staff.api.event.staff.StaffModeDisabledEvent;
import com.nookure.staff.api.event.staff.StaffModeEnabledEvent;
import com.nookure.staff.api.extension.StaffPlayerExtension;
import com.nookure.staff.api.extension.staff.StaffModeExtension;
import com.nookure.staff.api.item.StaffItem;
import com.nookure.staff.api.manager.StaffItemsManager;
import com.nookure.staff.api.messaging.EventMessenger;
import com.nookure.staff.paper.StaffPaperPlayerWrapper;
import com.nookure.staff.paper.data.ServerStaffModeData;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PaperStaffModeExtension extends StaffModeExtension {
    private final EventMessenger eventMessenger;
    private final ConfigurationContainer<BukkitMessages> messages;
    private final ConfigurationContainer<BukkitConfig> config;
    private final Logger logger;
    private final StaffStateRepository staffStateRepository;
    private final StaffPaperPlayerWrapper player;
    private final NookureStaff nookPlugin;
    private final AtomicReference<ServerStaffModeData> serverStaffModeData;
    private final AtomicReference<StaffStateModel> staffStateModel;
    private final AtomicBoolean staffMode;
    private final Map<Integer, StaffItem> items = new HashMap<>();
    private final StaffItemsManager itemsManager;

    @Inject
    public PaperStaffModeExtension(
            @NotNull final StaffPlayerWrapper player,
            @NotNull final Logger logger,
            @NotNull final EventMessenger eventMessenger,
            @NotNull final ConfigurationContainer<BukkitMessages> messages,
            @NotNull final ConfigurationContainer<BukkitConfig> config,
            @NotNull final StaffStateRepository staffStateRepository,
            @NotNull final NookureStaff nookPlugin,
            @NotNull final AtomicReference<ServerStaffModeData> serverStaffModeData,
            @NotNull final AtomicReference<StaffStateModel> staffStateModel,
            @NotNull @StaffChatAsDefaultBool final AtomicBoolean staffChatAsDefault,
            @NotNull @StaffModeBool final AtomicBoolean staffMode,
            @NotNull final StaffItemsManager itemsManager) {
        super(player);
        this.player = (StaffPaperPlayerWrapper) player;
        this.logger = logger;
        this.eventMessenger = eventMessenger;
        this.messages = messages;
        this.config = config;
        this.serverStaffModeData = serverStaffModeData;
        this.staffStateModel = staffStateModel;
        this.nookPlugin = nookPlugin;
        this.staffStateRepository = staffStateRepository;
        this.staffMode = staffMode;
        this.itemsManager = itemsManager;
    }

    @Override
    public void enableStaffMode(boolean silentJoin) {
        long time = System.currentTimeMillis();

        player.enablePlayerPerks();

        if (!silentJoin) {
            saveLocation();
            saveInventory();
        }

        setItems();
        player.sendMiniMessage(messages.get().staffMode.toggledOn());
        writeStaffModeState(true);

        if (config.get().staffMode.enableVanishOnStaffEnable()) {
            player.enableVanish(silentJoin);
            player.writeVanishState(true);
        }

        eventMessenger.publish(player, new StaffModeEnabledEvent(player.getUniqueId()));
        eventMessenger.publish(
                player,
                new BroadcastMessageExcept(
                        messages.get().staffMode.toggledOnOthers().replace("{player}", player.getName()),
                        player.getUniqueId()));

        try {
            player.getExtensions().values().forEach(StaffPlayerExtension::onStaffModeEnabled);
        } catch (Exception e) {
            logger.severe("An error occurred while enabling staff mode for %s: %s", player.getName(), e.getMessage());
            if (nookPlugin.isDebug()) {
                logger.severe(e);
            }
        }

        logger.debug("Staff mode enabled for %s in %dms", player.getName(), System.currentTimeMillis() - time);
    }

    @Override
    public void disableStaffMode() {
        long time = System.currentTimeMillis();
        player.disablePlayerPerks();
        restoreInventory();
        if (config.get().staffMode.teleportToPreviousLocation()) restoreLocation();
        player.sendMiniMessage(messages.get().staffMode.toggledOff());
        writeStaffModeState(false);

        if (config.get().staffMode.disableVanishOnStaffDisable()) {
            player.disableVanish(false);
            player.writeVanishState(false);
        }

        eventMessenger.publish(player, new StaffModeDisabledEvent(player.getUniqueId()));
        eventMessenger.publish(
                player,
                new BroadcastMessageExcept(
                        messages.get().staffMode.toggledOffOthers().replace("{player}", player.getName()),
                        player.getUniqueId()));

        try {
            player.getExtensions().values().forEach(StaffPlayerExtension::onStaffModeDisabled);
        } catch (Exception e) {
            logger.severe("An error occurred while disabling staff mode for %s: %s", player.getName(), e.getMessage());
            if (nookPlugin.isDebug()) {
                logger.severe(e);
            }
        }

        logger.debug("Staff mode disabled for %s in %dms", player.getName(), System.currentTimeMillis() - time);
    }

    @Override
    public void checkStaffMode() {
        if (serverStaffModeData.get() == null) {
            serverStaffModeData.set(ServerStaffModeData.read(nookPlugin, player));
        }

        if (staffStateModel.get() == null) {
            logger.severe("StaffModeData is null for %s", player.getName());
            return;
        }

        if (serverStaffModeData.get().record().staffMode()) {
            clearInventory();
            enableStaffMode(true);
        }

        if (staffStateModel.get().staffMode()) {
            if (!player.isInStaffMode()) {
                saveInventory();
                saveLocation();
                enableStaffMode(true);
            }
        }

        logger.debug("StaffDataModel state: %s", staffStateModel.get());
        player.setStaffChatAsDefault(staffStateModel.get().staffChatEnabled());
    }

    @Override
    public void writeStaffModeState(boolean state) {
        staffStateRepository.fromUUIDAsync(player.getUniqueId()).thenAccept((model) -> {
            if (model == null) {
                return;
            }

            staffStateModel.set(StaffStateModel.builder(model).staffMode(state).build());

            staffStateRepository.saveOrUpdateModel(staffStateModel.get());
            logger.debug("Staff mode state for %s has been set to %s on the database", player.getName(), state);
        });

        serverStaffModeData.get().record().staffMode(state);
        serverStaffModeData.get().write();

        staffMode.set(state);
    }

    @Override
    public void toggleStaffMode(boolean silentJoin) {
        if (!staffMode.get()) enableStaffMode(silentJoin);
        else disableStaffMode();
    }

    @Override
    public boolean isStaffMode() {
        return staffMode.get();
    }

    @Override
    public void setItems() {
        if (items.isEmpty()) {
            itemsManager.getItems().forEach((identifier, item) -> {
                if (item.getPermission() != null && !player.hasPermission(item.getPermission())) return;

                items.put(item.getSlot(), item);
            });
        }

        items.forEach((identifier, item) -> item.setItem(player.getPlayer()));
    }

    @Override
    public @NotNull Map<Integer, StaffItem> getItems() {
        return items;
    }

    @Override
    public void saveInventory() {
        if (serverStaffModeData.get() == null) {
            serverStaffModeData.set(ServerStaffModeData.read(nookPlugin, player));
        }

        assert serverStaffModeData.get() != null;

        serverStaffModeData
                .get()
                .record()
                .playerInventory(player.getPlayer().getInventory().getContents());
        serverStaffModeData
                .get()
                .record()
                .playerInventoryArmor(player.getPlayer().getInventory().getArmorContents());

        serverStaffModeData.get().write();

        clearInventory();
    }

    @Override
    public void clearInventory() {
        player.getPlayer().getInventory().clear();
        player.getPlayer().getInventory().setArmorContents(new ItemStack[0]);
    }

    @Override
    public void restoreInventory() {
        if (serverStaffModeData.get() == null) {
            serverStaffModeData.set(ServerStaffModeData.read(nookPlugin, player));
        }

        assert serverStaffModeData.get() != null;

        player.getPlayer()
                .getInventory()
                .setContents(serverStaffModeData.get().record().playerInventory());
        player.getPlayer()
                .getInventory()
                .setArmorContents(serverStaffModeData.get().record().playerInventoryArmor());

        serverStaffModeData.get().write();
    }

    @Override
    public void saveLocation() {
        serverStaffModeData.get().record().enabledLocation(player.getPlayer().getLocation());
        serverStaffModeData.get().write();
    }

    @Override
    public void restoreLocation() {
        Location location = serverStaffModeData.get().record().enabledLocation();
        if (location == null) return;
        player.getPlayer().teleport(location);
    }
}
