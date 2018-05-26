package me.mtm123.cannonboat;

import me.mtm123.spigotutils.InvUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CBoat {

    private ItemStack[] items;
    private boolean onCooldown;

    public CBoat(){
        items = new ItemStack[5];
        this.onCooldown = false;
    }

    public CBoat(ItemStack[] items){
        this.items = items;
        this.onCooldown = false;
    }

    public boolean isOnCooldown() {
        return onCooldown;
    }

    public void setOnCooldown(boolean onCooldown) {
        this.onCooldown = onCooldown;
    }

    public boolean hasEnoughTNT(){
        return InvUtil.countItem(items, Material.TNT) > 0;
    }

    public boolean hasEnoughSulphur(int amount){
        return InvUtil.countItem(items, Material.SULPHUR) >= amount;
    }

    public void deductSulphur(int sulphurAmount){

        int i = 0;
        while (sulphurAmount > 0){
            ItemStack item = items[i];
            if(item != null
                    && item.getType() == Material.SULPHUR){

                int amount = item.getAmount();
                if(sulphurAmount >= amount){
                    item.setType(Material.AIR);
                }else {
                    item.setAmount(amount - sulphurAmount);
                }
                sulphurAmount -= amount;

            }

            i++;
        }

    }

    public void deductTNT(){

        int k = 0;
        for(ItemStack i : items){
            if(i != null && i.getType() == Material.TNT){
                if(i.getAmount() > 1){
                    i.setAmount(i.getAmount() - 1);
                } else {
                    i.setType(Material.AIR);
                }
                return;
            }
        }

    }

    public void setItems(ItemStack[] items) {
        this.items = items;
    }

    public ItemStack[] getItems() {
        return items;
    }
}
