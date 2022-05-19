package xyz.keesvanopdorp.SharedWolfs;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.keesvanopdorp.SharedWolfs.events.EnityEvents;
import xyz.keesvanopdorp.SharedWolfs.events.PlayerEvents;
import xyz.keesvanopdorp.SharedWolfs.utils.WolfUtil;

import java.io.File;
import java.util.logging.Level;

public class Main extends JavaPlugin {
    public String wolfFolderPath;
    public WolfUtil wolfUtil;

    @Override
    public void onEnable() {
        this.wolfFolderPath = this.getDataFolder() + "/wolfs";
        File wolfFolder = new File(this.wolfFolderPath);
        if(!wolfFolder.exists()) {
            if (wolfFolder.mkdirs()) {
                this.getLogger().log(Level.INFO, "Wolfs folder created");
            }
        }

        this.wolfUtil = new WolfUtil(this.wolfFolderPath);

        this.getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);
        this.getServer().getPluginManager().registerEvents(new EnityEvents(this), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}