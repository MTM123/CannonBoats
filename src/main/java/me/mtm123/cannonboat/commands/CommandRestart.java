package me.mtm123.cannonboat.commands;

import me.mtm123.cannonboat.CannonBoats;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class CommandRestart implements CommandExecutor {

    private final CannonBoats plugin;

    private final String reloadMessage;

    public CommandRestart(CannonBoats plugin, FileConfiguration cfg) {
        this.plugin = plugin;
        this.reloadMessage = ChatColor.translateAlternateColorCodes('&', cfg.getString("reload-message"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender.hasPermission("cannonboat.reload")){
            plugin.loadPlugin(true);
            sender.sendMessage(reloadMessage);

            return true;
        }

        return false;
    }

}
