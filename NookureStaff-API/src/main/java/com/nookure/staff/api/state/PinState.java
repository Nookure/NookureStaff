package com.nookure.staff.api.state;

import static com.nookure.staff.api.database.AbstractPluginConnection.DATABASE_NAME;

import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.model.PinModel;
import io.ebean.DB;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents the state of the pin module on a player.
 */
public final class PinState extends PlayerState {
    public PinState(@NotNull PlayerWrapper wrapper) {
        super(wrapper);
        PinModel model = DB.byName(DATABASE_NAME)
                .find(PinModel.class)
                .where()
                .eq("player", wrapper.getPlayerModel())
                .findOne();

        pinSet = model != null;
    }

    private boolean pinSet;
    private boolean login = false;
    private boolean pinInventoryOpen = false;

    private long timeToCreateAPin = -1;

    private String pin = "";

    public synchronized boolean isPinSet() {
        return pinSet;
    }

    public synchronized void setPinSet(boolean pinSet) {
        this.pinSet = pinSet;
    }

    public synchronized boolean isLogin() {
        return login;
    }

    public synchronized void setLogin(boolean login) {
        this.login = login;
    }

    public synchronized long getTimeToCreateAPin() {
        return timeToCreateAPin;
    }

    public synchronized void setTimeToCreateAPin(long timeToCreateAPin) {
        this.timeToCreateAPin = timeToCreateAPin;
    }

    public synchronized boolean isPinInventoryOpen() {
        return pinInventoryOpen;
    }

    public synchronized void setPinInventoryOpen(boolean pinInventoryOpen) {
        this.pinInventoryOpen = pinInventoryOpen;
    }

    public synchronized boolean addPinDigit(char digit) {
        if (pin.length() >= 4 - 1) {
            pin += digit;
            return false;
        }

        pin += digit;
        return true;
    }

    public synchronized boolean removePinDigit() {
        if (pin.isEmpty()) {
            return false;
        }
        pin = pin.substring(0, pin.length() - 1);
        return true;
    }

    public synchronized String getPin() {
        return pin;
    }

    public synchronized void deletePin() {
        pin = "";
    }

    @Override
    public String toString() {
        return "PinState{" + "pinSet=" + pinSet + ", login=" + login + '}';
    }
}
