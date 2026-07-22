package com.sanityxbounty.command;

import com.sanityxbounty.SanityXBounty;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class AcceptDuelCommand implements CommandExecutor {

    private final SanityXBounty plugin;

    public AcceptDuelCommand(SanityXBounty plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // Duel acceptance handling will be implemented later.
        return true;
    }
}
