package com.nookure.staff.paper.item;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.config.bukkit.partials.CustomItemPartial;
import com.nookure.staff.api.config.bukkit.partials.CustomItemType;
import com.nookure.staff.api.item.ExecutableItem;
import com.nookure.staff.api.item.PlayerInteractItem;
import com.nookure.staff.api.item.StaffItem;
import com.nookure.staff.paper.StaffPaperPlayerWrapper;
import org.bukkit.Bukkit;

import org.jetbrains.annotations.NotNull;

public class CustomCommandItem extends StaffItem implements ExecutableItem, PlayerInteractItem {
  private final CustomItemPartial customItemPartial;

  @Inject
  public CustomCommandItem(@Assisted CustomItemPartial customItemPartial) {
    super(customItemPartial);
    this.customItemPartial = customItemPartial;
  }

  @Override
  public void click(@NotNull PlayerWrapper player) {
    if (customItemPartial.getType() == CustomItemType.COMMAND_TARGET_AS_PLAYER || customItemPartial.getType() == CustomItemType.COMMAND_TARGET_AS_CONSOLE) {
      return;
    }

    if (!(player instanceof StaffPaperPlayerWrapper staff)) {
      return;
    }

    if (customItemPartial.getType() == CustomItemType.COMMAND_AS_PLAYER) {
      Bukkit.dispatchCommand(staff.getPlayer(), customItemPartial.getCommand()
          .replace("{player}", staff.getName())
          .replace("{player_uuid}", staff.getUniqueId().toString())
      );
    }

    if (customItemPartial.getType() == CustomItemType.COMMAND_AS_CONSOLE) {
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), customItemPartial.getCommand()
          .replace("{player}", staff.getName())
          .replace("{player_uuid}", staff.getUniqueId().toString())
      );
    }
  }

  @Override
  public void click(@NotNull PlayerWrapper player, @NotNull PlayerWrapper target) {
    if (customItemPartial.getType() == CustomItemType.COMMAND_AS_CONSOLE || customItemPartial.getType() == CustomItemType.COMMAND_AS_PLAYER) {
      return;
    }

    if (!(player instanceof StaffPaperPlayerWrapper staff)) {
      return;
    }

    if (customItemPartial.getType() == CustomItemType.COMMAND_TARGET_AS_PLAYER) {
      staff.getPlayer().performCommand(customItemPartial.getCommand()
          .replace("{player}", staff.getName())
          .replace("{player_uuid}", staff.getUniqueId().toString())
          .replace("{target}", target.getName())
          .replace("{target_uuid}", target.getUniqueId().toString())
      );
    }

    if (customItemPartial.getType() == CustomItemType.COMMAND_TARGET_AS_CONSOLE) {
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), customItemPartial.getCommand()
          .replace("{player}", staff.getName())
          .replace("{player_uuid}", staff.getUniqueId().toString())
          .replace("{target}", target.getName())
          .replace("{target_uuid}", target.getUniqueId().toString())
      );
    }
  }
}
