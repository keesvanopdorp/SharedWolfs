package xyz.keesvanopdorp.SharedWolfs.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import xyz.keesvanopdorp.SharedWolfs.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class PlayerEvents implements Listener {
    Main plugin;

    public PlayerEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        // Step 1 checks if the entity the interacted with is not a wolf
        if (entity.getType() != EntityType.WOLF) {
            return;
        }

        Wolf selectedWolf = (Wolf) entity;


        if ((player.getInventory().getItemInMainHand().getType() == Material.BONE && !player.isSneaking()) ||
                player.getInventory().getItemInMainHand().getType() != Material.BONE ||
                selectedWolf.getOwner() == null) {
            // vanilla behaviour
            return;
        }

        if (player.isSneaking()) {
            try {
                YamlConfiguration configuration = this.plugin.wolfUtil.getWolfConfiguration(selectedWolf.getUniqueId());
                List<String> owners = new ArrayList<>();

                if (configuration.contains("owners")) {
                    owners = configuration.getStringList("owners");
                }

                if (!configuration.contains("tamable")) configuration.set("tamable", true);

                if (!configuration.getBoolean("tamable")) {
                    return;
                }

                if (!owners.contains(player.getUniqueId().toString())) {
                    owners.add(player.getUniqueId().toString());
                    configuration.set("owners", owners);
                    this.plugin.wolfUtil.writeToWolfFile(selectedWolf.getUniqueId(), configuration);
                    player.sendMessage(ChatColor.AQUA + "[Shared wolfs] You have now tamed this wolf");
                    return;
                }

                selectedWolf.setOwner(player);
                player.sendMessage(ChatColor.AQUA + "[shared wolfs] you are now the temporary owner of this wolf");
            } catch (IOException | InvalidConfigurationException | NullPointerException e) {
                this.plugin.getLogger().log(Level.SEVERE, e.getMessage());
            }
        }
    }
}
