package io.wisp.holoitemclear.config;

import io.wisp.holoitemclear.Main;
import io.wisp.holoitemclear.utils.Colorize;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ConfigProvider {
    protected final String path;
    protected final Class<?> classType;

    public ConfigProvider(String path, Class<?> classType) {
        this.path = path;
        this.classType = classType;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        FileConfiguration config = Main.getInstance().getConfig();
        Object value = config.get(path);

        if (value == null) {
            Bukkit.getLogger().warning("[HIC] Value on: " + path + " is null, please, create this path and add some value!");
            return null;
        }

        if (!value.getClass().equals(classType)) {
            Bukkit.getLogger().warning("[HIC] Value on: " + path + " is not correct type!");
            return null;
        }

        return (T) value;
    }

    private  <T> void also(Class<T> classType, Consumer<T> actionInBukkitThread) {
        T value = getValue();
        actionInBukkitThread.accept(classType.cast(value));
    }

    @SuppressWarnings("unchecked")
    public void sendMessage(UUID playerUUID) {
        if (classType == String.class) {
            also(String.class, message ->
                    sendPlayerMessage(playerUUID, String.format(Colorize.hex(message))
                    ));
        } else {
            also(List.class, messages -> {
                messages.forEach(msg -> sendPlayerMessage(playerUUID,
                        String.format(Colorize.hex((String) msg))));
            });
        }
    }

    private void sendPlayerMessage(UUID playerUUID, String message) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;

        player.sendMessage(message);
    }
}
