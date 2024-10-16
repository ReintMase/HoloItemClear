package io.wisp.holoitemclear.runnable;

import io.wisp.holoitemclear.Main;
import io.wisp.holoitemclear.data.DroppedItemData;
import io.wisp.holoitemclear.particles.ParticleManager;
import io.wisp.holoitemclear.utils.Colorize;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map;

@Setter
public class ClearTimer extends BukkitRunnable {

    private final DroppedItemData droppedItemData;
    private String itemText;

    public ClearTimer(DroppedItemData droppedItemData) {
        this.droppedItemData = droppedItemData;
        FileConfiguration config = Main.getInstance().getConfig();

        itemText = config.getString("item-text");
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

                item.remove();
                iterator.remove();
            } else {
                droppedItemData.setTimeItem(item, itemTime - 1);
                item.setCustomName(Colorize.hex(itemText.replace("%time%", (itemTime - 1) + "")));
            }
        }
    }
}
