package com.nookure.staff.api.config.bukkit.partials.messages;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class PinMessagePartial {
    @Setting
    public String thePinIsIncorrect = "<red>The PIN is incorrect.";

    @Setting
    public String correctPin = "<green>The PIN is correct.";

    @Setting
    public String pinSet = "<green>Your PIN has been set to {pin}.";

    @Setting
    public String alreadyLoggedIn = "<red>You are already logged in.";

    @Setting
    public String doesntAllowedToDoThatUntilLoggedIn =
            "<red>You don't have permission to do that until you enter your PIN.";

    @Setting
    public String changePinCommandUsage = "<red>Usage: /changepin <new pin> <-- Must be 4 digits.";

    @Setting
    public String pinDeleteCommandUsage = "<red>Usage: /deletepin <uuid|name>";

    @Setting
    public String pinDeleted = "<green>The PIN has been successfully deleted for {player}.";

    @Setting
    public String pinChanged = "<green>Your PIN has been changed to {pin}.";

    @Setting
    public String pinNotSet = "<red>You don't have a PIN set.";

    public String youMustHaveToSetAPin = """
      {center}<bold>◆ <red>Nookure <white>Network</white> ◆</bold>

      {center}<gray>You <red>must</red> have to set a <red>PIN</red> before you can play on this server.
      {center}<gray>Use <red><u><click:run_command:'/setpin'>/setpin</click></u></red> to set a PIN.
      {center}<gray>You have {time} to set a PIN.
      """;

    @Setting
    public final String pinTimeExpired = "<red>Your time to set a PIN has expired.";
}
