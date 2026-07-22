package com.sanityxbounty.listener;

import com.sanityxbounty.SanityXBounty;
import com.sanityxbounty.models.Duel;
import com.sanityxbounty.models.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerSanityListener implements Listener {

    private final SanityXBounty plugin;

    public PlayerSanityListener(SanityXBounty plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        if (killer == null) {
            return;
        }

        PlayerData victimData = plugin.getPlayerDataManager().getPlayerData(victim.getUniqueId());
        PlayerData killerData = plugin.getPlayerDataManager().getPlayerData(killer.getUniqueId());

        Duel duel = plugin.getDuelManager().getActiveDuel(killer.getUniqueId());
        if (duel != null && duel.isAccepted() && ((duel.getRequester().equals(killer.getUniqueId()) && duel.getTarget().equals(victim.getUniqueId())) || (duel.getRequester().equals(victim.getUniqueId()) && duel.getTarget().equals(killer.getUniqueId())))) {
            return;
        }

        if (plugin.getSelfDefenseManager().hasSelfDefense(victim.getUniqueId(), killer.getUniqueId())) {
            return;
        }

        killerData.incrementUnlawfulKills();
        plugin.getPlayerDataManager().savePlayerData(killerData);

        plugin.getSanityManager().handleUnlawfulKill(killer, killerData);
        plugin.getBountyManager().handleRankUpdate(killer, killerData);
        plugin.getRewardManager().giveIllegalKillReward(killer, victim, victimData.getBountyRank());

        victimData.getCombatWindow().setCombatExpiry(0L);
        killerData.getCombatWindow().setCombatExpiry(0L);
        plugin.getPlayerDataManager().savePlayerData(victimData);
        plugin.getPlayerDataManager().savePlayerData(killerData);

        Duel activeDuel = plugin.getDuelManager().getActiveDuel(killer.getUniqueId());
        if (activeDuel == null) {
            activeDuel = plugin.getDuelManager().getActiveDuel(victim.getUniqueId());
        }
        plugin.getDuelManager().removeDuel(activeDuel);
    }
}
