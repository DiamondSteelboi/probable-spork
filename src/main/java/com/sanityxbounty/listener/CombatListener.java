package com.sanityxbounty.listener;

import com.sanityxbounty.SanityXBounty;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatListener implements Listener {

    private final SanityXBounty plugin;

    public CombatListener(SanityXBounty plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        if (!(event.getDamager() instanceof Player attacker)) {
            return;
        }

        plugin.getCombatManager().tagCombat(attacker.getUniqueId(), victim.getUniqueId());
        plugin.getSelfDefenseManager().grantSelfDefense(attacker.getUniqueId(), victim.getUniqueId());
    }
}
