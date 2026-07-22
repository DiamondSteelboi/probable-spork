package com.sanityxbounty.command;

import com.sanityxbounty.SanityXBounty;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SanityXBountyCommand implements CommandExecutor {

    private final SanityXBounty plugin;

    public SanityXBountyCommand(SanityXBounty plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("sanityxbounty.reload")) {
            sender.sendMessage("&cYou do not have permission to use this command.");
            return true;
        }

        plugin.reloadConfig();
        sender.sendMessage("&aSanityXBounty configuration reloaded.");
        return true;
    }
}
