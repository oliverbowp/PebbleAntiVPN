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
        return !message.contains(" <-> InitialHandler");
    }

}
