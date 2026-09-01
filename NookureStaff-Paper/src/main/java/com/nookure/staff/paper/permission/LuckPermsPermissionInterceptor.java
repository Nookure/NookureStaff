package com.nookure.staff.paper.permission;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nookure.staff.api.util.transformer.PlayerTransformer;
import com.nookure.staff.paper.bootstrap.StaffBootstrapper;
import java.io.Closeable;
import java.util.Optional;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventSubscription;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class LuckPermsPermissionInterceptor implements Closeable {
    private final EventSubscription<NodeAddEvent> nodeAddEventEventSubscription;
    private final PlayerTransformer playerTransformer;

    private static final String BASE_PERMISSION = "nookure.staff";
    private static final String GROUP_PERMISSION = "group.";

    @Inject
    public LuckPermsPermissionInterceptor(
            @NotNull final StaffBootstrapper bootstrapper, @NotNull final PlayerTransformer playerTransformer) {
        RegisteredServiceProvider<LuckPerms> provider =
                Bukkit.getServicesManager().getRegistration(LuckPerms.class);

        if (provider == null) {
            throw new IllegalStateException("LuckPerms is not installed on the server.");
        }

        LuckPerms luckPerms = provider.getProvider();

        this.playerTransformer = playerTransformer;

        nodeAddEventEventSubscription =
                luckPerms.getEventBus().subscribe(bootstrapper, NodeAddEvent.class, this::onNodeAdd);
    }

    private void onNodeAdd(@NotNull final NodeAddEvent event) {
        if (nookureStaffBasePermissionCheck(event)) return;
        if (fullPermissionGrant(event)) return;
        if (playerAddedToGroupCheck(event)) return;
    }

    public boolean fullPermissionGrant(@NotNull final NodeAddEvent event) {
        if (!event.getNode().getKey().equals("*")) return false;
        if (!(event.getTarget() instanceof User user)) return false;
        if (!event.getNode().getValue()) playerTransformer.staff2player(user.getUniqueId());

        playerTransformer.player2Staff(user.getUniqueId());
        return true;
    }

    public boolean nookureStaffBasePermissionCheck(@NotNull final NodeAddEvent event) {
        if (!event.getNode().getKey().equals(BASE_PERMISSION)) return false;
        if (!(event.getTarget() instanceof User user)) return false;
        if (!event.getNode().getValue()) playerTransformer.staff2player(user.getUniqueId());
        else playerTransformer.player2Staff(user.getUniqueId());
        return true;
    }

    public boolean playerAddedToGroupCheck(@NotNull final NodeAddEvent event) {
        if (!event.getNode().getKey().startsWith(GROUP_PERMISSION)) return false;
        if (!(event.getTarget() instanceof User user)) return false;
        if (!event.getNode().getValue()) return false;

        Optional<Group> group = user.getInheritedGroups(user.getQueryOptions()).stream()
                .filter(g -> g.getName().equals(event.getNode().getKey().substring(GROUP_PERMISSION.length())))
                .findFirst();

        if (group.isEmpty()) return false;

        if (group.get().getNodes().stream().anyMatch(node -> node.getKey().equals(BASE_PERMISSION))) {
            playerTransformer.player2Staff(user.getUniqueId());
            return true;
        }

        return false;
    }

    @Override
    public void close() {
        nodeAddEventEventSubscription.close();
    }
}
