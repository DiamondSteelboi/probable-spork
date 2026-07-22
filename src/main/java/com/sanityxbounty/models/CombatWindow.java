package com.sanityxbounty.models;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatWindow {

    private long combatExpiry;
    private final Map<UUID, Long> selfDefenseExpirations;

    public CombatWindow() {
        this.selfDefenseExpirations = new HashMap<>();
    }

    public long getCombatExpiry() {
        return combatExpiry;
    }

    public void setCombatExpiry(long combatExpiry) {
        this.combatExpiry = combatExpiry;
    }

    public void addSelfDefense(UUID attacker, long expiresAt) {
        this.selfDefenseExpirations.put(attacker, expiresAt);
    }

    public Long getSelfDefenseExpiry(UUID attacker) {
        return selfDefenseExpirations.get(attacker);
    }

    public Map<UUID, Long> getSelfDefenseExpirations() {
        return new HashMap<>(selfDefenseExpirations);
    }

    public void save(ConfigurationSection section) {
        section.set("combat-expiry", combatExpiry);
        ConfigurationSection selfDefenseSection = section.createSection("self-defense");
        for (Map.Entry<UUID, Long> entry : selfDefenseExpirations.entrySet()) {
            selfDefenseSection.set(entry.getKey().toString(), entry.getValue());
        }
    }

    public static CombatWindow load(ConfigurationSection section) {
        CombatWindow window = new CombatWindow();
        if (section == null) {
            return window;
        }

        window.setCombatExpiry(section.getLong("combat-expiry", 0L));
        ConfigurationSection selfDefenseSection = section.getConfigurationSection("self-defense");
        if (selfDefenseSection != null) {
            for (String key : selfDefenseSection.getKeys(false)) {
                UUID attacker = UUID.fromString(key);
                long expiry = selfDefenseSection.getLong(key, 0L);
                window.addSelfDefense(attacker, expiry);
            }
        }

        return window;
    }
}
