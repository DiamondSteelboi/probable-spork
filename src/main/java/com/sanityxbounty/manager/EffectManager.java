package com.sanityxbounty.manager;

import com.sanityxbounty.SanityXBounty;
import com.sanityxbounty.managers.PlayerDataManager;
import com.sanityxbounty.models.BountyRank;
import com.sanityxbounty.models.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class EffectManager {

    private static final long EFFECT_INTERVAL_TICKS = 20L * 180;
    private static final long HEART_TOGGLE_INTERVAL_TICKS = 20L * 15;

    private final SanityXBounty plugin;
    private final PlayerDataManager playerDataManager;
    private final Random random;
    private boolean heartVisible;

    public EffectManager(SanityXBounty plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
        this.random = new Random();
        this.heartVisible = false;

        new BukkitRunnable() {
            @Override
            public void run() {
                applyPeriodicEffects();
            }
        }.runTaskTimer(plugin, EFFECT_INTERVAL_TICKS, EFFECT_INTERVAL_TICKS);

        new BukkitRunnable() {
            @Override
            public void run() {
                togglePublicEnemyHearts();
            }
        }.runTaskTimer(plugin, HEART_TOGGLE_INTERVAL_TICKS, HEART_TOGGLE_INTERVAL_TICKS);
    }

    private void applyPeriodicEffects() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            PlayerData data = playerDataManager.getPlayerData(player.getUniqueId());
            BountyRank rank = data.getBountyRank();
            switch (rank) {
                case SUSPECT -> applySuspectEffects(player);
                case OUTLAW -> applyOutlawEffects(player);
                case PUBLIC_ENEMY -> applyPublicEnemyEffects(player);
                default -> {
                }
            }
        }
    }

    private void applySuspectEffects(Player player) {
        int durationSeconds = 1 + random.nextInt(2);
        player.addPotionEffect(org.bukkit.potion.PotionEffectType.DARKNESS.createEffect(durationSeconds * 20, 0), true);
        player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 0.7f, 1.0f);
        player.spawnParticle(Particle.LARGE_SMOKE, player.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 0.02);
        if (random.nextBoolean()) {
            player.getWorld().spawn(player.getLocation().add(0, 0.5, 0), org.bukkit.entity.Phantom.class, phantom -> phantom.setPersistent(false));
        }
    }

    private void applyOutlawEffects(Player player) {
        player.addPotionEffect(org.bukkit.potion.PotionEffectType.WEAKNESS.createEffect(20 * 5, 0), true);
        player.addPotionEffect(org.bukkit.potion.PotionEffectType.HUNGER.createEffect(20 * 5, 0), true);
        if (random.nextInt(3) == 0) {
            player.addPotionEffect(org.bukkit.potion.PotionEffectType.DARKNESS.createEffect((1 + random.nextInt(2)) * 20, 0), true);
            player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 0.7f, 1.0f);
            player.spawnParticle(Particle.LARGE_SMOKE, player.getLocation().add(0, 1, 0), 12, 0.5, 0.5, 0.5, 0.03);
            player.getWorld().strikeLightningEffect(player.getLocation());
        }
    }

    private void applyPublicEnemyEffects(Player player) {
        player.addPotionEffect(org.bukkit.potion.PotionEffectType.WEAKNESS.createEffect(20 * 5, 0), true);
        player.addPotionEffect(org.bukkit.potion.PotionEffectType.HUNGER.createEffect(20 * 5, 0), true);
        player.addPotionEffect(org.bukkit.potion.PotionEffectType.MINING_FATIGUE.createEffect(20 * 5, 0), true);
        if (random.nextInt(4) == 0) {
            int durationSeconds = 2 + random.nextInt(2);
            if (!plugin.getCombatManager().isInCombat(player.getUniqueId())) {
                player.addPotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS.createEffect(durationSeconds * 20, 0), true);
            }
            player.spawnParticle(Particle.SOUL, player.getLocation().add(0, 1, 0), 15, 0.5, 0.5, 0.5, 0.03);
            player.spawnParticle(Particle.LARGE_SMOKE, player.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.04);
            player.getWorld().strikeLightningEffect(player.getLocation());
        }
    }

    private void togglePublicEnemyHearts() {
        heartVisible = !heartVisible;
        if (!heartVisible) {
            return;
        }
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            PlayerData data = playerDataManager.getPlayerData(player.getUniqueId());
            if (data.getBountyRank() == BountyRank.PUBLIC_ENEMY) {
                player.spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 8, 0.5, 0.5, 0.5, 0.02);
            }
        }
    }
}
