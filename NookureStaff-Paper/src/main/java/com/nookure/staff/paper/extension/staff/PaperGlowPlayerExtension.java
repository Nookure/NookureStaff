 package com.nookure.staff.paper.extension.staff;

import com.google.inject.Inject;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.GlowConfig;
import com.nookure.staff.api.extension.staff.GlowPlayerExtension;
import com.nookure.staff.api.hook.PermissionHook;
import com.nookure.staff.api.util.Scheduler;
import com.nookure.staff.api.util.transformer.NameTagTransformer;
import com.nookure.staff.paper.StaffPaperPlayerWrapper;
import org.jetbrains.annotations.NotNull;

public class PaperGlowPlayerExtension extends GlowPlayerExtension {
  private final StaffPaperPlayerWrapper player;
  private final ConfigurationContainer<GlowConfig> config;
  private final NameTagTransformer nameTagTransformer;
  private final PermissionHook permissionHook;
  private final Scheduler scheduler;

  @Inject
  public PaperGlowPlayerExtension(
      @NotNull final StaffPlayerWrapper player,
      @NotNull final ConfigurationContainer<GlowConfig> config,
      @NotNull final NameTagTransformer nameTagTransformer,
      @NotNull final PermissionHook permissionHook,
      @NotNull final Scheduler scheduler
  ) {
    super(player);

    this.player = (StaffPaperPlayerWrapper) player;
    this.config = config;
    this.nameTagTransformer = nameTagTransformer;
    this.permissionHook = permissionHook;
    this.scheduler = scheduler;
  }

  @Override
  public void onStaffModeEnabled() {
    final String color = getGlowColor();
    scheduler.sync(() -> {
      nameTagTransformer.setPrefix(player, color);
      player.getPlayer().setGlowing(true);
    }, 1L);
  }

  @Override
  public void onStaffModeDisabled() {
    nameTagTransformer.removePrefix(player);
    player.getPlayer().setGlowing(false);
  }

  @Override
  public @NotNull String getGlowColor() {
    final String color = config.get().glowColors.get(permissionHook.getHighestGroup(player));

    return color == null ? config.get().defaultColor : color;
  }
}
