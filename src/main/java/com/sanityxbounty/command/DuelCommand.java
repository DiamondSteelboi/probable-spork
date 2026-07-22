package com.sanityxbounty.command;

import com.sanityxbounty.SanityXBounty;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DuelCommand implements CommandExecutor, TabCompleter {

    private final SanityXBounty plugin;

    public DuelCommand(SanityXBounty plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (!player.hasPermission("sanityxbounty.duel")) {
            player.sendMessage("&cYou do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("&eUsage: /duel <player> | /duel accept | /duel deny");
            return true;
        }

        String sub = args[0].toLowerCase();
        if (sub.equals("accept")) {
            if (plugin.getDuelManager().accept(player.getUniqueId())) {
                player.sendMessage("&aDuel accepted.");
            } else {
                player.sendMessage("&cNo duel request to accept.");
            }
            return true;
        }

        if (sub.equals("deny")) {
            if (plugin.getDuelManager().deny(player.getUniqueId())) {
                player.sendMessage("&aDuel denied.");
            } else {
                player.sendMessage("&cNo duel request to deny.");
            }
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage("&cPlayer not found.");
            return true;
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage("&cYou cannot duel yourself.");
            return true;
        }

        if (plugin.getDuelManager().challenge(player.getUniqueId(), target.getUniqueId())) {
            player.sendMessage("&aDuel request sent to " + target.getName() + ".");
            target.sendMessage("&e" + player.getName() + " has challenged you to a duel. Use /duel accept or /duel deny.");
        } else {
            player.sendMessage("&cCould not start a duel. Check if you or the target are already in a duel or in combat.");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            String prefix = args[0].toLowerCase();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(prefix)) {
                    suggestions.add(player.getName());
                }
            }
            suggestions.add("accept");
            suggestions.add("deny");
            return suggestions;
        }
        return List.of();
    }
}
