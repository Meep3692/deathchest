package ca.awoo.deathchest;

import org.bukkit.plugin.java.JavaPlugin;

public class DeathChest extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
    }

    @Override
    public void onDisable() {
    }
}
