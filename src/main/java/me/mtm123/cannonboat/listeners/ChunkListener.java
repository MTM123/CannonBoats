package me.mtm123.cannonboat.listeners;

import me.mtm123.cannonboat.BoatManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListener implements Listener {

    private final BoatManager boatManager;

    public ChunkListener(BoatManager boatManager) {
        this.boatManager = boatManager;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event){
        for(Entity e: event.getChunk().getEntities()){
            if(e.getType() == EntityType.BOAT){
                boatManager.loadBoat(e.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event){
        for(Entity e: event.getChunk().getEntities()){
            if(e.getType() == EntityType.BOAT){
                boatManager.unloadBoat(e.getUniqueId());
            }
        }
    }
}
