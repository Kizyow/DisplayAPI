package fr.watch54.displays.tasks;

import fr.watch54.displays.managers.HologramManager;
import fr.watch54.displays.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class HologramTask extends BukkitRunnable {

    private Hologram hologram;
    private HologramManager hologramManager;
    private boolean refresh;

    public HologramTask(Hologram hologram, HologramManager hologramManager, boolean refresh){
        this.hologram = hologram;
        this.hologramManager = hologramManager;
        this.refresh = refresh;
    }

    @Override
    public void run(){
        if(Bukkit.getOnlinePlayers().size() < 1 && hologram.isSpawned()){
            cancel();
            hologramManager.remove(hologram);
            return;
        }
        if(refresh) hologram.update();
    }

}
