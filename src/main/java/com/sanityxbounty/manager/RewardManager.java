package com.sanityxbounty.manager;

import com.sanityxbounty.SanityXBounty;
import com.sanityxbounty.models.BountyRank;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class RewardManager {

    private static final long COOLDOWN_MILLIS = 120L * 1000L;

    private final SanityXBounty plugin;
    private final Random random;
    private final Map<UUID, Long> victimCooldowns;

    public RewardManager(SanityXBounty plugin) {
        this.plugin = plugin;
        this.random = new Random();
        this.victimCooldowns = new HashMap<>();
    }

    public void giveIllegalKillReward(Player killer, Player victim, BountyRank rank) {
        if (killer == null || victim == null) {
            return;
        }

        UUID victimId = victim.getUniqueId();
        long now = System.currentTimeMillis();
        long last = victimCooldowns.getOrDefault(victimId, 0L);
        if (now - last < COOLDOWN_MILLIS) {
            return;
        }

        victimCooldowns.put(victimId, now);
        ItemStack reward = selectReward(rank);
        if (reward != null) {
            victim.getWorld().dropItemNaturally(victim.getLocation(), reward);
        }
    }

    private ItemStack selectReward(BountyRank rank) {
        return switch (rank) {
            case PUBLIC_ENEMY -> weightedPick(new Object[][]{
                {Material.DIAMOND, 10},
                {Material.GOLD_INGOT, 30},
                {Material.IRON_INGOT, 40},
                {Material.COOKED_BEEF, 20}
            });
            case OUTLAW -> weightedPick(new Object[][]{
                {Material.GOLD_INGOT, 25},
                {Material.IRON_INGOT, 35},
                {Material.COOKED_BEEF, 30},
                {Material.ARROW, 10}
            });
            case SUSPECT -> weightedPick(new Object[][]{
                {Material.IRON_INGOT, 20},
                {Material.COOKED_BEEF, 40},
                {Material.ARROW, 20},
                {Material.STRING, 20}
            });
            default -> null;
        };
    }

    private ItemStack weightedPick(Object[][] options) {
        int totalWeight = 0;
        for (Object[] option : options) {
            totalWeight += (Integer) option[1];
        }

        int roll = random.nextInt(totalWeight);
        int current = 0;
        for (Object[] option : options) {
            current += (Integer) option[1];
            if (roll < current) {
                return new ItemStack((Material) option[0], 1);
            }
        }
        return null;
    }
}
