package pebbleantivpn.pebbleantivpn;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import org.json.JSONObject;
import pebbleantivpn.JSON.BungeeLoader;
import pebbleantivpn.JSON.BungeeSaver;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public final class PebbleAntiVPNBungeeCord extends Plugin implements Listener {

    public static JSONObject IPs = new JSONObject();
    String BlockMessage;
    String BypassPerm;
    String lastBypassPerm;
    String lastBlockMessage;
    static boolean ConsoleFilter;
    boolean lastConsoleFilter;

    @Override
    public void onEnable() {
        getLogger().info(translate("&eLoading &6PebbleAntiVPN"));
        getProxy().getPluginManager().registerListener(this, this);
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Commands(this));
        try {
            if (!getDataFolder().exists())
                getDataFolder().mkdir();
            File file = new File(getDataFolder().getPath(), "config.yml");
            if (!file.exists()) {
                file.createNewFile();
                Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                config.set("block-message", "&6PebbleAntiVPN%nl%&cProxy/VPN Detected.");
                config.set("console-filter", true);
                config.set("bypass-permission", "pav.bypass");
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
            }
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            this.lastBlockMessage = config.getString("block-message");
            this.lastConsoleFilter = config.getBoolean("console-filter");
            this.lastBypassPerm = config.getString("bypass-permission");
            reload();
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);

        } catch (IOException e) {
            getLogger().warning("§cAn error has occurred while reloading the config!\n§bInfo:\n");
            e.printStackTrace();
        }
        new PebbleAntiVPNLoggerBungee().registerFilter();
        try {
            BungeeLoader.loadJSONData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getLogger().info(translate("&aLoaded &6PebbleAntiVPN"));
    }

    @Override
    public void onDisable() {
        getLogger().info(translate("&6PebbleAntiVPN &cHas Been Unloaded"));
        try {
            BungeeSaver.saveJSONData();
        } catch (IOException e) {
            getLogger().warning(translate("&cFailed to save JSON data!"));
        }
    }

    @EventHandler
    public void onConnect(PostLoginEvent e) throws IOException {
        if (e.getPlayer().hasPermission(BypassPerm))
            return;

        String IP = e.getPlayer().getSocketAddress().toString().split(":")[0].replace("/", "");

        JSONObject object = getIPInfo(IP);

        if (object.getBoolean("proxy")) {
            e.getPlayer().disconnect(new TextComponent(translate(BlockMessage)));
        }

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
        if(object.getBoolean("proxy"))
            getLogger().info(translate("&bIP &a" + IP + " &bis a VPN/Proxy from &a" + (object.get("country"))));
        return object;
    }

    public void reload() {
        File file = new File(getDataFolder().getPath(), "config.yml");
        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
            this.BlockMessage = config.getString("block-message");
            this.BypassPerm = config.getString("bypass-permission");
            ConsoleFilter = config.getBoolean("console-filter");

            if(!lastBlockMessage.equals(BlockMessage))
                getLogger().info(translate("\n&eChanged block message from\n" + lastBlockMessage + "\n&eTo\n" + BlockMessage));
            if(!lastBypassPerm.equals(BypassPerm))
                getLogger().info(translate("\n&eChanged bypass permission from\n" + lastBypassPerm + "\n&eTo\n" + BypassPerm));
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
                getLogger().info(translate("&eSwitched Console Filter modes. " + From + " &b-> " + To));
            }

            this.lastBlockMessage = BlockMessage;
            this.lastConsoleFilter = ConsoleFilter;
            this.lastBypassPerm = BypassPerm;
        } catch (IOException e) {
            getLogger().warning("§cAn error has occurred while reloading the config!\n§bInfo:\n");
            e.printStackTrace();
        }
    }

    public String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text).replace("%nl%", "\n");
    }

}
