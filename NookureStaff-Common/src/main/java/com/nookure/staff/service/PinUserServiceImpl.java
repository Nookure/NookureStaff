package com.nookure.staff.service;

import static java.util.Objects.requireNonNull;

import com.google.common.hash.Hashing;
import com.google.inject.Inject;
import com.nookure.staff.api.model.PinModel;
import com.nookure.staff.api.model.PlayerModel;
import com.nookure.staff.api.service.PinUserService;
import io.ebean.Database;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;

public class PinUserServiceImpl implements PinUserService {
    @Inject
    private AtomicReference<Database> database;

    @Override
    public boolean isValid(@NotNull UUID uuid, @NotNull String pin) {
        requireNonNull(uuid, "UUID cannot be null");
        requireNonNull(pin, "Pin cannot be null");

        PlayerModel model =
                database.get().find(PlayerModel.class).where().eq("uuid", uuid).findOne();

        return model != null && isValid(model, pin);
    }

    @Override
    public boolean isValid(@NotNull PlayerModel player, @NotNull String pin) {
        requireNonNull(player, "Player cannot be null");
        requireNonNull(pin, "Pin cannot be null");

        String hashedPin =
                Hashing.sha256().hashString(pin, StandardCharsets.UTF_8).toString();

        PinModel pinModel = getPinModel(player);
        boolean isValid = pinModel != null && pinModel.pin().equals(hashedPin);

        if (isValid) {
            pinModel.setLastLogin(Instant.now());
            database.get().save(pinModel);
        }

        return isValid;
    }

    @Override
    public void setPin(@NotNull PlayerModel player, @NotNull String pin) {
        requireNonNull(player, "Player cannot be null");
        requireNonNull(pin, "Pin cannot be null");

        String hashedPin =
                Hashing.sha256().hashString(pin, StandardCharsets.UTF_8).toString();

        PinModel pinModel = getPinModel(player);

        if (pinModel == null) {
            pinModel = new PinModel();
            pinModel.setPlayer(player);
        }

        pinModel.setPin(hashedPin);
        pinModel.setLastLogin(Instant.now());
        pinModel.setIp(player.getLastIp());
        database.get().save(pinModel);
    }

    @Override
    public void updateIp(@NotNull PlayerModel player) {
        requireNonNull(player, "Player cannot be null");

        PinModel pinModel = getPinModel(player);
        if (pinModel != null) {
            pinModel.setIp(player.getLastIp());
            database.get().save(pinModel);
        }
    }

    @Override
    public boolean isPinSet(@NotNull PlayerModel player) {
        requireNonNull(player, "Player cannot be null");

        return getPinModel(player) != null;
    }

    public PinModel getPinModel(PlayerModel player) {
        return database.get().find(PinModel.class).where().eq("player", player).findOne();
    }
}
