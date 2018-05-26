package me.mtm123.cannonboat;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.mtm123.cannonboat.commands.CommandRestart;
import me.mtm123.cannonboat.listeners.*;
import me.mtm123.factionsaddons.data.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class CannonBoats extends JavaPlugin{

    private CannonBoats plugin;

    private BoatManager boatManager;

    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        plugin = this;

        if(!getServer().getPluginManager().isPluginEnabled("ProtocolLib")){
            getLogger().log(Level.SEVERE, "ProtocolLib is missing! Disabling the plugin..");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        ConfigManager.initialize(plugin);

        protocolManager = ProtocolLibrary.getProtocolManager();

        loadPlugin(false);

    }

    @Override
    public void onDisable() {
        boatManager.saveAllAndUnload();
    }

    public void loadPlugin(boolean reload){

        PluginManager pm = getServer().getPluginManager();

        if(reload){

            for(Player p : Bukkit.getOnlinePlayers()){
                if(p.getOpenInventory().getTopInventory().getHolder() instanceof BoatHolder){
                    p.closeInventory();
                }
            }

            boatManager.saveAllAndUnload();

            HandlerList.unregisterAll(plugin);
            protocolManager.removePacketListeners(plugin);

        }

        FileConfiguration cfg = ConfigManager.load("config.yml");
        FileConfiguration boatcfg = ConfigManager.load("boats.yml");

        boatManager = new BoatManager(cfg, boatcfg);

        for(World w : Bukkit.getWorlds()){
            for(Chunk c : w.getLoadedChunks()){
                for(Entity e : c.getEntities()){
                    if(e.getType() == EntityType.BOAT){
                        boatManager.loadBoat(e.getUniqueId());
                    }
                }
            }
        }

        pm.registerEvents(new InventoryListener(boatManager), plugin);
        pm.registerEvents(new VehicleListener(boatManager), plugin);
        pm.registerEvents(new PlayerListener(boatManager, cfg), plugin);
        pm.registerEvents(new ChunkListener(boatManager), plugin);

        protocolManager.addPacketListener(new PacketListener(plugin, cfg, boatManager));

        getCommand("cbreload").setExecutor(new CommandRestart(plugin, cfg));

    }

}
