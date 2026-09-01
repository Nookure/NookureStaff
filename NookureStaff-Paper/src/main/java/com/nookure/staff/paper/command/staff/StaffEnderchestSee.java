package com.nookure.staff.paper.command.staff;

import com.google.inject.Inject;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.command.CommandData;
import com.nookure.staff.api.command.StaffCommand;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.util.transformer.PlayerTransformer;
import com.nookure.staff.paper.StaffPaperPlayerWrapper;
import com.nookure.staff.paper.inventory.player.PlayerEnderchestInventory;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandData(
        name = "enderchest",
        description = "View the enderchest inventory of another player",
        permission = Permissions.STAFF_ENDERCHEST)
public class StaffEnderchestSee extends StaffCommand {
    private final ConfigurationContainer<BukkitMessages> messages;

    @Inject
    public StaffEnderchestSee(
            @NotNull final PlayerTransformer transformer,
            @NotNull final ConfigurationContainer<BukkitMessages> messages) {
        super(transformer, messages);
        this.messages = messages;
    }

    @Override
    protected void onStaffCommand(
            @NotNull StaffPlayerWrapper sender, @NotNull String label, @NotNull List<String> args) {
        if (args.isEmpty()) {
            sender.sendMiniMessage(messages.get().staffMode.invseeEnderchestCommandUsage());
            return;
        }

        String targetName = args.getFirst();

        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            sender.sendMiniMessage(messages.get().playerNotFound());
            return;
        }

        if (sender instanceof StaffPaperPlayerWrapper player) {
            if (player.hasPermission(Permissions.STAFF_ENDERCHEST_MODIFY)) {
                player.getPlayer().openInventory(target.getEnderChest());
            } else {
                player.getPlayer().openInventory(new PlayerEnderchestInventory(target).getInventory());
            }
        }
    }
}
