package io.wisp.holoitemclear.listener;

import io.wisp.holoitemclear.Main;
import io.wisp.holoitemclear.data.DroppedItemData;
import io.wisp.holoitemclear.utils.Colorize;
import lombok.Setter;
import net.advancedplugins.ae.impl.utils.hooks.plugins.AdvancedEnchantmentsHook;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Setter
public class PlayerListener implements Listener {

    private final DroppedItemData droppedItemData;
    private final List<String> blockEnchantments;
    private final boolean blockEnchantmentsEnabled;
    private final AdvancedEnchantmentsHook advancedEnchantmentsHook;

    private int startTimeClear;
    private String itemText;

    public PlayerListener(DroppedItemData droppedItemData, AdvancedEnchantmentsHook advancedEnchantmentsHook) {
        this.droppedItemData = droppedItemData;
        FileConfiguration config = Main.getInstance().getConfig();

        startTimeClear = config.getInt("time-clear-item");
        itemText = config.getString("item-text");
        blockEnchantments = config.getStringList("advanced-enchantments.block-enchantments");
        blockEnchantmentsEnabled = config.getBoolean("advanced-enchantments.hook");

        this.advancedEnchantmentsHook = advancedEnchantmentsHook;
    }


    @EventHandler
    public void onKillEntity(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        List<ItemStack> drops = new ArrayList<>(event.getDrops());

        event.getDrops().clear();

        for (ItemStack drop : drops) {
            Item droppedItem = entity.getWorld().dropItemNaturally(entity.getLocation(), drop);
            droppedItemData.addItem(droppedItem, startTimeClear);
            droppedItem.setCustomName(Colorize.hex(itemText.replace("%time%", String.valueOf(startTimeClear))));
            droppedItem.setCustomNameVisible(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (event.isCancelled() || player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        Block block = event.getBlock();

        boolean hasBlockedEnchantments = blockEnchantmentsEnabled && advancedEnchantmentsHook != null &&
                blockEnchantments.stream().anyMatch(enchantment ->
                        advancedEnchantmentsHook.getEnchantmentsOnItem(tool).containsKey(enchantment));

        if (!hasBlockedEnchantments) {
            List<ItemStack> drops = new ArrayList<>(block.getDrops(tool));
            event.setDropItems(false);

            for (ItemStack drop : drops) {
                Item droppedItem = block.getWorld().dropItemNaturally(block.getLocation(), drop);
                droppedItemData.addItem(droppedItem, startTimeClear);
                droppedItem.setCustomName(Colorize.hex(itemText.replace("%time%", String.valueOf(startTimeClear))));
                droppedItem.setCustomNameVisible(true);
            }
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockDropItem(BlockDropItemEvent event) {
        if (event.isCancelled()) return;

        for (Item droppedItem : event.getItems()) {
            droppedItemData.addItem(droppedItem, startTimeClear);
            droppedItem.setCustomName(Colorize.hex(itemText.replace("%time%", String.valueOf(startTimeClear))));
            droppedItem.setCustomNameVisible(true);
        }
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
