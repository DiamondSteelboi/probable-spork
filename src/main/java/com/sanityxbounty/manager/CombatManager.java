package com.sanityxbounty.manager;

import com.sanityxbounty.SanityXBounty;
import com.sanityxbounty.managers.PlayerDataManager;
import com.sanityxbounty.models.PlayerData;

import java.util.UUID;

public class CombatManager {

    private static final long COMBAT_TAG_SECONDS = 15L;

    private final SanityXBounty plugin;
    private final PlayerDataManager playerDataManager;

    public CombatManager(SanityXBounty plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }

    public void tagCombat(UUID attacker, UUID victim) {
        long expiry = System.currentTimeMillis() + (COMBAT_TAG_SECONDS * 1000L);

        PlayerData attackerData = playerDataManager.getPlayerData(attacker);
        attackerData.getCombatWindow().setCombatExpiry(expiry);
        playerDataManager.savePlayerData(attackerData);

        PlayerData victimData = playerDataManager.getPlayerData(victim);
        victimData.getCombatWindow().setCombatExpiry(expiry);
        playerDataManager.savePlayerData(victimData);
    }

    public boolean isInCombat(UUID uuid) {
        PlayerData data = playerDataManager.getPlayerData(uuid);
        return data.getCombatWindow().getCombatExpiry() > System.currentTimeMillis();
    }
}
