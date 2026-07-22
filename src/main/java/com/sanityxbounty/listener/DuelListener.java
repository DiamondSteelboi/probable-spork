package com.sanityxbounty.listener;

import com.sanityxbounty.SanityXBounty;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class DuelListener implements Listener {

    private final SanityXBounty plugin;

    public DuelListener(SanityXBounty plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Duel cleanup logic will be implemented later.
    }
}
