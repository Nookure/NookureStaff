package com.nookure.staff.paper.test.player;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.paper.bootstrap.StaffBootstrapper;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class PlayerWrapperTest {
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
  @DisplayName("Create one player and check join")
  public void createOnePlayerAndCheckJoin() {
    Player player = server.addPlayer();
    PlayerWrapperManager<Player> playerWrapperManager = injector.getInstance(Key.get(new TypeLiteral<>() {
    }));

    Optional<PlayerWrapper> playerWrapper = playerWrapperManager.getPlayerWrapper(player);

    assert playerWrapper.isPresent();
  }

  @Test
  @DisplayName("Create one player and check leave")
  public void createOnePlayerAndCheckLeave() {
    PlayerMock player = server.addPlayer();
    PlayerWrapperManager<Player> playerWrapperManager = injector.getInstance(Key.get(new TypeLiteral<>() {
    }));

    Optional<PlayerWrapper> playerWrapper = playerWrapperManager.getPlayerWrapper(player);

    assert playerWrapper.isPresent();

    player.disconnect();

    assert playerWrapperManager.getPlayerCount() == 0;
  }
}
