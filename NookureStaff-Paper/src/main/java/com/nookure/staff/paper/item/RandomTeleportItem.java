package com.nookure.staff.paper.item;

import com.google.inject.Inject;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.config.bukkit.ItemsConfig;
import com.nookure.staff.api.item.ExecutableItem;
import com.nookure.staff.api.item.Items;
import com.nookure.staff.api.item.StaffItem;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RandomTeleportItem extends StaffItem implements ExecutableItem {
    private final Random random = new Random();

    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Inject
    private ConfigurationContainer<BukkitMessages> messages;

    @Inject
    public RandomTeleportItem(ConfigurationContainer<ItemsConfig> itemsConfig) {
        super(itemsConfig.get().staffItems.getItems().get(Items.RANDOM_PLAYER_TELEPORT.toString()));
    }

    @Override
    public void click(@NotNull PlayerWrapper player) {
        Stream<PlayerWrapper> stream = playerWrapperManager.stream().filter(p -> !(p instanceof StaffPlayerWrapper));

        List<PlayerWrapper> players = stream.toList();

        if (players.isEmpty()) {
            player.sendMiniMessage(messages.get().staffMode.noPlayersOnline());
            return;
        }

        PlayerWrapper randomPlayer = players.get(random.nextInt(players.size()));

        player.teleport(randomPlayer);

        player.sendMiniMessage(messages.get().staffMode.teleportingTo(), "player", randomPlayer.getName());
    }
}
