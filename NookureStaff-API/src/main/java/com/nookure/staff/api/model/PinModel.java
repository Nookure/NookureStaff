package com.nookure.staff.api.model;

import io.ebean.Model;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "nookure_staff_pin")
public class PinModel extends Model {
    @Id
    Long id;

    @OneToOne
    PlayerModel player;

    @Column(nullable = false, length = 64)
    String pin;

    @Column(nullable = false)
    Instant lastLogin;

    @Column(nullable = false, length = 15)
    String ip;

    public PinModel() {
        super("nkstaff");
    }

    public String ip() {
        return ip;
    }

    public PinModel setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String pin() {
        return pin;
    }

    public PinModel setPin(String pin) {
        this.pin = pin;
        return this;
    }

    public PlayerModel player() {
        return player;
    }

    public PinModel setPlayer(PlayerModel player) {
        this.player = player;
        return this;
    }

    public Long id() {
        return id;
    }

    public Instant lastLogin() {
        return lastLogin;
    }

    public PinModel setLastLogin(Instant lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }
}
