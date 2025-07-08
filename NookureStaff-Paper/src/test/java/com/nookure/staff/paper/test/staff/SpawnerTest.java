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
import com.nookure.staff.paper.listener.staff.state.OnSpawnerSpawn;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SpawnerTest {
    private ServerMock server;
    private Injector injector;
    private OnSpawnerSpawn spawnerListener;
    private PlayerWrapperManager<Player> playerWrapperManager;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        StaffBootstrapper plugin = MockBukkit.load(StaffBootstrapper.class);
        injector = plugin.getInjector();
        spawnerListener = injector.getInstance(OnSpawnerSpawn.class);
        playerWrapperManager = injector.getInstance(Key.get(new TypeLiteral<>() {}));
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test spawner works with normal players")
    public void testSpawnerWorksWithNormalPlayers() {
        // Create a normal player (not staff)
        PlayerMock normalPlayer = new PlayerMock(server, "NormalPlayer");
        server.addPlayer(normalPlayer);

        // Create a spawner and set location
        World world = server.addSimpleWorld("world");
        Location spawnerLocation = new Location(world, 0, 64, 0);
        normalPlayer.teleport(spawnerLocation); // Put player near spawner

        CreatureSpawner spawner = (CreatureSpawner) world.getBlockAt(spawnerLocation).getState();
        spawner.setSpawnedType(EntityType.ZOMBIE);
        spawner.update();

        // Create spawn event
        SpawnerSpawnEvent event = new SpawnerSpawnEvent(
            server.addEntity(EntityType.ZOMBIE),
            spawner
        );

        // Process the event
        spawnerListener.onSpawnerSpawn(event);

        // Event should NOT be cancelled (spawning should happen)
        assertFalse(event.isCancelled(), "Spawning should work with normal players nearby");
    }

    @Test
    @DisplayName("Test spawner works when only vanished staff nearby")
    public void testSpawnerWorksWithOnlyVanishedStaff() {
        // Create a staff player
        PlayerMock staffPlayer = new PlayerMock(server, "StaffPlayer");
        staffPlayer.setOp(true);
        server.addPlayer(staffPlayer);

        // Get staff wrapper and enable vanish
        Optional<StaffPlayerWrapper> staffWrapper = playerWrapperManager.getStaffPlayer(staffPlayer.getUniqueId());
        assertTrue(staffWrapper.isPresent(), "Staff player wrapper should exist");
        staffWrapper.get().enableVanish(true);

        // Create a spawner and set location
        World world = server.addSimpleWorld("world");
        Location spawnerLocation = new Location(world, 0, 64, 0);
        staffPlayer.teleport(spawnerLocation); // Put vanished staff near spawner

        CreatureSpawner spawner = (CreatureSpawner) world.getBlockAt(spawnerLocation).getState();
        spawner.setSpawnedType(EntityType.ZOMBIE);
        spawner.update();

        // Create spawn event
        SpawnerSpawnEvent event = new SpawnerSpawnEvent(
            server.addEntity(EntityType.ZOMBIE),
            spawner
        );

        // Process the event
        spawnerListener.onSpawnerSpawn(event);

        // With the fix, spawning should be blocked when only vanished staff are nearby
        assertTrue(event.isCancelled(), "Spawning should be blocked when only vanished staff are nearby");
    }

    @Test
    @DisplayName("Test spawner works when vanished staff and normal players both nearby")
    public void testSpawnerWorksWithMixedPlayers() {
        // Create a staff player and normal player
        PlayerMock staffPlayer = new PlayerMock(server, "StaffPlayer");
        PlayerMock normalPlayer = new PlayerMock(server, "NormalPlayer");
        staffPlayer.setOp(true);
        
        server.addPlayer(staffPlayer);
        server.addPlayer(normalPlayer);

        // Enable vanish for staff
        Optional<StaffPlayerWrapper> staffWrapper = playerWrapperManager.getStaffPlayer(staffPlayer.getUniqueId());
        assertTrue(staffWrapper.isPresent());
        staffWrapper.get().enableVanish(true);

        // Create a spawner and set location
        World world = server.addSimpleWorld("world");
        Location spawnerLocation = new Location(world, 0, 64, 0);
        staffPlayer.teleport(spawnerLocation);
        normalPlayer.teleport(spawnerLocation);

        CreatureSpawner spawner = (CreatureSpawner) world.getBlockAt(spawnerLocation).getState();
        spawner.setSpawnedType(EntityType.ZOMBIE);
        spawner.update();

        // Create spawn event
        SpawnerSpawnEvent event = new SpawnerSpawnEvent(
            server.addEntity(EntityType.ZOMBIE),
            spawner
        );

        // Process the event
        spawnerListener.onSpawnerSpawn(event);

        // Event should NOT be cancelled (normal player can trigger spawning)
        assertFalse(event.isCancelled(), "Spawning should work when normal players are present");
    }

    @Test
    @DisplayName("Test spawner works when no players nearby")
    public void testSpawnerWorksWhenNoPlayersNearby() {
        // Create a spawner with no players nearby
        World world = server.addSimpleWorld("world");
        Location spawnerLocation = new Location(world, 0, 64, 0);

        CreatureSpawner spawner = (CreatureSpawner) world.getBlockAt(spawnerLocation).getState();
        spawner.setSpawnedType(EntityType.ZOMBIE);
        spawner.update();

        // Create spawn event
        SpawnerSpawnEvent event = new SpawnerSpawnEvent(
            server.addEntity(EntityType.ZOMBIE),
            spawner
        );

        // Process the event
        spawnerListener.onSpawnerSpawn(event);

        // With the fix, spawning should NOT be cancelled when no players are nearby
        // The plugin should only interfere when staff players are present
        assertFalse(event.isCancelled(), "Spawning should work when no players are nearby (let vanilla handle it)");
    }

    @Test
    @DisplayName("Test spawner works with non-vanished staff players")
    public void testSpawnerWorksWithNonVanishedStaff() {
        // Create a staff player
        PlayerMock staffPlayer = new PlayerMock(server, "StaffPlayer");
        staffPlayer.setOp(true);
        server.addPlayer(staffPlayer);

        // Get staff wrapper but don't enable vanish
        Optional<StaffPlayerWrapper> staffWrapper = playerWrapperManager.getStaffPlayer(staffPlayer.getUniqueId());
        assertTrue(staffWrapper.isPresent(), "Staff player wrapper should exist");
        assertFalse(staffWrapper.get().isInVanish(), "Staff should not be in vanish by default");

        // Create a spawner and set location
        World world = server.addSimpleWorld("world");
        Location spawnerLocation = new Location(world, 0, 64, 0);
        staffPlayer.teleport(spawnerLocation); // Put staff near spawner

        CreatureSpawner spawner = (CreatureSpawner) world.getBlockAt(spawnerLocation).getState();
        spawner.setSpawnedType(EntityType.ZOMBIE);
        spawner.update();

        // Create spawn event
        SpawnerSpawnEvent event = new SpawnerSpawnEvent(
            server.addEntity(EntityType.ZOMBIE),
            spawner
        );

        // Process the event
        spawnerListener.onSpawnerSpawn(event);

        // Spawning should work with non-vanished staff players
        assertFalse(event.isCancelled(), "Spawning should work with non-vanished staff players");
    }
}