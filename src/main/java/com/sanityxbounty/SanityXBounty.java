package com.sanityxbounty;

import com.sanityxbounty.command.BountyCommand;
import com.sanityxbounty.command.DuelCommand;
import com.sanityxbounty.command.SanityCommand;
import com.sanityxbounty.command.SanityXBountyCommand;
import com.sanityxbounty.listener.CombatListener;
import com.sanityxbounty.listener.DuelListener;
import com.sanityxbounty.listener.PlayerJoinQuitListener;
import com.sanityxbounty.listener.PlayerSanityListener;
import com.sanityxbounty.manager.BountyManager;
import com.sanityxbounty.manager.CombatManager;
import com.sanityxbounty.manager.DuelManager;
import com.sanityxbounty.manager.EffectManager;
import com.sanityxbounty.manager.RewardManager;
import com.sanityxbounty.manager.SanityManager;
import com.sanityxbounty.manager.SelfDefenseManager;
import com.sanityxbounty.managers.PlayerDataManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SanityXBounty extends JavaPlugin {

    private PlayerDataManager playerDataManager;
    private DuelManager duelManager;
    private CombatManager combatManager;
    private SelfDefenseManager selfDefenseManager;
    private SanityManager sanityManager;
    private BountyManager bountyManager;
    private EffectManager effectManager;
    private RewardManager rewardManager;

    @Override
    public void onEnable() {
        this.playerDataManager = new PlayerDataManager(this);
        this.duelManager = new DuelManager(this, playerDataManager);
        this.combatManager = new CombatManager(this, playerDataManager);
        this.selfDefenseManager = new SelfDefenseManager(this, playerDataManager);
        this.sanityManager = new SanityManager(this);
        this.bountyManager = new BountyManager(this);
        this.effectManager = new EffectManager(this, playerDataManager);
        this.rewardManager = new RewardManager(this);

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic can be added here later.
    }

    private void registerCommands() {
        PluginCommand duel = getCommand("duel");
        if (duel != null) {
            DuelCommand command = new DuelCommand(this);
            duel.setExecutor(command);
            duel.setTabCompleter(command);
        }

        PluginCommand sanity = getCommand("sanity");
        if (sanity != null) {
            SanityCommand command = new SanityCommand(this);
            sanity.setExecutor(command);
            sanity.setTabCompleter(command);
        }

        PluginCommand bounty = getCommand("bounty");
        if (bounty != null) {
            BountyCommand command = new BountyCommand(this);
            bounty.setExecutor(command);
            bounty.setTabCompleter(command);
        }

        PluginCommand sanityxbounty = getCommand("sanityxbounty");
        if (sanityxbounty != null) {
            sanityxbounty.setExecutor(new SanityXBountyCommand(this));
        }
    }

    private void registerListeners() {
        var manager = getServer().getPluginManager();
        manager.registerEvents(new DuelListener(this), this);
        manager.registerEvents(new CombatListener(this), this);
        manager.registerEvents(new PlayerSanityListener(this), this);
        manager.registerEvents(new PlayerJoinQuitListener(this), this);
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public DuelManager getDuelManager() {
        return duelManager;
    }

    public CombatManager getCombatManager() {
        return combatManager;
    }

    public SelfDefenseManager getSelfDefenseManager() {
        return selfDefenseManager;
    }

    public SanityManager getSanityManager() {
        return sanityManager;
    }

    public BountyManager getBountyManager() {
        return bountyManager;
    }

    public EffectManager getEffectManager() {
        return effectManager;
    }

    public RewardManager getRewardManager() {
        return rewardManager;
    }
}
