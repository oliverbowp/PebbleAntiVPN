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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public final class PebbleAntiVPNSpigot extends JavaPlugin implements Listener {

    HashMap<String, JSONObject> IPInfo = new HashMap<>();
    String BlockMessage;
    String lastBlockMessage;
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
        reload();

        new PebbleAntiVPNLoggerSpigot().registerFilter();

        sendConsoleMessage(translate("&aLoaded &6PebbleAntiVPN"));

    }

    @Override
    public void onDisable() {
        sendConsoleMessage(translate("&6PebbleAntiVPN &cHas Been Unloaded"));
    }

    @EventHandler
    public void onConnect(PlayerLoginEvent e) throws IOException {
        String IP = e.getAddress().getHostAddress();

        JSONObject object = getIPInfo(IP);

        if(String.valueOf(object.get("proxy")).equals("true")) e.disallow(PlayerLoginEvent.Result.KICK_BANNED, translate(BlockMessage));
    }

    public void sendConsoleMessage(String message) {
        getServer().getConsoleSender().sendMessage(message);
    }

    public String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text).replace("%nl%", "\n");
    }

    public JSONObject getIPInfo(String IP) throws IOException {
        if (IPInfo.containsKey(IP)) return IPInfo.get(IP);
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
        IPInfo.put(IP, object);
        if(String.valueOf(object.get("proxy")).equals("true"))
            sendConsoleMessage(translate("&bIP &a" + IP + " &bis a VPN/Proxy from &a" + (object.get("country"))));
        return object;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender s, Command cmd, @NotNull String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("aurora")) {
            if (!(s instanceof Player)) {
                if(args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        s.sendMessage("§eReloading...");
                        reload();
                        s.sendMessage("§aReloaded The Config.");
                    } else {
                        s.sendMessage("§cInvalid arguments (Reload)");
                    }
                } else {
                    s.sendMessage("§cInvalid arguments (Reload)");
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
        ConsoleFilter = getConfig().getBoolean("console-filter");

        if(!lastBlockMessage.equals(BlockMessage)) sendConsoleMessage(translate("\n&eChanged block message from\n" + lastBlockMessage + "\n&eTo\n" + BlockMessage));
        if(!this.lastConsoleFilter == ConsoleFilter) {
            String To;
            String From;
            if(ConsoleFilter) {
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
    }

}
