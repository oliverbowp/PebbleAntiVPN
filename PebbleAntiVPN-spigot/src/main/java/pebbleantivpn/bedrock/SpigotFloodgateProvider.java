package pebbleantivpn.bedrock;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import pebbleantivpn.pebbleantivpn.PebbleAntiVPNSpigot;

public class SpigotFloodgateProvider {

    private PebbleAntiVPNSpigot plugin;
    private FloodgateApi floodgate = null;

    public SpigotFloodgateProvider(PebbleAntiVPNSpigot plugin) {
        this.plugin = plugin;
        this.reload();
    }

    private void reload() {
        if (Bukkit.getPluginManager().isPluginEnabled("Floodgate")) {
            floodgate = FloodgateApi.getInstance();
        }
    }

    public boolean isBedrockPlayer(Player player) {
        if (this.isFloodgate()) {
            return floodgate.isFloodgateId(player.getUniqueId());
        }
        return false;
    }

    private boolean isFloodgate() {
        return floodgate != null;
    }
}
