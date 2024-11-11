package io.wisp.holoitemclear.runnable;

import io.wisp.holoitemclear.Main;
import io.wisp.holoitemclear.data.DroppedItemData;
import io.wisp.holoitemclear.manager.ParticleManager;
import io.wisp.holoitemclear.manager.SoundManager;
import io.wisp.holoitemclear.utils.Colorize;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Setter
public class ClearTimer extends BukkitRunnable {

    private final DroppedItemData droppedItemData;
    private final String itemText, soundNameOnClear;
    private final double volumeOnClear, pitchOnClear;

    public ClearTimer(DroppedItemData droppedItemData) {
        this.droppedItemData = droppedItemData;
        FileConfiguration config = Main.getInstance().getConfig();

        itemText = config.getString("item-text");
        soundNameOnClear = config.getString("sound-settings.sound-name-on-clear");
        volumeOnClear = config.getDouble("sound-settings.volume-on-clear");
        pitchOnClear = config.getDouble("sound-settings.pitch-on-clear");
    }

    @Override
    public void run() {
        Iterator<Map.Entry<Entity, Integer>> iterator = droppedItemData.getDroppedItems().entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Entity, Integer> entry = iterator.next();
            Entity item = entry.getKey();
            int itemTime = entry.getValue();

            if (itemTime <= 0) {
                if(Main.getInstance().getConfig().getBoolean("particle-settings.use-particle-on-clear")){
                    ParticleManager.playerParticle(item.getLocation());
                }

                if(Main.getInstance().getConfig().getBoolean("sound-settings.use-sound-on-clear")){
                    double radius = Main.getInstance().getConfig().getDouble("sound-settings.radius");
                    List<Entity> nearbyEntities = item.getNearbyEntities(radius, radius, radius);

                    for(Entity entity : nearbyEntities){
                        if(!(entity instanceof Player)){
                            continue;
                        }

                        Player player = (Player) entity;

                        SoundManager.playSound(item, player, soundNameOnClear, volumeOnClear, pitchOnClear);
                    }

                }

                droppedItemData.removeItem(item);
                item.remove();
                iterator.remove();
            } else {
                droppedItemData.setTimeItem(item, itemTime - 1);
                item.setCustomName(Colorize.hex(itemText.replace("%time%", (itemTime - 1) + "")));
            }
        }
    }
}
