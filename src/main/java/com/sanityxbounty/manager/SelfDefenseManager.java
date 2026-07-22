package com.sanityxbounty.manager;

import com.sanityxbounty.SanityXBounty;
import com.sanityxbounty.managers.PlayerDataManager;
import com.sanityxbounty.models.PlayerData;

import java.util.UUID;

public class SelfDefenseManager {

    private static final long SELF_DEFENSE_SECONDS = 60L;

    private final SanityXBounty plugin;
    private final PlayerDataManager playerDataManager;

    public SelfDefenseManager(SanityXBounty plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }

    public void grantSelfDefense(UUID attacker, UUID defender) {
        PlayerData defenderData = playerDataManager.getPlayerData(defender);
        long expiry = System.currentTimeMillis() + (SELF_DEFENSE_SECONDS * 1000L);
        defenderData.getCombatWindow().addSelfDefense(attacker, expiry);
        playerDataManager.savePlayerData(defenderData);
    }

    public boolean hasSelfDefense(UUID defender, UUID attacker) {
        PlayerData defenderData = playerDataManager.getPlayerData(defender);
        Long expiry = defenderData.getCombatWindow().getSelfDefenseExpiry(attacker);
        return expiry != null && expiry > System.currentTimeMillis();
    }
}
