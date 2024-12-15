package io.wisp.holoitemclear.data.impl;

import io.wisp.holoitemclear.Main;
import io.wisp.holoitemclear.config.CommonConfig;
import io.wisp.holoitemclear.data.DroppedItemData;
import io.wisp.holoitemclear.manager.item.ItemManager;
import io.wisp.holoitemclear.utils.Colorize;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

import java.util.Set;

@Setter
public class ItemListener implements Listener {

    private final ItemManager itemManager;
    public ItemListener(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        Item droppedItem = event.getEntity();
        Set<String> itemsSettings = Main.getInstance().getConfig().getConfigurationSection("items-settings").getKeys(false);

        itemsSettings.forEach(itemSettingsKey -> {
            int startClearItem;

            if (droppedItem.getItemStack().getType() == Material.valueOf(itemSettingsKey)) {
                startClearItem = Main.getInstance().getConfig().getInt("items-settings." + itemSettingsKey + ".start-clear-item");
                itemManager.addCheckItem(droppedItem, startClearItem);
                return;
            }

            startClearItem = CommonConfig.TIME_CLEAR_ITEM.getProvider().getValue();
            itemManager.addCheckItem(droppedItem, startClearItem);
        });
    }
}
