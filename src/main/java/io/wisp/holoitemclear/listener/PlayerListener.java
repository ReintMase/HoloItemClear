package io.wisp.holoitemclear.listener;

import io.wisp.holoitemclear.Main;
import io.wisp.holoitemclear.data.DroppedItemData;
import io.wisp.holoitemclear.utils.Colorize;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

@Setter
public class PlayerListener implements Listener {

    private final DroppedItemData droppedItemData;

    private int startTimeClear;
    private String itemText;

    public PlayerListener(DroppedItemData droppedItemData) {
        this.droppedItemData = droppedItemData;
        FileConfiguration config = Main.getInstance().getConfig();

        startTimeClear = config.getInt("time-clear-item");
        itemText = config.getString("item-text");
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        Entity item = event.getItemDrop();
        item.setCustomName(Colorize.hex(itemText.replace("%time%", String.valueOf(startTimeClear))));
        item.setCustomNameVisible(true);
        droppedItemData.addItem(item, startTimeClear);
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        Entity item = event.getItem();
        item.setCustomNameVisible(false);
        droppedItemData.removeItem(item);
    }
}
