package pebbleantivpn.Loggers;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.log.ConciseFormatter;
import pebbleantivpn.data.BungeeHandler;
import pebbleantivpn.pebbleantivpn.PebbleAntiVPNBungeeCord;

import java.util.logging.Logger;


public class PebbleAntiVPNLoggerBungee {

    private final BungeeHandler handler;

    public PebbleAntiVPNLoggerBungee(PebbleAntiVPNBungeeCord plugin) {
        this.handler = plugin.getHandler();
        if (!ProxyServer.getInstance().getVersion().contains("BungeeCord")) {
            new PebbleAntiVPNLoggerBungeeLog4J(this).registerFilter();
        } else {
            Logger logger = ProxyServer.getInstance().getLogger();
            logger.setFilter(record -> {
                String msg = (new ConciseFormatter(false)).formatMessage(record).trim();
                return logMessage(msg);
            });
        }
    }

    public boolean logMessage(String message) {
        if(!(boolean) this.handler.getConfig("console-filter", false)) return true;
        if(message.contains("Event ConnectionInitEvent(remoteAddress=")) return false;
        if(message.contains("disconnected with: ")) return false;
        if(message.contains("No client connected for pending server!")) return false;
        if(message.contains(" <-> ServerConnector")) return false;
        if(message.contains("PostLoginEvent") && message.contains("pebbleantivpn")) return false;
        if(message.contains("-> UpstreamBridge has disconnected")) return false;
        if (message.contains("Event PostLoginEvent(player=")) return false;
        return !message.contains(" <-> InitialHandler");
    }

}
