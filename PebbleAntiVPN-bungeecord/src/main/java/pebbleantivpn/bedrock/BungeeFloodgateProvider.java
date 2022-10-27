package pebbleantivpn.bedrock;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.geysermc.floodgate.api.FloodgateApi;
import pebbleantivpn.pebbleantivpn.PebbleAntiVPNBungeeCord;

public class BungeeFloodgateProvider {

    private final PebbleAntiVPNBungeeCord plugin;
    private FloodgateApi floodgate = null;

    public BungeeFloodgateProvider(PebbleAntiVPNBungeeCord plugin) {
        this.plugin = plugin;
        this.reload();
    }

    private void reload() {
        this.floodgate = FloodgateApi.getInstance();
    }

    public boolean isBedrockPlayer(ProxiedPlayer player) {
        if (this.isFloodgate()) {
            return floodgate.isFloodgateId(player.getUniqueId());
        }
        return false;
    }

    private boolean isFloodgate() {
        return floodgate != null;
    }
}
