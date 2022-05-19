package xyz.keesvanopdorp.SharedWolfs.events;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import xyz.keesvanopdorp.SharedWolfs.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class EnityEvents implements Listener {
    private Main plugin;

    public EnityEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityTame(EntityTameEvent event) {
        Player player = (Player) event.getOwner();
        LivingEntity entity = event.getEntity();

        if(entity.getType() != EntityType.WOLF) {
            return;
        }
        UUID wolfUuid = entity.getUniqueId();

        try {
            YamlConfiguration wolfConfiguration = this.plugin.wolfUtil.getWolfConfiguration(wolfUuid);

            if(!wolfConfiguration.contains("owners")) {
                wolfConfiguration.set("owners", null);
            }
            List<String> owners = new ArrayList<>();
            owners.add(player.getUniqueId().toString());

            wolfConfiguration.set("owners", owners);
            wolfConfiguration.set("tamable", true);
            this.plugin.wolfUtil.writeToWolfFile(wolfUuid,wolfConfiguration);
        } catch (IOException | InvalidConfigurationException e) {
            this.plugin.getLogger().log(Level.SEVERE, e.getMessage());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if(entity.getType() != EntityType.WOLF) {
            return;
        }
        UUID wolfUuid = entity.getUniqueId();
        this.plugin.wolfUtil.deleteWolfFile(wolfUuid);
    }

}
