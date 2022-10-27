package pebbleantivpn.pebbleantivpn;
import net.md_5.bungee.api.plugin.Plugin;
import pebbleantivpn.BungeeAlerts.MainAlert;
import pebbleantivpn.BungeeAlerts.WebhookAlert;
import pebbleantivpn.Loggers.PebbleAntiVPNLoggerBungee;
import pebbleantivpn.bedrock.BungeeFloodgateProvider;
import pebbleantivpn.data.BungeeHandler;
import pebbleantivpn.events.Disconnect;
import pebbleantivpn.events.PostLogin;


public final class PebbleAntiVPNBungeeCord extends Plugin {

    private BungeeHandler handler;
    private WebhookAlert webhook;
    private MainAlert bungeeAlert;
    private BungeeProxyChecker proxyChecker;
    private BungeeFloodgateProvider floodgateProvider;
    private boolean isEnabled = true;

    @Override
    public void onEnable() {
        getLogger().info("§eLoading §6PebbleAntiVPN§e...");
        this.handler = new BungeeHandler(this);
        this.bungeeAlert = new MainAlert(this);
        this.webhook = new WebhookAlert(this);
        this.proxyChecker = new BungeeProxyChecker(this);
        if (this.getProxy().getPluginManager().getPlugin("Floodgate") != null) {
            this.floodgateProvider = new BungeeFloodgateProvider(this);
        }
        getProxy().getPluginManager().registerListener(this, new PostLogin(this));
        getProxy().getPluginManager().registerListener(this, new Disconnect(this));
        getProxy().getPluginManager().registerCommand(this, new BungeeCommands(this));
        new PebbleAntiVPNLoggerBungee(this);
        getLogger().info("§6PebbleAntiVPN §bHas Been Loaded.");
    }

    @Override
    public void onDisable() {
    }

    public BungeeHandler getHandler() {
        return this.handler;
    }

    public WebhookAlert getWebhook() {
        return this.webhook;
    }

    public MainAlert getBungeeAlert() {
        return this.bungeeAlert;
    }

    public BungeeProxyChecker getProxyChecker() {
        return this.proxyChecker;
    }

    public BungeeFloodgateProvider getFloodgateProvider() { return this.floodgateProvider;}

    public void togglePlugin() {
        this.isEnabled = !this.isEnabled;
    }

    public boolean isPluginEnabled() {
        return this.isEnabled;
    }

}
