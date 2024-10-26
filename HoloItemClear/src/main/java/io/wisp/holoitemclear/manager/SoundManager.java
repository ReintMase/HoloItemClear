package io.wisp.holoitemclear.manager;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SoundManager {
    public static void playSound(Entity entity, Player player, String soundName, double volume, double pitch) {
        try {
            Sound sound = Sound.valueOf(soundName);
            player.playSound(entity.getLocation(), sound, (float) volume, (float) pitch);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: Звук с именем " + soundName + " не найден.");
        }
    }
}
