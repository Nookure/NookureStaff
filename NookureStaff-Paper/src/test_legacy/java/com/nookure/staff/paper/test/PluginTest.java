package com.nookure.staff.paper.test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitConfig;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.paper.bootstrap.StaffBootstrapper;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.*;

public class PluginTest {
  private StaffBootstrapper plugin;
  private Injector injector;

  @BeforeEach
  public void setUp() {
    ServerMock server = MockBukkit.mock();
    plugin = MockBukkit.load(StaffBootstrapper.class);
    injector = plugin.getInjector();
  }

  @Test
  @DisplayName("Verify the test environment")
  public void testPlugin() {
    Assertions.assertTrue(StaffBootstrapper.isMock);
  }

  @Test
  @DisplayName("Verify some injected classes")
  public void testInjections() {
    Assertions.assertNotNull(injector);
    Assertions.assertNotNull(plugin);
    Assertions.assertNotNull(injector.getInstance(Key.get(new TypeLiteral<PlayerWrapperManager<Player>>() {
    })));
    Assertions.assertNotNull(injector.getInstance(Key.get(new TypeLiteral<ConfigurationContainer<BukkitConfig>>() {
    })));
  }

  @AfterEach
  public void tearDown() {
    MockBukkit.unmock();
  }
}
