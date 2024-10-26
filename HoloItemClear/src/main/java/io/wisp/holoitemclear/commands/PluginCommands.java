package io.wisp.holoitemclear.commands;

import io.wisp.holoitemclear.Main;
import io.wisp.holoitemclear.data.DroppedItemData;
import io.wisp.holoitemclear.utils.Colorize;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;

import java.util.Iterator;
import java.util.Map;

public class PluginCommands implements CommandExecutor {

    private final DroppedItemData droppedItemData;
    private final FileConfiguration config;

    public PluginCommands(DroppedItemData droppedItemData) {
        this.droppedItemData = droppedItemData;
        config = Main.getInstance().getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!sender.hasPermission("holoitemclear.admin")) {
            sender.sendMessage(Colorize.hex(config.getString("messages.no-permission")));
            return true;
        }

        if(args.length < 1) {
            sender.sendMessage(Colorize.hex(config.getString("messages.usage-message")));
            return true;
        }

        if(args[0].equalsIgnoreCase("clear")) {
            Iterator<Map.Entry<org.bukkit.entity.Entity, Integer>> iterator = droppedItemData.getDroppedItems().entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<org.bukkit.entity.Entity, Integer> entry = iterator.next();
                Entity item = entry.getKey();
                item.remove();
                iterator.remove();
            }

            sender.sendMessage(Colorize.hex(config.getString("messages.on-clear-message")));
        }

        return true;
    }
}
