package io.wisp.holoitemclear.config;

import org.bukkit.configuration.ConfigurationSection;

public enum CommonConfig {
    NO_PERMISSION(String.class, "messages.no-permission"),
    TIME_CLEAR_ITEM(Integer.class, "time-clear-item"),
    ITEM_TEXT(String.class, "item-text"),
    USAGE_MESSAGE(String.class, "messages.usage-message"),
    ON_CLEAR_MESSAGE(String.class, "messages.on-clear-message"),
    ITEMS_SETTINGS(ConfigurationSection.class, "items-settings"),
    PARTICLE_TYPE(String.class, "particle-settings.particle-type"),
    PARTICLE_COUNT(Integer.class, "particle-settings.particle-count"),
    OFFSET_X(Integer.class, "particle-settings.offset-x"),
    OFFSET_Y(Integer.class, "particle-settings.offset-y"),
    OFFSET_Z(Integer.class, "particle-settings.offset-z"),
    USE_PARTICLE_ON_CLEAR(Boolean.class, "particle-settings.use-particle-on-clear"),
    SOUND_NAME_ON_CLEAR(String.class, "sound-settings.sound-name-on-clear"),
    USE_SOUND_ON_CLEAR(Boolean.class, "sound-settings.use-sound-on-clear");

    private final Class<?> classType;
    private final String path;

    private ConfigProvider provider;

    CommonConfig(Class<?> classType, String path) {
        this.classType = classType;
        this.path = path;
    }

    public ConfigProvider getProvider() {
        if(provider == null) {
            provider = new ConfigProvider(path, classType);
        }

        return provider;
    }
}
