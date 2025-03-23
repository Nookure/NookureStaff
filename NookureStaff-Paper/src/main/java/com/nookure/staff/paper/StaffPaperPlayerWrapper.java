package com.nookure.staff.paper;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.nookure.staff.api.*;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitConfig;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.database.model.StaffStateModel;
import com.nookure.staff.api.database.repository.StaffStateRepository;
import com.nookure.staff.api.event.EventManager;
import com.nookure.staff.api.extension.StaffPlayerExtension;
import com.nookure.staff.api.extension.StaffPlayerExtensionManager;
import com.nookure.staff.api.extension.VanishExtension;
import com.nookure.staff.api.extension.staff.StaffModeExtension;
import com.nookure.staff.api.item.StaffItem;
import com.nookure.staff.api.state.PlayerState;
import com.nookure.staff.api.util.Scheduler;
import com.nookure.staff.api.util.ServerUtils;
import com.nookure.staff.api.util.TextUtils;
import com.nookure.staff.paper.bootstrap.StaffPaperPlayerWrapperModule;
import com.nookure.staff.paper.data.ServerStaffModeData;
import io.ebean.Database;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class StaffPaperPlayerWrapper extends PaperPlayerWrapper implements StaffPlayerWrapper {
  private final Map<Class<? extends StaffPlayerExtension>, StaffPlayerExtension> extensionMap = new HashMap<>();
  private final ConfigurationContainer<BukkitMessages> messages;
  private final ConfigurationContainer<BukkitConfig> config;
  private final Scheduler scheduler;
  private final StaffPlayerExtensionManager extensionManager;
  private final StaffStateRepository staffStateRepository;
  private final Injector injector;
  private final AtomicReference<StaffStateModel> staffDataModel = new AtomicReference<>();
  private final AtomicBoolean staffMode = new AtomicBoolean(false);
  private final AtomicBoolean staffChatAsDefault = new AtomicBoolean(false);
  private final EventManager eventManager;
  private VanishExtension vanishExtension;
  private StaffModeExtension staffModeExtension;

  @Inject
  public StaffPaperPlayerWrapper(
      @NotNull final JavaPlugin plugin,
      @NotNull final NookureStaff nookPlugin,
      @NotNull final Logger logger,
      @NotNull final ConfigurationContainer<BukkitMessages> messages,
      @NotNull final ConfigurationContainer<BukkitConfig> config,
      @NotNull final AtomicReference<Database> db,
      @NotNull final Scheduler scheduler,
      @NotNull final StaffPlayerExtensionManager extensionManager,
      @NotNull final StaffStateRepository staffStateRepository,
      @NotNull final EventManager eventManager,
      @NotNull @Assisted final Player player,
      @NotNull @Assisted final List<Class<? extends PlayerState>> states
  ) {
    super(plugin, nookPlugin, logger, config, scheduler, db, player, states);
    this.messages = messages;
    this.config = config;
    this.scheduler = scheduler;
    this.extensionManager = extensionManager;
    this.staffStateRepository = staffStateRepository;
    this.eventManager = eventManager;

    AtomicReference<ServerStaffModeData> serverStaffModeData = new AtomicReference<>();
    this.injector = nookPlugin.getInjector().createChildInjector(
        new StaffPaperPlayerWrapperModule(
            this,
            serverStaffModeData,
            staffDataModel,
            staffMode,
            staffChatAsDefault
        )
    );

    this.loadDBState();
    this.addExtensions();
    this.checkStaffModeState();
    this.checkVanishState();
  }

  //<editor-fold desc="Vanish">
  @Override
  public void toggleVanish() {
    if (!config.get().modules.isVanish()) {
      player.performCommand("vanish");
    }

    if (isInVanish()) disableVanish(false);
    else enableVanish(false);

    writeVanishState(isInVanish());
  }

  @Override
  public void enableVanish(boolean silent) {
    logger.debug("Enabling vanish for %s with previous state %s", player.getName(), isInVanish());
    if (isInVanish()) return;

    if (vanishExtension != null) {
      vanishExtension.enableVanish(silent);
    }
  }

  @Override
  public void disableVanish(boolean silent) {
    if (!isInVanish()) return;

    logger.debug("Disabling vanish for %s", player.getName());
    vanishExtension.disableVanish(silent);
  }

  @Override
  public boolean isInVanish() {
    if (vanishExtension == null) {
      return false;
    }

    return vanishExtension.isVanished();
  }

  public void checkVanishState() {
    if (!config.get().modules.isVanish()) return;
    if (!vanishExtension.restoreFromDatabase()) return;

    logger.debug("Checking vanish state for %s", player.getName());
    StaffStateModel staffDataModel = staffStateRepository.fromUUID(getUniqueId());

    if (staffDataModel == null) {
      logger.debug("Staff data model for %s is null", player.getName());
      return;
    }

    logger.debug("Vanish state for %s is %s", player.getName(), staffDataModel.vanished());
    logger.debug("StaffDataModel state: %s", staffDataModel);

    if (staffDataModel.vanished()) {
      enableVanish(true);
    } else {
      disableVanish(true);
    }

    vanishExtension.setVanished(staffDataModel.vanished());
  }

  public void loadDBState() {
    if (staffDataModel.get() == null) {
      staffDataModel.set(staffStateRepository.fromUUID(player.getUniqueId()));

      if (staffDataModel.get() == null) {
        staffDataModel.set(StaffStateModel
            .builder()
            .uuid(player.getUniqueId())
            .staffMode(false)
            .vanished(false)
            .staffChatEnabled(false)
            .build());

        staffStateRepository.savePlayerModel(staffDataModel.get());
      }
    }
  }

  public void writeVanishState(boolean state) {
    staffDataModel.set(StaffStateModel.builder(staffDataModel.get())
        .vanished(state)
        .build());

    staffStateRepository.saveOrUpdateModelAsync(staffDataModel.get())
        .thenRun(() -> logger.debug("Vanish state for %s has been set to %s on the database", player.getName(), state));

    if (vanishExtension != null) {
      vanishExtension.setVanished(state);
    }
  }
  //</editor-fold>

  //<editor-fold desc="StaffMode">
  public void enableStaffMode(boolean silentJoin) {
    if (staffModeExtension != null) staffModeExtension.enableStaffMode(silentJoin);
  }

  public void disableStaffMode() {
    if (staffModeExtension != null) staffModeExtension.disableStaffMode();
  }

  private void checkStaffModeState() {
    if (staffModeExtension != null) staffModeExtension.checkStaffMode();
  }

  private void writeStaffModeState(boolean state) {
    if (staffModeExtension != null) staffModeExtension.writeStaffModeState(state);
  }

  @Override
  public void toggleStaffMode(boolean silentJoin) {
    if (staffModeExtension != null) staffModeExtension.toggleStaffMode(silentJoin);
  }

  @Override
  public boolean isInStaffMode() {
    return staffMode.get();
  }
  //</editor-fold>

  //<editor-fold desc="Items">
  @Override
  public void setItems() {
    if (staffModeExtension != null) staffModeExtension.setItems();
  }

  @Override
  public @NotNull Map<Integer, StaffItem> getItems() {
    if (staffModeExtension != null) return staffModeExtension.getItems();
    return Map.of();
  }
  //</editor-fold>

  //<editor-fold desc="Player Perks">
  public void enablePlayerPerks() {
    player.setAllowFlight(true);
    player.setFlying(true);
    player.setInvulnerable(true);
    loadPotionEffects();
  }

  public void disablePlayerPerks() {
    if (player.getGameMode() != GameMode.CREATIVE) {
      player.setAllowFlight(false);
    }

    player.setFlying(false);
    player.setInvulnerable(false);
    unloadPotionEffects();

    if (player.getGameMode() == GameMode.CREATIVE) {
      player.setFoodLevel(20);
    }
  }
  //</editor-fold>

  //<editor-fold desc="Inventory">
  @Override
  public void saveInventory() {
    if (staffModeExtension != null) staffModeExtension.saveInventory();
  }

  @Override
  public void clearInventory() {
    if (staffModeExtension != null) staffModeExtension.clearInventory();
  }

  @Override
  public void restoreInventory() {
    if (staffModeExtension != null) staffModeExtension.restoreInventory();
  }
  //</editor-fold>

  //<editor-fold desc="Location">
  public void saveLocation() {
    if (staffModeExtension != null) staffModeExtension.saveLocation();
  }

  public void loadPreviousLocation() {
    if (staffModeExtension != null) staffModeExtension.restoreLocation();
  }
  //</editor-fold>

  //<editor-fold desc="StaffChat">
  @Override
  public boolean isStaffChatAsDefault() {
    return staffChatAsDefault.get();
  }

  @Override
  public void setStaffChatAsDefault(boolean staffChatAsDefault, boolean saveInDB) {
    this.staffChatAsDefault.set(staffChatAsDefault);

    if (!saveInDB) {
      return;
    }

    staffDataModel.set(StaffStateModel.builder(staffDataModel.get())
        .staffChatEnabled(staffChatAsDefault)
        .build());

    staffStateRepository.saveOrUpdateModelAsync(staffDataModel.get())
        .thenRun(() -> logger.debug("Staff chat state for %s has been set to %s on the database", player.getName(), staffChatAsDefault));
  }
  //</editor-fold>

  //<editor-fold desc="Potion effects">
  public void loadPotionEffects() {
    if (ServerUtils.MINECRAFT_VERSION.isBefore(1, 20)) return;

    if (config.get().staffMode.nightVision()) {
      if (!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) toggleNightVision();
    }

    if (config.get().staffMode.customPotionEffects()) {
      config.get().staffMode.potionEffects().forEach(potionEffect -> {
        String[] split = potionEffect.split(":");
        PotionEffectType potionEffectType = getEffect(potionEffect);

        if (potionEffectType == null) return;

        int duration = Integer.parseInt(split[1]);

        int amplifier = Integer.parseInt(split[2]);

        player.addPotionEffect(new PotionEffect(potionEffectType, duration, amplifier, true, false));
      });
    }
  }

  @Override
  public void toggleNightVision() {
    if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
      player.removePotionEffect(PotionEffectType.NIGHT_VISION);
      return;
    }

    scheduler.sync(() -> player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false)));
  }

  public void unloadPotionEffects() {
    if (config.get().staffMode.nightVision()) {
      if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) toggleNightVision();
    }

    if (config.get().staffMode.customPotionEffects()) {
      config.get().staffMode.potionEffects().forEach(potionEffect -> {
        PotionEffectType potionEffectType = getEffect(potionEffect);

        if (potionEffectType == null) return;

        player.removePotionEffect(potionEffectType);
      });
    }
  }

  @Nullable
  private PotionEffectType getEffect(@NotNull final String effect) {
    String[] split = effect.split(":");

    if (split.length < 3) {
      logger.warning("Invalid potion effect format: %s", effect);
      return null;
    }

    PotionEffectType potionEffectType = Registry.EFFECT.get(Objects.requireNonNull(NamespacedKey.fromString(split[0].toLowerCase())));

    if (potionEffectType == null) {
      logger.warning("Invalid potion effect type: %s", split[0]);
      return null;
    }

    return potionEffectType;
  }
  //</editor-fold>

  //<editor-fold desc="ActionBar">
  public void addActionBar() {
    if (!staffMode.get() && !(config.get().staffMode.actionBarOnVanish() && isInVanish())) return;
    if (!config.get().staffMode.actionBar()) return;
    if (!hasPermission(Permissions.ACTION_BAR_PERMISSION)) return;

    String vanished = isInVanish() ? messages.get().placeholder.placeholderTrue() : messages.get().placeholder.placeholderFalse();
    String staffChat = staffChatAsDefault.get() ? messages.get().placeholder.placeholderTrue() : messages.get().placeholder.placeholderFalse();
    double tpsCount = Bukkit.getTPS()[0];
    String tps = String.valueOf(tpsCount);
    tps = tps.substring(0, Math.min(tps.length(), 5));

    if (tpsCount > 18) {
      tps = "<green>" + tps;
    } else if (tpsCount > 15) {
      tps = "<yellow>" + tps;
    } else {
      tps = "<red>" + tps;
    }

    sendActionbar(MiniMessage.miniMessage().deserialize(
        TextUtils.parsePlaceholdersWithPAPI(
            player,
            messages.get().staffMode.actionBar()
                .replace("{vanished}", vanished)
                .replace("{staffChat}", staffChat)
                .replace("{tps}", tps)
        ))
    );
  }
  //</editor-fold>

  //<editor-fold desc="Extensions">
  @Override
  @SuppressWarnings("unchecked")
  public <T extends StaffPlayerExtension> Optional<T> getExtension(Class<T> extension) {
    StaffPlayerExtension staffPlayerExtension = extensionMap.get(extension);

    if (staffPlayerExtension == null) return Optional.empty();

    return Optional.of((T) staffPlayerExtension);
  }

  public void addExtensions() {
    extensionManager.getExtensionsStream().forEach(extension -> {
      try {
        StaffPlayerExtension instance = injector.getInstance(extension.extension());

        extensionMap.put(extension.extension(), instance);

        if (extension.base() != extension.extension()) {
          extensionMap.put(extension.base(), instance);
        }

        eventManager.registerListenerWeakly(instance);
      } catch (Exception e) {
        logger.severe("An error occurred while adding extension %s for %s: %s", extension.base().getName(), player.getName(), e.getMessage());
      }
    });

    vanishExtension = getExtension(VanishExtension.class).orElse(null);
    staffModeExtension = getExtension(StaffModeExtension.class).orElse(null);
  }

  public void unregisterExtensions() {
    extensionMap.values().forEach(eventManager::unregisterListener);
  }
  //</editor-fold>

  @Override
  public @NotNull Map<Class<? extends StaffPlayerExtension>, StaffPlayerExtension> getExtensions() {
    return extensionMap;
  }
}
