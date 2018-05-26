package me.mtm123.cannonboat.listeners;

import me.mtm123.cannonboat.BoatHolder;
import me.mtm123.cannonboat.BoatManager;
import me.mtm123.cannonboat.CBoat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    private final BoatManager boatManager;

    private final ItemStack sulphur;
    private final ItemStack tnt;

    public InventoryListener(BoatManager boatManager) {
        this.boatManager = boatManager;

        this.sulphur = new ItemStack(Material.SULPHUR);
        this.tnt = new ItemStack(Material.TNT);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event){

        if(!(event.getInventory().getHolder() instanceof BoatHolder))
            return;

        Player player = (Player) event.getPlayer();

        Inventory inv = event.getInventory();

        CBoat cBoat = boatManager.getBoatFromPlayer(player.getUniqueId());

        boatManager.removeOpenInventory(player.getUniqueId());

        cBoat.setItems(inv.getContents());

        cBoat.setOnCooldown(false);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onItemClick(InventoryClickEvent event){

        if(!(event.getInventory().getHolder() instanceof BoatHolder))
            return;

        ItemStack movedItem = event.getCurrentItem();

        if(event.getAction() == InventoryAction.HOTBAR_SWAP
                || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD){

            movedItem = event.getWhoClicked().getInventory().getItem(event.getHotbarButton());

        }else if(event.getAction() == InventoryAction.PLACE_ALL
                || event.getAction() == InventoryAction.PLACE_ONE
                || event.getAction() == InventoryAction.PLACE_SOME){

            movedItem = event.getCursor();

        }

        if(movedItem != null
                && !(movedItem.isSimilar(sulphur) || movedItem.isSimilar(tnt)))
            event.setCancelled(true);

    }

}
