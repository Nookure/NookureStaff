package com.nookure.staff.paper.test.freeze;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.manager.FreezeManager;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.paper.StaffPaperPlayerWrapper;
import com.nookure.staff.paper.bootstrap.StaffBootstrapper;
import com.nookure.staff.paper.extension.FreezePlayerExtension;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.Preconditions;

public class StaffFreezeAnotherPlayerTest {
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
  @DisplayName("Freeze another player")
  public void freezeAnotherPlayer() {
    final FreezeManager freezeManager = injector.getInstance(FreezeManager.class);
    final PlayerMock staff = new PlayerMock(server, "Staff 1");

    staff.setOp(true);

    server.addPlayer(staff);

    final PlayerWrapperManager<Player> playerWrapperManager = injector.getInstance(Key.get(new TypeLiteral<>() {
    }));

    final StaffPaperPlayerWrapper staffPlayerWrapper = (StaffPaperPlayerWrapper) playerWrapperManager.getStaffPlayer(staff.getUniqueId()).orElseThrow();

    final PlayerMock hacker = new PlayerMock(server, "Hacker");
    server.addPlayer(hacker);

    final PlayerWrapper targetPlayerWrapper = playerWrapperManager.getPlayerWrapper(hacker.getUniqueId()).orElseThrow();

    staffPlayerWrapper.getExtension(FreezePlayerExtension.class).orElseThrow().freezePlayer(targetPlayerWrapper);

    Preconditions.condition(freezeManager.isFrozen(hacker.getUniqueId()), "The player should be frozen");
  }
}
