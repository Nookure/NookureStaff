package com.nookure.staff.paper.command;

import com.google.inject.Inject;
import com.nookure.core.inv.paper.PaperNookureInventoryEngine;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.command.Command;
import com.nookure.staff.api.command.CommandData;
import com.nookure.staff.api.command.CommandSender;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.paper.PaperPlayerWrapper;
import com.nookure.staff.paper.inventory.InventoryList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandData(
        name = "player-list",
        permission = Permissions.PLAYER_LIST_PERMISSION,
        description = "List all the players on the server.")
public class PlayerListCommand extends Command {
    @Inject
    private AtomicReference<PaperNookureInventoryEngine> engine;

    @Inject
    private PlayerWrapperManager<Player> wrapper;

    @Inject
    private ConfigurationContainer<BukkitMessages> messages;

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args) {
        if (sender instanceof PaperPlayerWrapper player) {
            if (args.isEmpty()) {
                engine.get()
                        .openAsync(
                                player.getPlayer(),
                                InventoryList.PLAYER_LIST,
                                "players",
                                wrapper.stream().toList(),
                                "page",
                                1,
                                "staff",
                                player);
                return;
            }

            if (!sender.hasPermission(Permissions.PLAYER_ACTIONS_PERMISSION)) {
                sender.sendMiniMessage(messages.get().noPermission());
                return;
            }

            String value = args.getFirst();
            UUID uuid;

            try {
                uuid = UUID.fromString(value);
            } catch (IllegalArgumentException e) {
                Player p = Bukkit.getPlayer(value);

                if (p == null) {
                    sender.sendMiniMessage(messages.get().playerNotFound());
                    return;
                }

                uuid = p.getUniqueId();
            }

            engine.get().openAsync(player.getPlayer(), InventoryList.PLAYER_ACTIONS, "uuid", uuid.toString());
        }
    }
}
