package com.nookure.staff.paper.hook.permission;

import static java.util.Objects.requireNonNull;

import com.google.inject.Singleton;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.hook.PermissionHook;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class LuckPermsPermissionHook implements PermissionHook {
    private final RegisteredServiceProvider<LuckPerms> provider =
            Bukkit.getServicesManager().getRegistration(LuckPerms.class);

    @Override
    public @NotNull String getHighestGroup(@NotNull PlayerWrapper player) {
        if (provider == null) {
            throw new IllegalStateException("LuckPerms is not installed on the server.");
        }

        return requireNonNull(provider.getProvider().getUserManager().getUser(player.getUniqueId()))
                .getPrimaryGroup();
    }
}
