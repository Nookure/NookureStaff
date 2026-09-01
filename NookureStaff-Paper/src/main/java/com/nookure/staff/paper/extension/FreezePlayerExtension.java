package com.nookure.staff.paper.extension;

import static java.util.Objects.requireNonNull;

import com.google.inject.Inject;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitConfig;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.event.server.BroadcastMessage;
import com.nookure.staff.api.extension.StaffPlayerExtension;
import com.nookure.staff.api.manager.FreezeManager;
import com.nookure.staff.api.messaging.EventMessenger;
import com.nookure.staff.api.util.ServerUtils;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class FreezePlayerExtension extends StaffPlayerExtension {
    private final StaffPlayerWrapper player;
    private final FreezeManager freezeManager;
    private final ConfigurationContainer<BukkitConfig> config;
    private final ConfigurationContainer<BukkitMessages> messages;
    private final EventMessenger eventMessenger;
    private final ServerUtils serverUtils;

    @Inject
    public FreezePlayerExtension(
            @NotNull final StaffPlayerWrapper player,
            @NotNull final FreezeManager freezeManager,
            @NotNull final ConfigurationContainer<BukkitConfig> config,
            @NotNull final ConfigurationContainer<BukkitMessages> messages,
            @NotNull final EventMessenger eventMessenger,
            @NotNull final ServerUtils serverUtils) {
        super(player);
        this.player = player;
        this.freezeManager = freezeManager;
        this.config = config;
        this.messages = messages;
        this.eventMessenger = eventMessenger;
        this.serverUtils = serverUtils;
    }

    public void freezePlayer(PlayerWrapper target) {
        final long configTime = config.get().freeze.freezeTimer();

        if (configTime == -1) {
            freezeManager.freeze(target, player, -1);
        } else {
            freezeManager.freeze(
                    target,
                    player,
                    System.currentTimeMillis() + config.get().freeze.freezeTimer());
        }

        target.sendMiniMessage(messages.get().freeze.frozenMessage());

        eventMessenger.publish(
                player,
                new BroadcastMessage(
                        messages.get()
                                .freeze
                                .staffFrozenMessage()
                                .replace("{player}", target.getName())
                                .replace("{staff}", player.getName()),
                        Permissions.STAFF_FREEZE));
    }

    public void unfreezePlayer(PlayerWrapper target) {
        freezeManager.removeFreezeContainer(target.getUniqueId());

        target.sendMiniMessage(messages.get().freeze.unfrozenMessage());

        eventMessenger.publish(
                player,
                new BroadcastMessage(
                        messages.get()
                                .freeze
                                .staffUnfrozenMessage()
                                .replace("{player}", target.getName())
                                .replace("{staff}", player.getName()),
                        Permissions.STAFF_FREEZE));
    }

    public void executeFreezeCommands(@NotNull PlayerWrapper target, @NotNull String name) {
        requireNonNull(target, "Target cannot be null");
        requireNonNull(name, "Name cannot be null");

        config.get().freeze.commands().forEach(command -> execute(target, command, name));
    }

    public void pauseFreeze(@NotNull final PlayerWrapper target, @NotNull final PlayerWrapper staff) {
        freezeManager
                .getFreezeContainer(target.getUniqueId())
                .ifPresentOrElse(
                        container -> {
                            container.setTimeLeft(-1);
                            target.sendMiniMessage(messages.get().freeze.theStaffHasPausedTheTimer());

                            eventMessenger.publish(
                                    staff,
                                    new BroadcastMessage(
                                            messages.get()
                                                    .freeze
                                                    .theTimerHasBeenPausedFor()
                                                    .replace("{player}", target.getName())
                                                    .replace("{staff}", staff.getName()),
                                            Permissions.STAFF_FREEZE));
                        },
                        () -> target.sendMiniMessage(messages.get().freeze.playerNotOnline()));
    }

    public void execute(@NotNull PlayerWrapper player, @NotNull String command, @NotNull String name) {
        requireNonNull(player, "Player cannot be null");
        requireNonNull(command, "Command cannot be null");
        requireNonNull(name, "Name cannot be null");

        if (command.contains("{player}")) {
            command = command.replace("{player}", name);
        }

        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        if (command.startsWith("proxy:")) {
            command = command.substring(6);

            if (command.startsWith(" ")) command = command.substring(1);
            serverUtils.sendCommandToProxy(command, player);
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
