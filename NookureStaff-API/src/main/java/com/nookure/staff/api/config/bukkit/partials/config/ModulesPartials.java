package com.nookure.staff.api.config.bukkit.partials.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class ModulesPartials {
    @Setting
    @Comment("""
      Enable or disable the staff mode.
      This include /staff command and the staff
      vault.
      """)
    private boolean staffMode = true;

    @Setting
    @Comment("""
      Enable or disable the freeze module.
      This include /freeze command
      """)
    private boolean freeze = true;

    @Setting
    @Comment("""
      Enable or disable the vanish module.
      This include /vanish command and the plugin vanish
      on StaffMode.
      """)
    private boolean vanish = true;

    @Setting
    @Comment("""
      Enable or disable the StaffChat module.
      This include /staffchat command and
      the staff chat prefix.
      """)
    private boolean staffChat = true;

    @Setting
    @Comment("""
      This module is required to be enabled
      if you want to use any module that requires
      player data like user notes
      """)
    private boolean playerData = true;

    @Setting
    @Comment("""
      Enable or disable the user notes module.
      This include /note command and the user notes
      vault.
      """)
    private boolean userNotes = true;

    @Setting
    @Comment("""
      Enable or disble the player-list module.
      This will allow you to list all the players on the server
      and perform actions on them.
      """)
    private boolean playerList = true;

    @Setting
    @Comment("""
      Enable or disable the pin code module.
      This will allow you to set a pin code to the staff's accounts
      and use it to login.

      WARNING: STILL IN BETA
      """)
    private boolean pinCode = false;

    public boolean isStaffMode() {
        return staffMode;
    }

    public boolean isFreeze() {
        return freeze;
    }

    public boolean isVanish() {
        return vanish;
    }

    public boolean isStaffChat() {
        return staffChat;
    }

    public boolean isPlayerData() {
        return playerData;
    }

    public boolean isUserNotes() {
        return userNotes;
    }

    public boolean isPlayerList() {
        return playerList;
    }

    public boolean isPinCode() {
        return pinCode;
    }
}
