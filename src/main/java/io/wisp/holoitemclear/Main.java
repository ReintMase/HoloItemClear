package io.wisp.holoitemclear;

import io.wisp.holoitemclear.commands.PluginCommands;
import io.wisp.holoitemclear.data.DroppedItemData;
import io.wisp.holoitemclear.data.impl.ItemListener;
import io.wisp.holoitemclear.manager.item.ItemManager;
import io.wisp.holoitemclear.runnable.ClearTimer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;
import java.util.Map;

@Getter
public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        ItemManager itemManager = new ItemManager();
        ItemListener listener = new ItemListener(itemManager);
        getServer().getPluginManager().registerEvents(listener, this);

        ClearTimer clearTimer = new ClearTimer();
        clearTimer.runTaskTimer(this, 20L, 20L);

        PluginCommands commands = new PluginCommands();

        getCommand("holoitemclear").setExecutor(commands);
    }

    @Override
    public void onDisable() {
        DroppedItemData droppedItemData = DroppedItemData.getInstance();
        Iterator<Map.Entry<Entity, Integer>> iterator = droppedItemData.getDroppedItems().entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Entity, Integer> entry = iterator.next();
            Entity item = entry.getKey();
            item.remove();
            iterator.remove();
            droppedItemData.removeItem(item);
        }
    }
}