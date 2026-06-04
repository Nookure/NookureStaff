package com.nookure.staff.paper.test.command;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.paper.bootstrap.StaffBootstrapper;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

public class StaffCommandTest {
  private ServerMock server;
  private Injector injector;

  @BeforeEach
  public void setUp() {
    server = MockBukkit.mock();
    StaffBootstrapper plugin = MockBukkit.load(StaffBootstrapper.class);
    injector = plugin.getInjector();
  }

  @AfterEach
  public void tearDown() {
    MockBukkit.unmock();
  }

  @Test
  @DisplayName("Enable staff mode and check")
  public void enableStaffModeAndCheck() {
    PlayerMock player = getOpPlayer();
    server.addPlayer(player);

    PlayerWrapperManager<Player> playerWrapperManager = injector.getInstance(Key.get(new TypeLiteral<>() {
    }));

    Optional<StaffPlayerWrapper> playerWrapper = playerWrapperManager.getStaffPlayer(player.getUniqueId());

    assert playerWrapper.isPresent();

    playerWrapper.get().toggleStaffMode();

    assert playerWrapper.get().isInStaffMode();
  }

  public PlayerMock getOpPlayer() {
    PlayerMock player = new PlayerMock(server, "StaffPlayer", UUID.randomUUID());
    player.setOp(true);
    return player;
  }
}
