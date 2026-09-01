package com.nookure.staff.paper;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitConfig;
import com.nookure.staff.api.model.PlayerModel;
import com.nookure.staff.api.state.PlayerState;
import com.nookure.staff.api.state.WrapperState;
import com.nookure.staff.api.util.DefaultFontInfo;
import com.nookure.staff.api.util.Scheduler;
import com.nookure.staff.api.util.ServerUtils;
import com.nookure.staff.api.util.TextUtils;
import io.ebean.Database;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PaperPlayerWrapper implements PlayerWrapper {
    protected final JavaPlugin plugin;
    protected final NookureStaff nookPlugin;
    protected final ConfigurationContainer<BukkitConfig> config;
    protected final AtomicReference<Database> db;
    protected final Logger logger;
    protected final Player player;
    protected final Scheduler scheduler;
    protected final WrapperState state = new WrapperState();
    protected PlayerModel playerModel;

    @Inject
    public PaperPlayerWrapper(
            @NotNull final JavaPlugin plugin,
            @NotNull final NookureStaff nookPlugin,
            @NotNull final Logger logger,
            @NotNull final ConfigurationContainer<BukkitConfig> config,
            @NotNull final Scheduler scheduler,
            @NotNull final AtomicReference<Database> db,
            @NotNull @Assisted final Player player,
            @NotNull @Assisted final List<Class<? extends PlayerState>> states) {
        this.plugin = plugin;
        this.nookPlugin = nookPlugin;
        this.logger = logger;
        this.player = player;
        this.config = config;
        this.scheduler = scheduler;
        this.db = db;

        getPlayerModelAsync(player)
                .thenAccept(model -> this.playerModel = model)
                .thenRun(() -> scheduler.sync(() -> addStates(states)));
    }

    // <editor-fold desc="Player Model">
    private CompletableFuture<PlayerModel> getPlayerModelAsync(Player player) {
        return CompletableFuture.supplyAsync(() -> getPlayerModel(player));
    }

    private PlayerModel getPlayerModel(Player p) {
        if (!config.get().modules.isPlayerData()) return null;

        Optional<PlayerModel> optional = db.get()
                .find(PlayerModel.class)
                .where()
                .eq("uuid", p.getUniqueId())
                .findOneOrEmpty();

        if (optional.isPresent()) {
            PlayerModel m = updatePlayer(optional.get(), p);
            m.update();

            return m;
        }

        PlayerModel player = updatePlayer(new PlayerModel(), p);
        player.setUuid(p.getUniqueId());
        player.setFirstLogin(Instant.now());

        try {
            player.setFirstIp(
                    Objects.requireNonNull(p.getAddress()).getAddress().getHostAddress());
        } catch (NullPointerException e) {
            player.setFirstIp("0.0.0.0");
        }

        player.save();

        return player;
    }

    private PlayerModel updatePlayer(PlayerModel player, Player bukkitPlayer) {
        player.setName(bukkitPlayer.getName());

        try {
            player.setLastIp(Objects.requireNonNull(bukkitPlayer.getAddress())
                    .getAddress()
                    .getHostAddress());
        } catch (NullPointerException e) {
            player.setLastIp("0.0.0.0");
        }

        player.setLastLogin(Instant.now());
        return player;
    }
    // </editor-fold>

    // <editor-fold desc="Constructor parameters parsing">
    private void addStates(List<Class<? extends PlayerState>> states) {
        for (Class<? extends PlayerState> state : states) {
            PlayerState playerState;

            try {
                playerState = state.getConstructor(PlayerWrapper.class).newInstance(this);
            } catch (Exception e) {
                throw new IllegalStateException(
                        String.format("""
                An error occurred while adding the state
                Class-name: %s
                Package: %s
                Error: %s
                """, state.getSimpleName(), state.getPackage(), e.getMessage()), e);
            }

            this.state.setState(playerState);
        }
    }
    // </editor-fold>

    // <editor-fold desc="PaperPlayer and CommandSender impl">
    @Override
    public void sendMessage(@NotNull Component component) {
        if (ServerUtils.isPaper) {
            player.sendMessage(component);
            return;
        }

        player.sendMessage(LegacyComponentSerializer.legacySection().serialize(component));
    }

    @Override
    public void sendActionbar(@NotNull Component component) {
        if (!ServerUtils.isPaper) {
            return;
        }

        player.sendActionBar(component);
    }

    @Override
    public void sendPluginMessage(@NotNull String channel, byte @NotNull [] message) {
        logger.debug("Sending plugin message to " + player.getName() + " on channel " + channel);
        player.sendPluginMessage(plugin, channel, message);
    }

    @Override
    public void teleport(@NotNull PlayerWrapper to) {
        if (ServerUtils.isPaper)
            player.teleportAsync(((PaperPlayerWrapper) to).getPlayer().getLocation());
        else player.teleport(((PaperPlayerWrapper) to).getPlayer().getLocation());
    }

    @Override
    public void sendMiniMessage(@NotNull String message, String... placeholders) {
        if (message.isBlank()) return;

        if (placeholders.length % 2 != 0) {
            throw new IllegalArgumentException("Placeholders must be in pairs");
        }

        for (int i = 0; i < placeholders.length; i += 2) {
            message = message.replace("{" + placeholders[i] + "}", placeholders[i + 1]);
        }

        message = message.replace("{prefix}", nookPlugin.getPrefix());
        message = DefaultFontInfo.centerIfContains(message);
        message = TextUtils.parsePlaceholdersWithPAPI(player, message);

        sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    @Override
    public @NotNull Set<String> getListeningPluginChannels() {
        return player.getListeningPluginChannels();
    }

    @Override
    public int getPing() {
        return player.getPing();
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull Component getDisplayName() {
        if (ServerUtils.isPaper) {
            return player.displayName();
        }

        return Component.text(player.getDisplayName());
    }

    @Override
    public @NotNull String getName() {
        return player.getName();
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @NotNull public Player getPlayer() {
        return player;
    }

    @Override
    public PlayerModel getPlayerModel() {
        if (playerModel == null) {
            throw new IllegalStateException("Player model module is disabled");
        }

        return playerModel;
    }

    @Override
    public void kick(@NotNull String reason) {
        kick(MiniMessage.miniMessage().deserialize(reason));
    }

    @Override
    public void kick(@NotNull Component reason) {
        if (ServerUtils.isPaper) {
            player.kick(reason);
        } else {
            //noinspection deprecation (for legacy servers)
            player.kickPlayer(LegacyComponentSerializer.legacySection().serialize(reason));
        }
    }

    @Override
    public @NotNull WrapperState getState() {
        return state;
    }
    // </editor-fold>
}
