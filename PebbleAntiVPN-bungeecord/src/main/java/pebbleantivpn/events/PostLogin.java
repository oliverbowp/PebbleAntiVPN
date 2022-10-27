package pebbleantivpn.events;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pebbleantivpn.data.BungeeHandler;
import pebbleantivpn.pebbleantivpn.BungeeProxyChecker;
import pebbleantivpn.pebbleantivpn.PebbleAntiVPNBungeeCord;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PostLogin implements Listener {

    private final BungeeProxyChecker proxyChecker;
    private final PebbleAntiVPNBungeeCord main;
    private final BungeeHandler handler;

    public PostLogin(PebbleAntiVPNBungeeCord plugin) {
        this.main = plugin;
        this.proxyChecker = plugin.getProxyChecker();
        this.handler = plugin.getHandler();
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent e) throws IOException {
        ProxiedPlayer player = e.getPlayer();

        if (!this.main.isPluginEnabled() || player.hasPermission(this.handler.getConfig("bypass-permission", false).toString()))
            return;

        if (this.handler.getConfig().getStringList("bypass-list-lower-case").contains(player.getName().toLowerCase()))
            return;

        if (this.handler.getConfig().getBoolean("floodgate.bypass-bedrock-players", false) && this.main.getFloodgateProvider().isBedrockPlayer(player))
            return;

        String IP = player.getSocketAddress().toString().split(":")[0].replace("/", "");
        String dataIP = IP.replace(".", "_");
        String name = player.getName();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String country;
        String countryCode;

        if (this.proxyChecker.isProxy(IP, name)) {
            country = this.handler.getData("details." + dataIP + ".country.name").toString();
            countryCode = this.handler.getData("details." + dataIP + ".country.name").toString();

            player.disconnect(new TextComponent(this.handler.getConfig("block-message", true).toString().replace("%ip%", IP).replace("%player%", name).replace("%time%", dtf.format(now)).replace("%country%", country).replace("%countryCode%", countryCode)));
        } else if ((boolean) this.handler.getConfig("blocked-countries.enabled", false)) {
            country = this.handler.getData("details." + dataIP + ".country.name").toString();
            countryCode = this.handler.getData("details." + dataIP + ".country.name").toString();
            String kickMessage = this.handler.getConfig("blocked-countries.kick-message", true).toString().replace("%ip%", IP).replace("%player%", name).replace("%time%", dtf.format(now)).replace("%country%", country).replace("%countryCode%", countryCode);

            List<?> BlockedCountries = this.handler.getList("blocked-countries.countries");
            if (BlockedCountries.contains(this.handler.getData("details." + dataIP + ".country.name")))
                player.disconnect(new TextComponent(kickMessage));
            else if (BlockedCountries.contains(this.handler.getData("details." + dataIP + ".country.code"))) {
                player.disconnect(new TextComponent(kickMessage));
            }
        } else if ((boolean) this.handler.getConfig("users-per-ip.enabled", false)) {
            int max = (int) this.handler.getConfig("users-per-ip.limit", false);
            int connection = this.handler.getConnections(IP);

            if (max <= 0) {
                this.main.getLogger().warning("§cThe minimum amount of connections per IP must be over 0.");
            } else if (connection >= max) {
                player.disconnect(new TextComponent(this.handler.getConfig("users-per-ip.kick-message", true).toString()));
            } else {
                this.handler.addConnection(IP);
            }
        }
    }

}
