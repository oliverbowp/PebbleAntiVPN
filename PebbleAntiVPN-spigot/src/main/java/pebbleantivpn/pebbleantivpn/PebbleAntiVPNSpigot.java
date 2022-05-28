package pebbleantivpn.pebbleantivpn;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import pebbleantivpn.pebbleantivpn.JSON.SpigotLoader;
import pebbleantivpn.pebbleantivpn.JSON.SpigotSaver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class PebbleAntiVPNSpigot extends JavaPlugin implements Listener {

    public static JSONObject IPs = new JSONObject();
    String BlockMessage;
    String BypassPerm;
    String lastBlockMessage;
    String lastBypassPerm;
    static boolean ConsoleFilter;
    boolean lastConsoleFilter;

    @Override
    public void onEnable() {
        sendConsoleMessage(translate("&eLoading &6PebbleAntiVPN..."));

        getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        this.lastBlockMessage = getConfig().getString("block-message");
        this.lastConsoleFilter = getConfig().getBoolean("console-filter");
        this.lastBypassPerm = getConfig().getString("bypass-permission");
        reload();

        new PebbleAntiVPNLoggerSpigot().registerFilter();
        try {
            SpigotLoader.loadJSONData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendConsoleMessage(translate("&aLoaded &6PebbleAntiVPN"));

    }

    @Override
    public void onDisable() {
        sendConsoleMessage(translate("&6PebbleAntiVPN &cHas Been Unloaded"));
        try {
            SpigotSaver.saveJSONData();
        } catch (IOException e) {
            getLogger().warning(translate("&cFailed to save JSON data!"));
        }
    }

    @EventHandler
    public void onConnect(PlayerLoginEvent e) throws IOException {

        if (e.getPlayer().hasPermission(BypassPerm))
            return;

        String IP = e.getAddress().getHostAddress();

        JSONObject object = getIPInfo(IP);

        if (object.getBoolean("proxy")) e.disallow(PlayerLoginEvent.Result.KICK_BANNED, translate(BlockMessage));
    }

    public void sendConsoleMessage(String message) {
        getServer().getConsoleSender().sendMessage(message);
    }

    public String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text).replace("%nl%", "\n");
    }

    public JSONObject getIPInfo(String IP) throws IOException {
        if (!IPs.isNull(IP)) return IPs.getJSONObject(IP);
        InputStream inputStream;
        URL url = new URL("http://ip-api.com/json/" + IP + "?fields=country,proxy");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestProperty("Accept", "application/json");
        int responseCode = http.getResponseCode();
        if (200 <= responseCode && responseCode <= 299) {
            inputStream = http.getInputStream();
        } else {
            inputStream = http.getErrorStream();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String currentLine;
        while ((currentLine = in.readLine()) != null)
            response.append(currentLine);
        in.close();
        JSONObject object = new JSONObject(response.toString());
        IPs.put(IP, object);
        if (object.getBoolean("proxy"))
            sendConsoleMessage(translate("&bIP &a" + IP + " &bis a VPN/Proxy from &a" + (object.get("country"))));
        return object;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender s, Command cmd, @NotNull String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("pav")) {
            if (!(s instanceof Player)) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        s.sendMessage("§eReloading...");
                        reload();
                        s.sendMessage("§aReloaded The Config.");
                    } else if (args[0].equalsIgnoreCase("whitelist")) {
                        if (args.length != 3) {
                            s.sendMessage("§cInvalid arguments (Whitelist <add/remove>)");
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("add")) {
                            if (!IPs.isNull(args[2]))
                                IPs.getJSONObject(args[2]).remove("proxy");

                            JSONObject to = new JSONObject();
                            to.put("proxy", false);
                            IPs.put(args[2], to);
                            s.sendMessage("§aWhitelisted §e" + args[2] + " §7This IP will not be checked while connecting.");
                        } else if (args[1].equalsIgnoreCase("remove")) {
                            if (!IPs.isNull(args[2]))
                                IPs.remove(args[2]);

                            s.sendMessage("§aUnWhitelisted §e" + args[2] + " §7This IP will be checked while connecting.");
                        } else {
                            s.sendMessage("§cInvalid arguments (Whitelist <add/remove>)");
                        }
                    } else {
                        s.sendMessage("§cInvalid arguments (Reload/Whitelist)");
                    }
                } else {
                    s.sendMessage("§cInvalid arguments (Reload/Whitelist)");
                }
            } else {
                s.sendMessage("§cThis command can only be executed in console.");
            }
        }
        return true;
    }

    public void reload() {
        reloadConfig();
        this.BlockMessage = getConfig().getString("block-message");
        this.BypassPerm = getConfig().getString("bypass-permission");
        ConsoleFilter = getConfig().getBoolean("console-filter");

        if (!lastBlockMessage.equals(BlockMessage))
            sendConsoleMessage(translate("\n&eChanged block message from\n" + lastBlockMessage + "\n&eTo\n" + BlockMessage));
        if (!lastBypassPerm.equals(BypassPerm))
            sendConsoleMessage(translate("\n&eChanged bypass permission from\n" + lastBypassPerm + "\n&eTo\n" + BypassPerm));
        if (!this.lastConsoleFilter == ConsoleFilter) {
            String To;
            String From;
            if (ConsoleFilter) {
                To = "&aTrue";
                From = "&cFalse";
            } else {
                To = "&cFalse";
                From = "&aTrue";
            }
            sendConsoleMessage(translate("&eSwitched Console Filter modes. " + From + " &b-> " + To));
        }

        this.lastBlockMessage = BlockMessage;
        this.lastConsoleFilter = ConsoleFilter;
        this.lastBypassPerm = BypassPerm;
    }
}
