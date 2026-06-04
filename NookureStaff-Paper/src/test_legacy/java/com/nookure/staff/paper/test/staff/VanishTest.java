package com.nookure.staff.paper.test.staff;

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

public class VanishTest {
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
  @DisplayName("Simple vanish test")
  public void testSimpleVanish() {
    PlayerMock player = new PlayerMock(server, "Staff 1");
    player.setOp(true);

    server.addPlayer(player);

    PlayerWrapperManager<Player> playerWrapperManager = injector.getInstance(Key.get(new TypeLiteral<>() {
    }));

    Optional<StaffPlayerWrapper> playerWrapper = playerWrapperManager.getStaffPlayer(player.getUniqueId());

    assert playerWrapper.isPresent();

    StaffPlayerWrapper staffPlayerWrapper = playerWrapper.get();

    staffPlayerWrapper.enableVanish();

    assert staffPlayerWrapper.isInVanish();
  }

  @Test
  @DisplayName("Enable vanish, leave, rejoin and check")
  public void testVanishLeaveRejoin() {
    PlayerMock player = new PlayerMock(server, "Staff 2", UUID.randomUUID());
    player.setOp(true);

    server.addPlayer(player);

    PlayerWrapperManager<Player> playerWrapperManager = injector.getInstance(Key.get(new TypeLiteral<>() {
    }));

    Optional<StaffPlayerWrapper> playerWrapper1 = playerWrapperManager.getStaffPlayer(player.getUniqueId());

    assert playerWrapper1.isPresent();

    playerWrapper1.get().toggleVanish();

    player.disconnect();

    server.addPlayer(player);

    Optional<StaffPlayerWrapper> playerWrapper2 = playerWrapperManager.getStaffPlayer(player.getUniqueId());

    assert playerWrapper2.isPresent();

    assert playerWrapper2.get().isInVanish();
  }
}
