package com.sanityxbounty.managers;

import com.sanityxbounty.SanityXBounty;
import com.sanityxbounty.models.PlayerData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    private static final String STORAGE_FOLDER = "storage";
    private static final String PLAYERS_FILE = "players.yml";

    private final SanityXBounty plugin;
    private final File storageFile;
    private final YamlConfiguration configuration;
    private final Map<UUID, PlayerData> cache;

    public PlayerDataManager(SanityXBounty plugin) {
        this.plugin = plugin;
        this.cache = new ConcurrentHashMap<>();

        File storageFolder = new File(plugin.getDataFolder(), STORAGE_FOLDER);
        if (!storageFolder.exists() && !storageFolder.mkdirs()) {
            plugin.getLogger().warning("Could not create storage folder for player data.");
        }

        this.storageFile = new File(storageFolder, PLAYERS_FILE);
        if (!storageFile.exists()) {
            try {
                if (!storageFile.createNewFile()) {
                    plugin.getLogger().warning("Could not create players.yml storage file.");
                }
            } catch (IOException exception) {
                plugin.getLogger().warning("Failed to create players.yml: " + exception.getMessage());
            }
        }

        this.configuration = YamlConfiguration.loadConfiguration(storageFile);
        loadAll();
    }

    public void loadAll() {
        for (String key : configuration.getKeys(false)) {
            ConfigurationSection section = configuration.getConfigurationSection(key);
            if (section == null) {
                continue;
            }

            PlayerData data = PlayerData.load(section);
            if (data != null) {
                cache.put(data.getUuid(), data);
            }
        }
    }

    public void saveAll() {
        for (Map.Entry<UUID, PlayerData> entry : cache.entrySet()) {
            savePlayerDataToConfig(entry.getValue());
        }
        saveConfiguration();
    }

    public PlayerData getPlayerData(UUID uuid) {
        return cache.computeIfAbsent(uuid, PlayerData::new);
    }

    public void savePlayerData(PlayerData playerData) {
        cache.put(playerData.getUuid(), playerData);
        savePlayerDataToConfig(playerData);
        saveConfiguration();
    }

    public void removePlayerData(UUID uuid) {
        cache.remove(uuid);
        configuration.set(uuid.toString(), null);
        saveConfiguration();
    }

    public Collection<PlayerData> getAllPlayerData() {
        return cache.values();
    }

    private void savePlayerDataToConfig(PlayerData playerData) {
        ConfigurationSection section = configuration.createSection(playerData.getUuid().toString());
        playerData.save(section);
    }

    private void saveConfiguration() {
        try {
            configuration.save(storageFile);
        } catch (IOException exception) {
            plugin.getLogger().warning("Could not save players.yml: " + exception.getMessage());
        }
    }
}
