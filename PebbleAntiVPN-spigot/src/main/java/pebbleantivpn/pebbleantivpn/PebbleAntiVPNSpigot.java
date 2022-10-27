package pebbleantivpn.pebbleantivpn;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pebbleantivpn.SpigotAlerts.MainAlert;
import pebbleantivpn.SpigotAlerts.WebhookAlert;
import pebbleantivpn.bedrock.SpigotFloodgateProvider;
import pebbleantivpn.data.SpigotHandler;
import pebbleantivpn.events.PlayerLogin;
import pebbleantivpn.events.PlayerQuit;

import java.util.Objects;

public final class PebbleAntiVPNSpigot extends JavaPlugin {

    private SpigotHandler handler;
    private SpigotProxyChecker proxyChecker;
    private WebhookAlert webhook;
    private MainAlert spigotAlert;
    private SpigotFloodgateProvider floodgateProvider;
    private boolean isEnabled = true;

    // Thanks to Justinnn#0001 for assisting me with the new features.

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        getServer().getConsoleSender().sendMessage("§eLoading §6PebbleAntiVPN§e...");
        this.handler = new SpigotHandler(this);
        this.spigotAlert = new MainAlert(this);
        this.webhook = new WebhookAlert(this);
        this.proxyChecker = new SpigotProxyChecker(this);
        if (Bukkit.getPluginManager().getPlugin("Floodgate") != null) {
            this.floodgateProvider = new SpigotFloodgateProvider(this);
        }
        new PebbleAntiVPNLoggerSpigot(this);
        getServer().getPluginManager().registerEvents(new PlayerLogin(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
        Objects.requireNonNull(getCommand("pav")).setExecutor(new SpigotCommands(this));
        getServer().getConsoleSender().sendMessage("§6PebbleAntiVPN §bHas Been Loaded.");
    }

    @Override
    public void onDisable() {

    }

    public SpigotHandler getHandler() {
        return this.handler;
    }

    public SpigotProxyChecker getProxyChecker() {
        return this.proxyChecker;
    }

    public WebhookAlert getWebhook() {
        return this.webhook;
    }

    public MainAlert getSpigotAlert() {
        return this.spigotAlert;
    }

    public SpigotFloodgateProvider getFloodgateProvider() {return this.floodgateProvider;}

    public void togglePlugin() {
        this.isEnabled = !this.isEnabled;
    }

    public boolean isPluginEnabled() {
        return this.isEnabled;
    }
}
