package pebbleantivpn.SpigotAlerts;

import pebbleantivpn.data.SpigotHandler;
import pebbleantivpn.pebbleantivpn.PebbleAntiVPNSpigot;

public class MainAlert {

    private final PebbleAntiVPNSpigot main;
    private final SpigotHandler handler;

    public MainAlert(PebbleAntiVPNSpigot plugin) {
        this.main = plugin;
        this.handler = plugin.getHandler();
    }

    public void execute(String IP, String name, String country, String countryCode, String time) {
        String message = this.handler.getConfig("alerts.console.message", true).toString().replace("%ip%", IP).replace("%player%", name).replace("%time%", time).replace("%country%", country).replace("%countryCode%", countryCode);
        if ((boolean) this.handler.getConfig("alerts.console.enabled", false))
            this.main.getServer().getConsoleSender().sendMessage(message);
        if ((boolean) this.handler.getConfig("alerts.players.enabled", false)) {
            this.main.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(player.hasPermission(this.handler.getConfig("alerts.players.permission", false).toString()) ? message : ""));
        }
    }
}
