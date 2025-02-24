package com.nookure.staff.paper.placeholder;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nookure.staff.Constants;
import com.nookure.staff.api.placeholder.Placeholder;
import com.nookure.staff.api.placeholder.PlaceholderManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.Stream;

public class PlaceholderApiExtension extends PlaceholderExpansion {
  public static final String IDENTIFIER = "nkstaff";
  @Inject
  private PlaceholderManager placeholderManager;
  @Inject
  private Injector in;

  @Override
  public @NotNull String getIdentifier() {
    return IDENTIFIER;
  }

  @Override
  public @NotNull String getAuthor() {
    return "Angelillo15";
  }

  @Override
  public @NotNull String getVersion() {
    return Constants.VERSION;
  }

  @Override
  public boolean register() {
    Stream.of(
        ServerCountPlaceholder.class,
        StaffCountPlaceholder.class,
        StaffModePlaceholder.class,
        VanishPlaceholder.class,
        StaffGlowPlaceholder.class,
        FreezePlaceholder.class
    ).forEach(p -> placeholderManager.registerPlaceholder(in.getInstance(p)));

    return super.register();
  }

  @Override
  public String onPlaceholderRequest(Player player, @NotNull String params) {
    Optional<Placeholder> placeholder = placeholderManager.getPlaceholder(params);

    if (placeholder.isEmpty()) return "Not found";

    return placeholder.get().onPlaceholderRequest(player, params);
  }
}
