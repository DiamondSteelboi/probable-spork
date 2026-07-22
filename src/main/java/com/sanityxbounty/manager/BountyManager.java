package com.sanityxbounty.manager;

import com.sanityxbounty.SanityXBounty;
import com.sanityxbounty.models.BountyRank;
import com.sanityxbounty.models.PlayerData;
import org.bukkit.entity.Player;

public class BountyManager {

    private final SanityXBounty plugin;

    public BountyManager(SanityXBounty plugin) {
        this.plugin = plugin;
    }

    public void handleRankUpdate(Player player, PlayerData playerData) {
        BountyRank rank = playerData.getBountyRank();
        player.sendMessage("&eYour bounty rank is now: &f" + rank);
    }
}
