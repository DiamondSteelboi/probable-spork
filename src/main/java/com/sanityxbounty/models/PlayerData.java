package com.sanityxbounty.models;

import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private int unlawfulKills;
    private Duel activeDuel;
    private CombatWindow combatWindow;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.unlawfulKills = 0;
        this.combatWindow = new CombatWindow();
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getUnlawfulKills() {
        return unlawfulKills;
    }

    public void setUnlawfulKills(int unlawfulKills) {
        this.unlawfulKills = unlawfulKills;
    }

    public void incrementUnlawfulKills() {
        this.unlawfulKills++;
    }

    public Duel getActiveDuel() {
        return activeDuel;
    }

    public void setActiveDuel(Duel activeDuel) {
        this.activeDuel = activeDuel;
    }

    public CombatWindow getCombatWindow() {
        return combatWindow;
    }

    public void setCombatWindow(CombatWindow combatWindow) {
        this.combatWindow = combatWindow;
    }

    public int getSanity() {
        return switch (unlawfulKills) {
            case 0 -> 100;
            case 1 -> 75;
            case 2 -> 50;
            default -> 0;
        };
    }

    public BountyRank getBountyRank() {
        if (unlawfulKills == 0) {
            return BountyRank.NONE;
        }
        if (unlawfulKills == 1) {
            return BountyRank.SUSPECT;
        }
        if (unlawfulKills == 2) {
            return BountyRank.OUTLAW;
        }
        return BountyRank.PUBLIC_ENEMY;
    }

    public void save(ConfigurationSection section) {
        section.set("uuid", uuid.toString());
        section.set("unlawful-kills", unlawfulKills);

        if (activeDuel != null) {
            ConfigurationSection duelSection = section.createSection("active-duel");
            activeDuel.save(duelSection);
        }

        if (combatWindow != null) {
            ConfigurationSection combatSection = section.createSection("combat-window");
            combatWindow.save(combatSection);
        }
    }

    public static PlayerData load(ConfigurationSection section) {
        if (section == null || !section.contains("uuid")) {
            return null;
        }

        UUID uuid = UUID.fromString(section.getString("uuid"));
        PlayerData playerData = new PlayerData(uuid);
        playerData.setUnlawfulKills(section.getInt("unlawful-kills", 0));
        playerData.setActiveDuel(Duel.load(section.getConfigurationSection("active-duel")));
        playerData.setCombatWindow(CombatWindow.load(section.getConfigurationSection("combat-window")));
        return playerData;
    }
}
