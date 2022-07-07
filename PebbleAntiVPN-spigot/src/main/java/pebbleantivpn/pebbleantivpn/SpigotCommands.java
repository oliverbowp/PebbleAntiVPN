package pebbleantivpn.pebbleantivpn;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pebbleantivpn.data.SpigotHandler;

public class SpigotCommands implements CommandExecutor {

    private final PebbleAntiVPNSpigot main;
    private final SpigotHandler handler;

    public SpigotCommands(PebbleAntiVPNSpigot plugin) {
        this.main = plugin;
        this.handler = plugin.getHandler();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("pav")) {
            if (!(sender instanceof Player)) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        this.handler.updateConfig();
                        this.handler.updateData();
                        this.handler.updateWebhook();
                        sender.sendMessage("§aReloaded all configs.");
                    } else if (args[0].equalsIgnoreCase("toggle")) {
                        this.main.togglePlugin();
                        if (this.main.isPluginEnabled())
                            sender.sendMessage("§aAll PebbleAntiVPN checks and events have been enabled.");
                        else
                            sender.sendMessage("§cAll PebbleAntiVPN checks and events have been disabled.");
                    } else {
                        sender.sendMessage("§cInvalid arguments (reload/toggle)");
                    }
                } else {
                    sender.sendMessage("§cInvalid arguments (reload/toggle)");
                }
            } else {
                sender.sendMessage("§cThis command may only be executed by console.");
            }
        }
        return false;
    }

}
