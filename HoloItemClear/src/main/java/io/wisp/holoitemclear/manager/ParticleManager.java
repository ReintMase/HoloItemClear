package io.wisp.holoitemclear.manager;

import io.wisp.holoitemclear.Main;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;

public class ParticleManager {
    public static void playerParticle(Location location){
        FileConfiguration config = Main.getInstance().getConfig();
        String particleType = config.getString("particle-settings.particle-type");
        int particleCount = config.getInt("particle-settings.particle-count");
        int offsetX = config.getInt("particle-settings.offset-x");
        int offsetY = config.getInt("particle-settings.offset-y");
        int offsetZ = config.getInt("particle-settings.offset-z");

        location.getWorld().spawnParticle(Particle.valueOf(particleType), location, particleCount, offsetX, offsetY, offsetZ);
    }
}
