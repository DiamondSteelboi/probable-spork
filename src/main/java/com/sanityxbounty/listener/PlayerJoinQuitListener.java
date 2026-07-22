package com.sanityxbounty.listener;

import com.sanityxbounty.SanityXBounty;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    private final SanityXBounty plugin;

    public PlayerJoinQuitListener(SanityXBounty plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getPlayerDataManager().getPlayerData(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();
        plugin.getDuelManager().cleanupPlayer(player.getUniqueId());
        var playerData = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        playerData.getCombatWindow().setCombatExpiry(0L);
        plugin.getPlayerDataManager().savePlayerData(playerData);
    }
}
