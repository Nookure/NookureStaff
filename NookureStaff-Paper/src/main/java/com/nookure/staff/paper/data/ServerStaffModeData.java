package com.nookure.staff.paper.data;

import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.util.LocationWrapper;
import com.nookure.staff.paper.PaperPlayerWrapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class ServerStaffModeData { // TODO: Pending recode
    private final NookureStaff plugin;
    private final StaffModeDataRecord record;
    private final PlayerWrapper player;
    private final Logger logger;

    /**
     * Create a new staff mode user data container.
     * <p>
     * This constructor is private because you should use {@link #read(NookureStaff, PlayerWrapper)}
     * to read a staff mode user record from the binary file.
     * <br>
     * You can also use {@link #write()} to write the staff mode user record to the binary file.
     * <br>
     * This class is not thread-safe.
     * </p>
     *
     * @param plugin the plugin instance
     * @param record the staff mode user record
     * @param player the player
     */
    private ServerStaffModeData(NookureStaff plugin, StaffModeDataRecord record, PlayerWrapper player) {
        this.plugin = plugin;
        this.logger = plugin.getPLogger();
        this.record = record;
        this.player = player;
    }

    /**
     * Read a staff mode user record from the database.
     *
     * @param plugin the plugin instance
     * @param player the player
     * @return the staff mode user data container
     */
    public static ServerStaffModeData read(NookureStaff plugin, PlayerWrapper player) {
        Logger logger = plugin.getPLogger();

        logger.debug("Reading staff mode user record for player %s from database", player.getName());

        long start = System.currentTimeMillis();

        String file = plugin.getPluginDataFolder().getAbsolutePath() + "/data/" + player.getUniqueId() + ".nookdata";

        if (!new File(file).exists()) {
            logger.debug("Staff mode user record for player %s does not exist in database", player.getName());
            if (!(player instanceof PaperPlayerWrapper playerWrapper)) return null;
            Player bukkitPlayer = playerWrapper.getPlayer();

            return new ServerStaffModeData(
                    plugin,
                    new StaffModeDataRecord(
                            bukkitPlayer.getInventory().getContents(),
                            bukkitPlayer.getInventory().getArmorContents(),
                            new ItemStack[0],
                            false,
                            new LocationWrapper(
                                    ((PaperPlayerWrapper) player).getPlayer().getLocation())),
                    player);
        }

        StaffModeDataRecord record;

        try {
            record =
                    (StaffModeDataRecord) new PluginObjectInputStream(Files.newInputStream(Path.of(file))).readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.severe(
                    "Failed to read staff mode user record for player %s from database, \n %s",
                    player.getName(), e.getMessage());
            throw new RuntimeException(e);
        }

        logger.debug(
                "Read staff mode user record for player %s from database in %dms",
                player.getName(), System.currentTimeMillis() - start);

        return new ServerStaffModeData(plugin, record, player);
    }

    public void write() {
        logger.debug("Writing staff mode user record for player %s to database", player.getName());

        long start = System.currentTimeMillis();

        File dataFolder = plugin.getPluginDataFolder().toPath().resolve("data").toFile();

        if (!dataFolder.exists()) {
            if (dataFolder.mkdirs()) {
                logger.debug("Created data folder for staff mode user records");
            }
        }

        File tempFile = plugin.getPluginDataFolder()
                .toPath()
                .resolve("data")
                .resolve(player.getUniqueId() + ".nookdata.tmp")
                .toFile();

        try {
            if (tempFile.createNewFile()) {
                logger.debug("Created temp file for staff mode user record for player %s", player.getName());
            }
        } catch (IOException e) {
            logger.severe(
                    "Failed to create temp file for staff mode user record for player %s, \n %s",
                    player.getName(), e.getMessage());
            throw new RuntimeException(e);
        }

        try (FileOutputStream temp = new FileOutputStream(tempFile)) {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(temp);
            out.writeObject(record);
            out.flush();
            out.close();
        } catch (IOException e) {
            logger.severe(
                    "Failed to write staff mode user record for player %s to database, \n %s",
                    player.getName(), e.getMessage());

            throw new RuntimeException(e);
        }

        try {
            Files.move(
                    tempFile.toPath(),
                    plugin.getPluginDataFolder().toPath().resolve("data").resolve(player.getUniqueId() + ".nookdata"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.severe(
                    "Failed to move staff mode user record for player %s to database, \n %s",
                    player.getName(), e.getMessage());

            throw new RuntimeException(e);
        }

        logger.debug(
                "Wrote staff mode user record for player %s to database in %dms",
                player.getName(), System.currentTimeMillis() - start);
    }

    public StaffModeDataRecord record() {
        return record;
    }
}
