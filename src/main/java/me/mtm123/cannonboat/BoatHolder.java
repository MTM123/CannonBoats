package me.mtm123.cannonboat;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class BoatHolder implements InventoryHolder {

    private final String title;
    private final ItemStack[] items;

    public BoatHolder(String title, ItemStack[] items) {
        this.title = title;
        this.items = items;
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, InventoryType.HOPPER, title);

        for(ItemStack i : items){
            if(i != null){
                inv.addItem(i);
            }
        }

        return inv;
    }

}
