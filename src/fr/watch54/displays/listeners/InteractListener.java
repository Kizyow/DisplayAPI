package fr.watch54.displays.listeners;

import fr.watch54.displays.holograms.Hologram;
import fr.watch54.displays.holograms.HologramServer;
import fr.watch54.displays.interfaces.Action;
import fr.watch54.displays.managers.HologramManager;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Optional;

public class InteractListener implements Listener {

    private HologramManager hologramManager;

    public InteractListener(HologramManager hologramManager){
        this.hologramManager = hologramManager;

    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event){

        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if(entity instanceof ArmorStand){

            Block block = entity.getLocation().getBlock();

            if(hologramManager.containsHologram(block)){

                Optional<Hologram> optionalHologram = hologramManager.getHologram(block);

                optionalHologram.ifPresent(hologram -> {

                    if(hologram instanceof HologramServer){

                        HologramServer server = (HologramServer) hologram;
                        Action action = server.getAction();

                        if(action != null) action.execute(player);

                    }

                });

            }

        }

    }

}
