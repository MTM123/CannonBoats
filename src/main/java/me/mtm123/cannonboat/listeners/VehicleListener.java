package me.mtm123.cannonboat.listeners;

import me.mtm123.cannonboat.BoatManager;
import me.mtm123.cannonboat.CBoat;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;

public class VehicleListener implements Listener {

    private final BoatManager boatManager;

    public VehicleListener(BoatManager boatManager){
        this.boatManager = boatManager;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onVehicleDestroy(VehicleDestroyEvent event){

        Vehicle v = event.getVehicle();
        if(v.getType() != EntityType.BOAT)
            return;

        Player p = boatManager.getPlayerFromBoat(v.getUniqueId());
        if(p != null){
            p.closeInventory();
        }

        CBoat cBoat = boatManager.getBoat(v.getUniqueId());

        ItemStack[] items = cBoat.getItems();

        World w = event.getVehicle().getWorld();
        Location loc = event.getVehicle().getLocation();
        for(ItemStack i : items){
            if(i != null)
                w.dropItemNaturally(loc, i);
        }

        boatManager.removeBoat(event.getVehicle().getUniqueId());

    }


}
