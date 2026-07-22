package com.sanityxbounty.command;

import com.sanityxbounty.SanityXBounty;
import com.sanityxbounty.models.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SanityCommand implements CommandExecutor, TabCompleter {

    private final SanityXBounty plugin;

    public SanityCommand(SanityXBounty plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        PlayerData targetData;
        if (args.length >= 1 && player.hasPermission("sanityxbounty.admin")) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                player.sendMessage("&cPlayer not found.");
                return true;
            }
            targetData = plugin.getPlayerDataManager().getPlayerData(target.getUniqueId());
        } else {
            targetData = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        }

        player.sendMessage("&aSanity: &f" + targetData.getSanity());
        player.sendMessage("&aBounty Rank: &f" + targetData.getBountyRank());
        player.sendMessage("&aUnlawful Kills: &f" + targetData.getUnlawfulKills());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    suggestions.add(player.getName());
                }
            }
            return suggestions;
        }
        return List.of();
    }
}
