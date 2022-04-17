package pebbleantivpn.pebbleantivpn;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import org.json.JSONObject;

public class Commands extends Command {
    private final PebbleAntiVPNBungeeCord mainClass;

    public Commands(PebbleAntiVPNBungeeCord main) {
        super("Pav");
        this.mainClass = main;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof net.md_5.bungee.api.connection.ProxiedPlayer)) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(new TextComponent("§eReloading..."));
                    this.mainClass.reload();
                    sender.sendMessage(new TextComponent("§aReloaded The Config."));
                } else if (args[0].equalsIgnoreCase("whitelist")) {
                    if (args.length != 3) {
                        sender.sendMessage(new TextComponent("§cInvalid arguments (Whitelist <add/remove>)"));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("add")) {
                        if (!PebbleAntiVPNBungeeCord.IPs.isNull(args[2]))
                            PebbleAntiVPNBungeeCord.IPs.getJSONObject(args[2]).remove("proxy");

                        JSONObject to = new JSONObject();
                        to.put("proxy", false);
                        PebbleAntiVPNBungeeCord.IPs.put(args[2], to);
                        sender.sendMessage(new TextComponent("§aWhitelisted §e" + args[2] + " §7This IP will not be checked while connecting."));
                    } else if (args[1].equalsIgnoreCase("remove")) {
                        if (!PebbleAntiVPNBungeeCord.IPs.isNull(args[2]))
                           PebbleAntiVPNBungeeCord.IPs.remove(args[2]);

                        sender.sendMessage(new TextComponent("§aUnWhitelisted §e" + args[2] + " §7This IP will be checked while connecting."));
                    } else {
                        sender.sendMessage(new TextComponent("§cInvalid arguments (Whitelist <add/remove>)"));
                    }
                } else {
                    sender.sendMessage(new TextComponent("§cInvalid arguments (Reload/Whitelist)"));
                }
            } else {
                sender.sendMessage(new TextComponent("§cInvalid arguments (Reload/Whitelist)"));
            }
        } else {
            sender.sendMessage(new TextComponent("§cThis command can only be executed in console."));
        }
    }
}