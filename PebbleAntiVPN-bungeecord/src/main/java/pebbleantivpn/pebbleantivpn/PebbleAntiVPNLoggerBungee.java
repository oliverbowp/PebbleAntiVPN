package pebbleantivpn.pebbleantivpn;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.log.ConciseFormatter;

import java.util.logging.Logger;

import static pebbleantivpn.pebbleantivpn.PebbleAntiVPNBungeeCord.ConsoleFilter;

public class PebbleAntiVPNLoggerBungee {

    public void registerFilter() {
        if (!ProxyServer.getInstance().getVersion().contains("BungeeCord")) {
            new PebbleAntiVPNLoggerBungeeLog4J().registerFilter();
        } else {
            Logger logger = ProxyServer.getInstance().getLogger();
            logger.setFilter(record -> {
                String msg = (new ConciseFormatter(false)).formatMessage(record).trim();
                return logMessage(msg);
            });
        }
    }

    public static boolean logMessage(String message) {
        if(!ConsoleFilter) return true;
        if(message.contains("Event ConnectionInitEvent(remoteAddress=")) return false;
        if(message.contains("disconnected with: ")) return false;
        if(message.contains("No client connected for pending server!")) return false;
        if(message.contains(" <-> ServerConnector")) return false;
        if(message.contains("-> UpstreamBridge has disconnected")) return false;
        return !message.contains(" <-> InitialHandler");
    }

}
