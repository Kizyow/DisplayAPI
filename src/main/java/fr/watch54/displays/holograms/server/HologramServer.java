package fr.watch54.displays.holograms.server;

import fr.watch54.displays.holograms.Hologram;
import fr.watch54.displays.interfaces.Action;
import fr.watch54.displays.interfaces.Text;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HologramServer extends Hologram {

    private Map<ArmorStand, Text> armorStandTextMap;

    public HologramServer(List<Text> textList, Location location) {
        super(textList, location);
        this.armorStandTextMap = new HashMap<>();
    }

    @Override
    public void display() {

        if (this.getTextList() == null) throw new NullPointerException("Texts cannot be null!");
        if (this.getLocation() == null) throw new NullPointerException("Location cannot be null!");

        Location locationClone = this.getLocation().clone();
        World world = locationClone.getWorld();

        for (Text text : this.getTextList()) {
            ArmorStand armorStand = (ArmorStand) world.spawnEntity(locationClone, EntityType.ARMOR_STAND);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(text.getText());

            armorStandTextMap.put(armorStand, text);
            locationClone.add(0, -0.3D, 0);

            this.setSpawned(true);
        }
    }

    @Override
    public void update() {
        armorStandTextMap.keySet().forEach(armorStand -> armorStand.setCustomName(armorStandTextMap.get(armorStand).getText()));
    }

    @Override
    public void remove() {
        armorStandTextMap.keySet().forEach(Entity::remove);
        armorStandTextMap.clear();
        this.setSpawned(false);
    }

    @Override
    public void teleport(Location location) {
        this.setLocation(location);
        Location locationClone = location.clone();

        armorStandTextMap.keySet().forEach(armorStand -> {
            armorStand.teleport(locationClone);
            locationClone.add(0, -0.3D, 0);
        });
    }

    @Override
    public void setTextList(List<Text> textList) {
        this.textList = textList;
        this.remove();
        this.display();
    }

}
