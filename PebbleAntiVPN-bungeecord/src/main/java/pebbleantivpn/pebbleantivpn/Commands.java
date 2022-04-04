package pebbleantivpn.pebbleantivpn;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class Commands extends Command {
    private final PebbleAntiVPNBungeeCord mainClass;

    public Commands(PebbleAntiVPNBungeeCord main) {
        super("Pav");
        this.mainClass = main;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof net.md_5.bungee.api.connection.ProxiedPlayer)) {
            if(args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(new TextComponent("§eReloading..."));
                    this.mainClass.reload();
                    sender.sendMessage(new TextComponent("§aReloaded The Config."));
                } else {
                    sender.sendMessage(new TextComponent("§cInvalid arguments (Reload)"));
                }
            } else {
                sender.sendMessage(new TextComponent("§cInvalid arguments (Reload)"));
            }
        } else {
            sender.sendMessage(new TextComponent("§cThis command can only be executed in console."));
        }
    }
}