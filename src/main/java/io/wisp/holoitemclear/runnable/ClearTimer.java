package io.wisp.holoitemclear.runnable;

import io.wisp.holoitemclear.Main;
import io.wisp.holoitemclear.config.CommonConfig;
import io.wisp.holoitemclear.data.DroppedItemData;
import io.wisp.holoitemclear.manager.ParticleManager;
import io.wisp.holoitemclear.manager.SoundManager;
import io.wisp.holoitemclear.utils.Colorize;
import lombok.Setter;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Setter
public class ClearTimer extends BukkitRunnable {

    @Override
    public void run() {
        DroppedItemData droppedItemData = DroppedItemData.getInstance();
        Iterator<Map.Entry<Entity, Integer>> iterator = droppedItemData.getDroppedItems().entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Entity, Integer> entry = iterator.next();
            Entity item = entry.getKey();
            int itemTime = entry.getValue();

            if (itemTime <= 0) {
                boolean useParticleOnClear = CommonConfig.USE_PARTICLE_ON_CLEAR.getProvider().getValue();
                if (useParticleOnClear) {
                    ParticleManager.playerParticle(item.getLocation());
                }

                boolean useSoundOnClear = CommonConfig.USE_SOUND_ON_CLEAR.getProvider().getValue();
                if (useSoundOnClear) {
                    double radius = Main.getInstance().getConfig().getDouble("sound-settings.radius");
                    double pitchOnClear = Main.getInstance().getConfig().getDouble("sound-settings.pitch-on-clear");
                    double volumeOnClear = Main.getInstance().getConfig().getDouble("sound-settings.volume-on-clear");

                    List<Entity> nearbyEntities = item.getNearbyEntities(radius, radius, radius);
                    nearbyEntities.forEach(player -> {
                        if (player instanceof Player) {
                            SoundManager.playSound(
                                    item,
                                    (Player) player,
                                    CommonConfig.SOUND_NAME_ON_CLEAR.getProvider().getValue(),
                                    volumeOnClear,
                                    pitchOnClear
                            );
                        }
                    });
                }

                Chunk itemChunk = item.getLocation().getChunk();
                itemChunk.load();

                item.remove();
                iterator.remove();
                droppedItemData.removeItem(item);
                itemChunk.unload();
            } else {
                droppedItemData.setTimeItem(item, itemTime - 1);
                item.setCustomName(Colorize.hex(CommonConfig.ITEM_TEXT.getProvider().getValue()
                        .toString()
                        .replace("%time%", String.valueOf(itemTime - 1))));
            }
        }
    }

}
