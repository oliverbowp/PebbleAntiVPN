package pebbleantivpn.pebbleantivpn;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

@Plugin(
        id = "pav",
        name = "PebbleAntiVPN",
        version = "1.5",
        description = "Block any type of <Tor/Proxy/VPNs>",
        authors = {"Binkie"}
)
public class PebbleAntiVPNVelocity{

    private final ProxyServer server;

    @Inject
    public PebbleAntiVPNVelocity(ProxyServer server, Logger logger) {
        this.server = server;

        logger.info("hi, this is a test plugin of PAV on velocity.");


    }

    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new Listener());
    }

}
