package fr.watch54.displays.holograms;

import fr.watch54.displays.interfaces.Action;
import fr.watch54.displays.interfaces.Text;
import org.bukkit.Location;

import java.util.List;

public abstract class Hologram {

    protected List<Text> textList;
    protected Location location;
    protected boolean spawned;

    public Hologram(List<Text> textList, Location location){
        this.textList = textList;
        this.location = location;
        this.spawned = false;
    }

    public List<Text> getTextList(){
        return textList;
    }

    public void setTextList(List<Text> textList){
        this.textList = textList;
    }

    public Location getLocation(){
        return location;

    }

    public void setLocation(Location location){
        this.location = location;
    }

    public boolean isSpawned(){
        return spawned;
    }

    public void setSpawned(boolean spawned){
        this.spawned = spawned;
    }

    public abstract void display();

    public abstract void update();

    public abstract void remove();

    public abstract void interact(Action action);

    public void teleport(Location location){
        this.update();
    }

}
