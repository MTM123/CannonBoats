package me.mtm123.cannonboat.listeners;

import me.mtm123.cannonboat.BoatHolder;
import me.mtm123.cannonboat.BoatManager;
import me.mtm123.cannonboat.CBoat;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;

public class PlayerListener implements Listener {

    private final BoatManager boatManager;

    private final String title;

    public PlayerListener(BoatManager boatManager, FileConfiguration cfg) {
        this.boatManager = boatManager;

        this.title = ChatColor.translateAlternateColorCodes('&', cfg.getString("inv-title"));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onInteractAtEntity(PlayerInteractAtEntityEvent event){

        Player player = event.getPlayer();

        if(!player.isSneaking())
            return;

        if(!event.getPlayer().hasPermission("cannonboat.openinv"))
            return;

        Entity rightClicked = event.getRightClicked();

        if(rightClicked.getType() != EntityType.BOAT)
            return;

        if (player.getVehicle() != null && (player.getVehicle().equals(rightClicked)
                ||  player.getVehicle().getType() == EntityType.BOAT))
            return;

        CBoat cBoat = boatManager.getBoat(rightClicked.getUniqueId());

        if(cBoat.isOnCooldown())
            return;

        cBoat.setOnCooldown(true);

        boatManager.putOpenInventory(player.getUniqueId(), rightClicked.getUniqueId());

        Inventory inv = new BoatHolder(title, cBoat.getItems()).getInventory();
        player.openInventory(inv);

    }

}
