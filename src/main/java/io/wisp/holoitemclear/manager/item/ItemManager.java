package io.wisp.holoitemclear.manager.item;

import io.wisp.holoitemclear.config.CommonConfig;
import io.wisp.holoitemclear.data.DroppedItemData;
import io.wisp.holoitemclear.utils.Colorize;
import org.bukkit.entity.Item;

public class ItemManager {
    public void addCheckItem(Item item, int startTimeClear) {
        DroppedItemData droppedItemData = DroppedItemData.getInstance();

        item.setCustomName(Colorize.hex(CommonConfig.ITEM_TEXT.getProvider().getValue().toString()
                .replace("%time%", String.valueOf(startTimeClear))));
        item.setCustomNameVisible(true);

        droppedItemData.addItem(item, startTimeClear);
    }
}
