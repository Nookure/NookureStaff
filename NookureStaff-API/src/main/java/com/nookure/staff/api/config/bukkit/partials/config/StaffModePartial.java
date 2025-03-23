package com.nookure.staff.api.config.bukkit.partials.config;

import com.nookure.staff.api.config.bukkit.partials.VanishType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;

@ConfigSerializable
public class StaffModePartial {
  @Comment(
      """
          If true when a chest is opened by a staff member, the chest will not make any sound
          or animation.
          """
  )
  @Setting
  private boolean silentChestOpen = true;

  @Comment(
      """
          If true when a staff disables staff mode, they will be teleported to their previous location.
          This is useful for preventing staff members abusing the staff mode to teleport to a player or
          fly to a location.
          """
  )
  @Setting
  private boolean teleportToPreviousLocation = true;
  @Setting
  @Comment("""
      Enable or disable night vision when the player enters the staff mode.
      """)
  private boolean nightVision = true;

  @Setting
  @Comment("""
      Enable or disable custom potion effects when the player enters the staff mode.
      """)
  private boolean customPotionEffects = false;

  @Setting
  @Comment("""
      Potion effects to add when the player enters the staff mode.
      Format: "effect:level:duration"
      """)
  private List<String> potionEffects = List.of(
      "speed:1:999999",
      "jump:1:999999"
  );

  @Setting
  @Comment("""
      Enable the vanish when the staff member enables the staff mode.
      """)
  private boolean enableVanishOnStaffEnable = true;

  @Setting
  @Comment("""
      Disable the vanish when the staff member disables the staff mode.
      """)
  private boolean disableVanishOnStaffDisable = true;

  @Setting
  @Comment("""
      Enable or disable the action bar for the people who are in staff mode and
      they have the nookure.staff.actionbar permission.
      """)
  private boolean actionBar = true;

  @Setting
  @Comment("""
      Enable or disable the action bar when the user is in vanish
      """)
  private boolean actionBarOnVanish = true;

  @Setting
  @Comment("""
      The type of vanish to use when the player enters the staff mode.
      Available options:
      ----> INTERNAL_VANISH, SUPER_VANISH, PREMIUM_VANISH
      """)
  private VanishType vanishType = VanishType.INTERNAL_VANISH;

  @Setting
  @Comment("""
      Time to create a PIN code, before the player is kick
      """)
  private final String pinTime = "60s";

  public boolean silentChestOpen() {
    return silentChestOpen;
  }

  public boolean teleportToPreviousLocation() {
    return teleportToPreviousLocation;
  }

  public boolean nightVision() {
    return nightVision;
  }

  public boolean customPotionEffects() {
    return customPotionEffects;
  }

  public List<String> potionEffects() {
    return potionEffects;
  }

  public boolean enableVanishOnStaffEnable() {
    return enableVanishOnStaffEnable;
  }

  public boolean disableVanishOnStaffDisable() {
    return disableVanishOnStaffDisable;
  }

  public boolean actionBar() {
    return actionBar;
  }

  public boolean actionBarOnVanish() {
    return actionBarOnVanish;
  }

  public VanishType vanishType() {
    return vanishType;
  }

  public String pinTime() {
    return pinTime;
  }
}
