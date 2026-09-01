package com.nookure.staff.paper.command.staff;

import com.google.inject.Inject;
import com.nookure.core.inv.paper.PaperNookureInventoryEngine;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.command.CommandData;
import com.nookure.staff.api.command.StaffCommand;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.api.util.transformer.PlayerTransformer;
import com.nookure.staff.paper.StaffPaperPlayerWrapper;
import com.nookure.staff.paper.inventory.InventoryList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandData(name = "list", description = "List all staff members", permission = "nookurestaff.staff.list")
public class StaffListCommand extends StaffCommand {
    private final AtomicReference<PaperNookureInventoryEngine> engine;
    private final PlayerWrapperManager<Player> playerWrapperManager;

    @Inject
    public StaffListCommand(
            @NotNull final PlayerTransformer transformer,
            @NotNull final ConfigurationContainer<BukkitMessages> messages,
            @NotNull final AtomicReference<PaperNookureInventoryEngine> engine,
            @NotNull final PlayerWrapperManager<Player> playerWrapperManager) {
        super(transformer, messages);
        this.engine = engine;
        this.playerWrapperManager = playerWrapperManager;
    }

    @Override
    protected void onStaffCommand(
            @NotNull StaffPlayerWrapper sender, @NotNull String label, @NotNull List<String> args) {
        if (sender instanceof StaffPaperPlayerWrapper staff) {
            engine.get()
                    .openAsync(
                            staff.getPlayer(),
                            InventoryList.STAFF_LIST,
                            "player",
                            staff,
                            "players",
                            playerWrapperManager.stream()
                                    .filter(player -> player instanceof StaffPaperPlayerWrapper)
                                    .toList(),
                            "page",
                            1);
        }
    }
}
