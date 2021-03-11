package fr.watch54.displays.holograms.client;

import fr.watch54.displays.holograms.Hologram;
import fr.watch54.displays.interfaces.Action;
import fr.watch54.displays.interfaces.Text;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HologramClient extends Hologram {

    private Player player;
    private Map<EntityArmorStand, Text> entityArmorStandTextMap;

    public HologramClient(Player player, List<Text> textList, Location location){
        super(textList, location);
        this.player = player;
        this.entityArmorStandTextMap = new HashMap<>();
    }

    @Override
    public void display(){
        if(player == null) throw new NullPointerException("Player cannot be null!");
        if(this.getTextList() == null) throw new NullPointerException("Texts cannot be null!");
        if(this.getLocation() == null) throw new NullPointerException("Location cannot be null!");

        Location locationClone = this.getLocation().clone();

        CraftPlayer craftPlayer = (CraftPlayer) player;
        CraftWorld craftWorld = (CraftWorld) locationClone.getWorld();

        for(Text text : this.getTextList()){
            EntityArmorStand armorStand = new EntityArmorStand(craftWorld.getHandle(), locationClone.getX(), locationClone.getY(), locationClone.getZ());
            armorStand.setGravity(false);
            armorStand.setInvisible(true);
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(text.getText());
            armorStand.fireTicks = Integer.MAX_VALUE;

            entityArmorStandTextMap.put(armorStand, text);
            locationClone.add(0, -0.3D, 0);

            PacketPlayOutSpawnEntityLiving entityLiving = new PacketPlayOutSpawnEntityLiving(armorStand);
            craftPlayer.getHandle().playerConnection.sendPacket(entityLiving);

            this.setSpawned(true);
        }
    }

    @Override
    public void update(){
        this.remove();
        this.display();
    }

    @Override
    public void remove(){
        CraftPlayer craftPlayer = (CraftPlayer) player;

        List<EntityArmorStand> armorStandClone = new ArrayList<>(entityArmorStandTextMap.keySet());
        for(EntityArmorStand armorStand : armorStandClone){

            PacketPlayOutEntityDestroy entityDestroy = new PacketPlayOutEntityDestroy(armorStand.getId());
            craftPlayer.getHandle().playerConnection.sendPacket(entityDestroy);
            entityArmorStandTextMap.clear();

            this.setSpawned(false);
        }
    }

    @Override
    public void interact(Action action) {
        BlockIterator iter = new BlockIterator(player, 3);
        while (iter.hasNext()) {
            if (iter.next().equals(location.getBlock())) {
                action.execute(player);
            }
        }
        /*
        final int[] id = {0};
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler(){
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                if(packet instanceof PacketPlayInUseEntity){
                    PacketPlayInUseEntity useEntity = (PacketPlayInUseEntity) packet;

                    if (useEntity.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) return;

                    Field entityIDField = useEntity.getClass().getDeclaredField("a");
                    entityIDField.setAccessible(true);

                    for(EntityArmorStand entityArmorStand : entityArmorStandTextMap.keySet()){
                        if(entityIDField.get(useEntity).equals(entityArmorStand.getId())){
                            action.execute(player);
                            id[0] = entityArmorStand.getId();
                            break;
                        }
                    }
                }
                super.channelRead(channelHandlerContext, packet);
            }
        };
        ChannelPipeline channelPipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        channelPipeline.addBefore("packet_handler", player.getName() + "/" + id[0], channelDuplexHandler);*/
    }

    @Override
    public void teleport(Location location){
        this.setLocation(location);
        Location locationClone = location.clone();

        entityArmorStandTextMap.keySet().forEach(armorStand -> {
            armorStand.setPosition(locationClone.getX(), location.getY(), location.getZ());
            locationClone.add(0, -0.3D, 0);
        });
    }

    @Override
    public void setTextList(List<Text> textList){
        this.textList = textList;
        this.update();

    }

}
