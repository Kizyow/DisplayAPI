package fr.watch54.displays.listeners;

import fr.watch54.displays.holograms.client.HologramClient;
import fr.watch54.displays.holograms.server.HologramServer;
import fr.watch54.displays.managers.HologramManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) gameszaum, all rights reserved, unauthorized
 * utlization or copy of this file, is strictly prohibited and
 * liable to civil and criminal penalties, the project 'DisplayAPI'
 * is privated and the re-sale without contact with me (gameszaum) is not allowed.
 */
public class Listeners implements Listener {

    private HologramManager hologramManager;

    public Listeners(HologramManager hologramManager) {
        this.hologramManager = hologramManager;
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void clientSide(PlayerInteractEvent event) {
        if (hologramManager.getHologramMap().size() > 0 && hologramManager.getHologramMap().values().stream().anyMatch(hologram -> hologram instanceof HologramClient)) {
            for (Block block : getNearbyBlocks(event.getPlayer(), 4)) {
                if (hologramManager.containsHologram(block)) {
                    hologramManager.getHologram(block).filter(hologram -> hologram instanceof HologramClient).map(hologram -> (HologramClient) hologram).ifPresent(hologram -> {
                        if (hologram.getPlayer().getName().equals(event.getPlayer().getName())) {
                            if (hologram.getAction() != null) {
                                hologram.getAction().execute(event.getPlayer());
                            }
                        }
                    });
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void serverSide(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (entity instanceof ArmorStand) {
            Block block = entity.getLocation().getBlock();

            if (hologramManager.containsHologram(block)) {
                hologramManager.getHologram(block).filter(hologram -> hologram instanceof HologramServer).map(hologram -> (HologramServer) hologram).ifPresent(hologram -> {
                    if (hologram.getAction() != null) {
                        hologram.getAction().execute(player);
                    }
                });
            }
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        event.setCancelled(true);
    }

    private List<Block> getNearbyBlocks(Player player, int radius) {
        List<Block> blocks = new ArrayList<>();
        Location location = player.getLocation();

        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

}
