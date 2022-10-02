package es.angelillo15.mast.bungee;

import es.angelillo15.mast.bungee.metrics.Metrics;

public class MASTBungee extends MASTBungeeManager {
    @Override
    public void onEnable() {
        drawLogo();
        registerEvents();
        registerConfig();
        registerCommands();
        new Metrics(this, 16550);
    }

    @Override
    public void onDisable() {

    }
}
