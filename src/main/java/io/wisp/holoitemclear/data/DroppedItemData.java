package io.wisp.holoitemclear.data;

import lombok.Getter;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.Map;

@Getter
public class DroppedItemData {

    private final Map<Entity, Integer> droppedItems = new HashMap<>();

    public void addItem(Entity item, int time) {
        droppedItems.put(item, time);
    }

    public void removeItem(Entity item) {
        droppedItems.remove(item);
    }

    public void setTimeItem(Entity item, int time) {
        droppedItems.put(item, time);
    }
}
