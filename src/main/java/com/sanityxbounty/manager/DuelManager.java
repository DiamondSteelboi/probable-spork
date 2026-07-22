package com.sanityxbounty.manager;

import com.sanityxbounty.SanityXBounty;
import com.sanityxbounty.managers.PlayerDataManager;
import com.sanityxbounty.models.Duel;
import com.sanityxbounty.models.PlayerData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.UUID;

public class DuelManager {

    private static final long DEFAULT_DUEL_TIMEOUT_SECONDS = 300L;

    private final SanityXBounty plugin;
    private final PlayerDataManager playerDataManager;

    public DuelManager(SanityXBounty plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
        new BukkitRunnable() {
            @Override
            public void run() {
                cleanupExpiredDuels();
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public boolean challenge(UUID requester, UUID target) {
        if (requester.equals(target)) {
            return false;
        }

        PlayerData requesterData = playerDataManager.getPlayerData(requester);
        PlayerData targetData = playerDataManager.getPlayerData(target);

        if (requesterData.getActiveDuel() != null || targetData.getActiveDuel() != null) {
            return false;
        }

        Duel duel = new Duel(requester, target, false, System.currentTimeMillis() + (DEFAULT_DUEL_TIMEOUT_SECONDS * 1000L));
        requesterData.setActiveDuel(duel);
        targetData.setActiveDuel(duel);
        playerDataManager.savePlayerData(requesterData);
        playerDataManager.savePlayerData(targetData);
        return true;
    }

    public boolean accept(UUID target) {
        PlayerData targetData = playerDataManager.getPlayerData(target);
        Duel duel = targetData.getActiveDuel();
        if (duel == null || duel.isAccepted() || !target.equals(duel.getTarget())) {
            return false;
        }

        duel.setAccepted(true);
        duel.setExpiresAt(System.currentTimeMillis() + (DEFAULT_DUEL_TIMEOUT_SECONDS * 1000L));

        PlayerData requesterData = playerDataManager.getPlayerData(duel.getRequester());
        requesterData.setActiveDuel(duel);
        targetData.setActiveDuel(duel);

        playerDataManager.savePlayerData(requesterData);
        playerDataManager.savePlayerData(targetData);
        return true;
    }

    public boolean deny(UUID target) {
        PlayerData targetData = playerDataManager.getPlayerData(target);
        Duel duel = targetData.getActiveDuel();
        if (duel == null || duel.isAccepted()) {
            return false;
        }

        PlayerData requesterData = playerDataManager.getPlayerData(duel.getRequester());
        requesterData.setActiveDuel(null);
        targetData.setActiveDuel(null);

        playerDataManager.savePlayerData(requesterData);
        playerDataManager.savePlayerData(targetData);
        return true;
    }

    public void cleanupPlayer(UUID uuid) {
        PlayerData playerData = playerDataManager.getPlayerData(uuid);
        Duel duel = playerData.getActiveDuel();
        if (duel == null) {
            return;
        }

        PlayerData other = playerDataManager.getPlayerData(duel.getRequester().equals(uuid) ? duel.getTarget() : duel.getRequester());
        other.setActiveDuel(null);
        playerData.setActiveDuel(null);

        playerDataManager.savePlayerData(other);
        playerDataManager.savePlayerData(playerData);
    }

    public boolean isInDuel(UUID uuid) {
        return getActiveDuel(uuid) != null;
    }

    public Duel getActiveDuel(UUID uuid) {
        PlayerData data = playerDataManager.getPlayerData(uuid);
        Duel duel = data.getActiveDuel();
        if (duel == null) {
            return null;
        }

        if (duel.isExpired(System.currentTimeMillis())) {
            removeDuel(duel);
            return null;
        }

        return duel;
    }

    public void removeDuel(Duel duel) {
        if (duel == null) {
            return;
        }

        PlayerData requesterData = playerDataManager.getPlayerData(duel.getRequester());
        PlayerData targetData = playerDataManager.getPlayerData(duel.getTarget());
        requesterData.setActiveDuel(null);
        targetData.setActiveDuel(null);
        playerDataManager.savePlayerData(requesterData);
        playerDataManager.savePlayerData(targetData);
    }

    public void cleanupExpiredDuels() {
        long now = System.currentTimeMillis();
        Collection<PlayerData> players = playerDataManager.getAllPlayerData();
        for (PlayerData playerData : players) {
            Duel duel = playerData.getActiveDuel();
            if (duel != null && duel.isExpired(now)) {
                removeDuel(duel);
            }
        }
    }
}
