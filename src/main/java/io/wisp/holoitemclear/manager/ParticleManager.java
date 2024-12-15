package io.wisp.holoitemclear.manager;

import io.wisp.holoitemclear.config.CommonConfig;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleManager {
    public static void playerParticle(Location location) {
        String particleType = CommonConfig.PARTICLE_TYPE.getProvider().getValue();
        int particleCount = CommonConfig.PARTICLE_COUNT.getProvider().getValue();
        int offsetX = CommonConfig.OFFSET_X.getProvider().getValue();
        int offsetY = CommonConfig.OFFSET_Y.getProvider().getValue();
        int offsetZ = CommonConfig.OFFSET_Z.getProvider().getValue();
        if (location.getWorld() == null) return;

        location.getWorld().spawnParticle(
                Particle.valueOf(particleType),
                location,
                particleCount,
                offsetX, offsetY, offsetZ);
    }
}
