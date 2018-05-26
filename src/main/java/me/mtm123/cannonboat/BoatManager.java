package me.mtm123.cannonboat;

import me.mtm123.factionsaddons.data.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoatManager {

    private final FileConfiguration boatcfg;

    private final int sulphurPerShot;
    private final double power;
    private final int cooldownInMs;

    private final Map<UUID, CBoat> boats;
    private final Map<UUID, UUID> openBoatInvs;

    public BoatManager(FileConfiguration cfg, FileConfiguration boatcfg){
        this.sulphurPerShot = cfg.getInt("sulphur-per-shot");
        this.power = cfg.getDouble("power");
        this.cooldownInMs = (int) Math.round(cfg.getDouble("cooldown")*1000);
        this.boatcfg = boatcfg;

        this.boats = new HashMap<>();
        this.openBoatInvs = new HashMap<>();
    }

    public void loadBoat(UUID uuid){

        String key = uuid.toString();

        if(boatcfg.contains(key)){

            ItemStack[] items = new ItemStack[5];

            int j = 0;
            for(String k : boatcfg.getConfigurationSection(key + ".items").getKeys(false)){
                items[j] = boatcfg.getItemStack(key + ".items." + k);
                j++;
            }

            CBoat cBoat = new CBoat(items);

            boats.put(uuid, cBoat);
        }else{
            createBoat(uuid);
        }

    }

    public void unloadBoat(UUID uuid){
        saveBoat(uuid);

        boats.remove(uuid);
    }

    private void createBoat(UUID uuid){
        CBoat cBoat = new CBoat();

        boats.put(uuid, cBoat);

        int i = 0;
        while(i < 5){
            boatcfg.set(uuid.toString() + ".items." + i, new ItemStack(Material.AIR));
            i++;
        }

        ConfigManager.save(boatcfg, "boats.yml");

    }

    private void saveBoat(UUID uuid){

        CBoat cBoat = boats.get(uuid);

        int k = 0;
        for(ItemStack i : cBoat.getItems()){
            boatcfg.set(uuid.toString() + ".items." + k, i);
            k++;
        }

        ConfigManager.save(boatcfg, "boats.yml");

    }

    public void saveAllAndUnload(){
        for(Map.Entry<UUID, CBoat> e : boats.entrySet()){
            saveBoat(e.getKey());
        }

        boats.clear();
    }

    public void removeBoat(UUID uuid){

        boats.remove(uuid);
        boatcfg.set(uuid.toString(), null);

        ConfigManager.save(boatcfg, "boats.yml");

    }

    public CBoat getBoat(UUID uuid){
        CBoat cBoat = boats.get(uuid);
        if(cBoat == null) {
            loadBoat(uuid);
            cBoat = boats.get(uuid);
        }

        return cBoat;
    }

    public double getPower() {
        return power;
    }

    public int getCooldownInMs() {
        return cooldownInMs;
    }

    public int getSulphurPerShot() {
        return sulphurPerShot;
    }

    public void putOpenInventory(UUID player, UUID boat){
        openBoatInvs.put(player, boat);
    }

    public void removeOpenInventory(UUID player){
        openBoatInvs.remove(player);
    }

    public CBoat getBoatFromPlayer(UUID player){
        return boats.get(openBoatInvs.get(player));
    }

    public Player getPlayerFromBoat(UUID boat){
        for (Map.Entry<UUID, UUID> e : openBoatInvs.entrySet()) {
            if(e.getValue().equals(boat))
                return Bukkit.getPlayer(e.getKey());
        }

        return null;
    }

}
