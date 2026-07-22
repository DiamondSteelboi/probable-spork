package com.sanityxbounty.manager;

import com.sanityxbounty.SanityXBounty;
import com.sanityxbounty.models.PlayerData;
import org.bukkit.entity.Player;

public class SanityManager {

    private final SanityXBounty plugin;

    public SanityManager(SanityXBounty plugin) {
        this.plugin = plugin;
    }

    public void handleUnlawfulKill(Player player, PlayerData playerData) {
        player.sendMessage("&cIllegal kill recorded. Sanity: &f" + playerData.getSanity());
    }
}
