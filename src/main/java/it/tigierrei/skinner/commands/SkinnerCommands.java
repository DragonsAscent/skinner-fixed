package it.tigierrei.skinner.commands;

import it.tigierrei.skinner.Skinner;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SkinnerCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command label, String cmd, String[] args) {
        if (!(args.length < 1)) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("Skinner.admin") || sender.isOp()) {
                    Skinner.pl.setupConfig();
                    sender.sendMessage(ChatColor.GREEN + "Skinner config updated!");
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Use /sk reload or /skinner reload to update skinner config");
                return true;

            }
        } else {
            sender.sendMessage(ChatColor.RED + "Use /sk reload or /skinner reload to update skinner config");
        }
        return true;
    }
}
