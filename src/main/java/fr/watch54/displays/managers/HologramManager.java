package fr.watch54.displays.managers;

import fr.watch54.displays.holograms.Hologram;
import fr.watch54.displays.holograms.client.HologramClient;
import fr.watch54.displays.holograms.server.HologramServer;
import fr.watch54.displays.interfaces.Text;
import fr.watch54.displays.listeners.Listeners;
import fr.watch54.displays.tasks.HologramTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HologramManager {

    private Plugin plugin;
    private Map<Block, Hologram> hologramMap;

    public HologramManager(Plugin plugin){
        this.plugin = plugin;
        this.hologramMap = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(new Listeners(this), plugin);
    }

    public HologramServer createServer(List<Text> textList, Location location, boolean refresh){
        Block block = location.getBlock();
        if(this.containsHologram(block)) return (HologramServer) hologramMap.get(block);

        HologramServer hologramServer = new HologramServer(textList, location);
        Bukkit.getScheduler().runTaskLater(plugin, hologramServer::display, 20);

        hologramMap.put(location.getBlock(), hologramServer);

        HologramTask hologramTask = new HologramTask(hologramServer, this, refresh);
        hologramTask.runTaskTimer(plugin, 20, 20);

        return hologramServer;

    }

    public HologramClient createClient(Player player, List<Text> textList, Location location, boolean refresh){
        HologramClient hologram = new HologramClient(player, textList, location);
        Bukkit.getScheduler().runTaskLater(plugin, hologram::display, 20);

        hologramMap.put(location.getBlock(), hologram);

        HologramTask hologramTask = new HologramTask(hologram, this, refresh);
        hologramTask.runTaskTimer(plugin, 20, 20);

        return hologram;

    }

    public void teleport(Hologram hologram, Location location){
        if(!hologramMap.containsValue(hologram)) return;

        hologramMap.entrySet().removeIf(entry -> entry.getValue().equals(hologram));
        hologramMap.put(location.getBlock(), hologram);
        hologram.teleport(location);
    }

    public boolean containsHologram(Block block){
        return hologramMap.keySet().stream().anyMatch(blockMap -> block.getX() == blockMap.getX() && block.getZ() == blockMap.getZ());
    }

    public Optional<Hologram> getHologram(Block block){
        Optional<Block> blockFinal = hologramMap
                .keySet()
                .stream()
                .filter(blockMap -> blockMap.getX() == block.getX() && blockMap.getZ() == block.getZ())
                .findFirst();

        Hologram hologram = null;

        if(blockFinal.isPresent()) hologram = hologramMap.get(blockFinal.get());
        return Optional.ofNullable(hologram);
    }

    public void remove(Hologram hologram){
        if(!hologramMap.containsValue(hologram)) return;

        hologram.remove();
        hologramMap.entrySet().removeIf(entry -> entry.getValue().equals(hologram));
    }

    public void clear() {
        hologramMap.values().forEach(Hologram::remove);
        hologramMap.clear();
    }

    public Map<Block, Hologram> getHologramMap() {
        return hologramMap;
    }
}
