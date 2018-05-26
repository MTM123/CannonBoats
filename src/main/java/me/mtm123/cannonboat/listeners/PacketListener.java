package me.mtm123.cannonboat.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.mtm123.cannonboat.BoatManager;
import me.mtm123.cannonboat.CBoat;
import me.mtm123.cannonboat.CannonBoats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Vehicle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public class PacketListener extends PacketAdapter {

    private final CannonBoats plugin;
    private final BoatManager boatManager;

    private final FileConfiguration cfg;

    public PacketListener(CannonBoats plugin, FileConfiguration cfg, BoatManager boatManager) {
        super(plugin, PacketType.Play.Client.STEER_VEHICLE);
        this.plugin = plugin;
        this.boatManager = boatManager;
        this.cfg = cfg;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {

        Player player = event.getPlayer();
        if(player.getVehicle() == null
                || player.getVehicle().getType() != EntityType.BOAT)
            return;

        if(!player.hasPermission("cannonboat.shoot"))
            return;

        if(!event.getPacket().getBooleans().getValues().get(0))
            return;

        Vehicle v = (Vehicle) player.getVehicle();
        UUID uuid = v.getUniqueId();
        CBoat cboat = boatManager.getBoat(uuid);

        if(!cboat.isOnCooldown()){

            if(!cboat.hasEnoughTNT() || !cboat.hasEnoughSulphur(boatManager.getSulphurPerShot()))
                return;

            cboat.deductTNT();
            cboat.deductSulphur(boatManager.getSulphurPerShot());

            cboat.setOnCooldown(true);

            World w = player.getWorld();

            new BukkitRunnable(){
                @Override
                public void run(){
                    TNTPrimed tnt = w.spawn(player.getLocation(), TNTPrimed.class);
                    Vector velocity = player.getLocation().getDirection().normalize().multiply(boatManager.getPower());

                    tnt.setVelocity(velocity);
                }
            }.runTaskLater(plugin, 1L);

            String cooldownMsg = ChatColor.translateAlternateColorCodes('&', cfg.getString("cooldown-message"));
            String cooldownFinished = ChatColor.translateAlternateColorCodes('&', cfg.getString("cooldown-finished"));

            BossBar bossBar = Bukkit.createBossBar(cooldownMsg, BarColor.WHITE, BarStyle.SOLID);
            bossBar.setProgress(0);
            bossBar.addPlayer(player);

            new BukkitRunnable(){

                int currentTime = 0;
                int cooldownInMs = boatManager.getCooldownInMs();

                @Override
                public void run() {

                    if(v.isDead()){
                        bossBar.removeAll();
                        cancel();
                    }

                    if(currentTime < cooldownInMs){

                        bossBar.setProgress(currentTime/(double)cooldownInMs);

                    }else if(currentTime == cooldownInMs){

                        bossBar.setProgress(1);
                        bossBar.setTitle(cooldownFinished);

                    }else{

                        bossBar.removeAll();
                        cboat.setOnCooldown(false);
                        cancel();

                    }

                    currentTime += 500;

                }

            }.runTaskTimer(plugin, 0L, 10L);

        }

    }
}
