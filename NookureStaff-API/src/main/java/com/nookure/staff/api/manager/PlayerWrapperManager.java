package com.nookure.staff.api.manager;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.StaffPlayerWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

/**
 * Manages all the player wrappers.
 * <p>
 * You can get the instance of this class by injecting it,
 * see {@link com.google.inject.Injector#getInstance(Class)}
 * <br>
 *
 * @param <T> The player class of the player wrapper implementation
 *            (e.g. PlayerWrapper@Player or PlayerWrapper@ProxiedPlayer>)
 *            This is used to get the player wrapper by its player class.
 *            But you can also use the unique id of the player wrapper.
 *            see {@link #getPlayerWrapper(UUID)}
 * @see PlayerWrapper for more information about player wrappers
 * @see StaffPlayerWrapper for more information about staff players
 * @since 1.0.0
 */
@Singleton
public final class PlayerWrapperManager<T> {
  @Inject
  private Logger logger;
  private final BiMap<T, PlayerWrapper> playerWrappersByPlayerClass = HashBiMap.create();
  private final LinkedHashMap<UUID, PlayerWrapper> playerWrappersByUUID = new LinkedHashMap<>();
  private final ArrayList<UUID> staffPlayers = new ArrayList<>();

  /**
   * Gets a player wrapper by its player class.
   *
   * @param player the player class of the player wrapper
   * @return an optional containing the player wrapper if it exists
   */
  @NotNull
  public Optional<PlayerWrapper> getPlayerWrapper(@NotNull T player) {
    Objects.requireNonNull(player, "Player cannot be null");
    return Optional.ofNullable(playerWrappersByPlayerClass.get(player));
  }

  /**
   * Gets a player wrapper by its unique id.
   *
   * @param uuid the unique id of the player wrapper
   * @return an optional containing the player wrapper if it exists
   */
  @NotNull
  public Optional<PlayerWrapper> getPlayerWrapper(@NotNull UUID uuid) {
    Objects.requireNonNull(uuid, "UUID cannot be null");
    return Optional.ofNullable(playerWrappersByUUID.get(uuid));
  }

  /**
   * Gets a player wrapper by its unique id.
   *
   * @param uuid the unique id of the player wrapper
   * @return the player wrapper if it exists, otherwise null
   */
  @Nullable
  public PlayerWrapper getPlayerWrapperOrNull(@NotNull UUID uuid) {
    Objects.requireNonNull(uuid, "UUID cannot be null");
    return playerWrappersByUUID.get(uuid);
  }

  /**
   * Gets a staff player by its player class.
   *
   * @param uuid the unique id of the staff player
   * @return an optional containing the staff player if it exists
   */
  @NotNull
  public Optional<StaffPlayerWrapper> getStaffPlayer(@NotNull UUID uuid) {
    Objects.requireNonNull(uuid, "UUID cannot be null");
    Optional<PlayerWrapper> optionalPlayerWrapper = getPlayerWrapper(uuid);

    if (optionalPlayerWrapper.isPresent()) {
      PlayerWrapper playerWrapper = optionalPlayerWrapper.get();

      if (playerWrapper instanceof StaffPlayerWrapper) {
        return Optional.of((StaffPlayerWrapper) playerWrapper);
      }
    }

    return Optional.empty();
  }

  @Nullable
  public StaffPlayerWrapper getStaffPlayerOrNull(@NotNull UUID uuid) {
    Objects.requireNonNull(uuid, "UUID cannot be null");
    Optional<PlayerWrapper> optionalPlayerWrapper = getPlayerWrapper(uuid);

    if (optionalPlayerWrapper.isPresent()) {
      PlayerWrapper playerWrapper = optionalPlayerWrapper.get();

      if (playerWrapper instanceof StaffPlayerWrapper) {
        return (StaffPlayerWrapper) playerWrapper;
      }
    }

    return null;
  }

  /**
   * Gets a player by its player wrapper.
   *
   * @param playerWrapper the player wrapper of the player
   * @return an optional containing the player if it exists
   */
  @NotNull
  public Optional<T> getPlayer(@NotNull PlayerWrapper playerWrapper) {
    Objects.requireNonNull(playerWrapper, "PlayerWrapper cannot be null");
    return Optional.ofNullable(playerWrappersByPlayerClass.inverse().get(playerWrapper));
  }

  /**
   * Adds a player wrapper to the manager.
   *
   * @param player        the player that will be used as a key
   * @param playerWrapper the player wrapper that will be used as a value
   * @param isStaff       whether the player is a staff player or not
   */
  public void addPlayerWrapper(@NotNull T player, @NotNull PlayerWrapper playerWrapper, boolean isStaff) {
    Objects.requireNonNull(player, "Player cannot be null");
    Objects.requireNonNull(playerWrapper, "PlayerWrapper cannot be null");

    synchronized (playerWrappersByPlayerClass) {
      if (playerWrappersByPlayerClass.containsKey(player)) {
        logger.warning("PlayerWrapper already exists for player: %s", player);
        return;
      }

      playerWrappersByPlayerClass.put(player, playerWrapper);
    }

    synchronized (playerWrappersByUUID) {
      if (playerWrappersByUUID.containsKey(playerWrapper.getUniqueId())) {
        logger.warning("PlayerWrapper already exists for UUID: %s", playerWrapper.getUniqueId());
        return;
      }

      playerWrappersByUUID.put(playerWrapper.getUniqueId(), playerWrapper);
    }

    if (isStaff) {
      synchronized (staffPlayers) {
        staffPlayers.add(playerWrapper.getUniqueId());
      }
    }
  }

  /**
   * Adds a player wrapper to the manager without being a staff player.
   *
   * @param player        the player that will be used as a key
   * @param playerWrapper the player wrapper that will be used as a value
   */
  public void addPlayerWrapper(@NotNull T player, @NotNull PlayerWrapper playerWrapper) {
    addPlayerWrapper(player, playerWrapper, false);
  }

  /**
   * Removes a player wrapper from the manager.
   *
   * @param player the player that will be used as a key
   */
  public void removePlayerWrapper(@NotNull T player) {
    Objects.requireNonNull(player, "Player cannot be null");
    synchronized (playerWrappersByPlayerClass) {
      PlayerWrapper playerWrapper = playerWrappersByPlayerClass.remove(player);
      if (playerWrapper == null) {
        logger.warning("PlayerWrapper not found for player: %s", player);
        return;
      }
      if (playerWrapper instanceof StaffPlayerWrapper) staffPlayers.remove(playerWrapper.getUniqueId());

      synchronized (playerWrappersByUUID) {
        playerWrappersByUUID.remove(playerWrapper.getUniqueId());
      }
    }
  }

  /**
   * Gets the values as a stream.
   *
   * @return a stream of player wrappers
   */
  public Stream<PlayerWrapper> stream() {
    return playerWrappersByUUID.values().stream();
  }

  /**
   * Gets the amount of staff players.
   *
   * @return the amount of staff players
   */
  public int getStaffCount() {
    return staffPlayers.size();
  }

  /**
   * Gets the amount of player wrappers.
   *
   * @return the amount of player wrappers
   */
  public int getPlayerCount() {
    return playerWrappersByUUID.size();
  }

  public boolean isStaffPlayer(@NotNull UUID uuid) {
    Objects.requireNonNull(uuid, "UUID cannot be null");
    return staffPlayers.contains(uuid);
  }

  /**
   * Clears all the mappings from the manager.
   */
  public void clear() {
    playerWrappersByPlayerClass.clear();
    playerWrappersByUUID.clear();
    staffPlayers.clear();
  }
}
