package pebbleantivpn.pebbleantivpn;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import pebbleantivpn.data.BungeeHandler;

public class BungeeCommands extends Command {

    private final PebbleAntiVPNBungeeCord main;
    private final BungeeHandler handler;

    public BungeeCommands(PebbleAntiVPNBungeeCord plugin) {
        super("Pav");
        this.main = plugin;
        this.handler = plugin.getHandler();
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {
                    this.handler.update();
                    sender.sendMessage(new TextComponent("§aReloaded all configs."));
                } else if (args[0].equalsIgnoreCase("toggle")) {
                    this.main.togglePlugin();
                    if (this.main.isPluginEnabled())
                        sender.sendMessage(new TextComponent("§aAll PebbleAntiVPN checks and events have been enabled."));
                    else
                        sender.sendMessage(new TextComponent("§cAll PebbleAntiVPN checks and events have been disabled."));
                } else {
                    sender.sendMessage(new TextComponent("§cInvalid arguments (reload/toggle)"));
                }
            } else {
                sender.sendMessage(new TextComponent("§cInvalid arguments (reload/toggle)"));
            }
        } else {
            sender.sendMessage(new TextComponent("§cThis command may only be executed by console."));
        }

    }
}