package net.actest.commands;

import net.actest.ACTestAuthorizer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    private final ACTestAuthorizer plugin;

    public TestCommand(ACTestAuthorizer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("actest.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /actest <enable|disable|whitelist|unwhitelist|status>");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "enable" -> {
                plugin.setTestingEnabled(true);
                sender.sendMessage(ChatColor.GREEN + "AC tests enabled.");
            }
            case "disable" -> {
                plugin.setTestingEnabled(false);
                sender.sendMessage(ChatColor.RED + "AC tests disabled.");
            }
            case "whitelist" -> {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /actest whitelist <player>");
                    return true;
                }
                Player target = plugin.getServer().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                plugin.addToWhitelist(target.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "Whitelisted " + target.getName());
            }
            case "unwhitelist" -> {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /actest unwhitelist <player>");
                    return true;
                }
                Player target = plugin.getServer().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                plugin.removeFromWhitelist(target.getUniqueId());
                sender.sendMessage(ChatColor.YELLOW + "Removed " + target.getName());
            }
            case "status" -> {
                sender.sendMessage(ChatColor.GOLD + "AC Test Status:");
                sender.sendMessage(ChatColor.YELLOW + "Testing: " + (plugin.isTestingEnabled() ? "ENABLED" : "DISABLED"));
                sender.sendMessage(ChatColor.YELLOW + "Auth Key: " + plugin.getAuthKey());
                sender.sendMessage(ChatColor.YELLOW + "Whitelisted: " + plugin.getWhitelistedPlayers().size());
            }
            default -> sender.sendMessage(ChatColor.RED + "Unknown subcommand.");
        }
        return true;
    }
}
