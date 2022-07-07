package pebbleantivpn.evnets;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pebbleantivpn.data.SpigotHandler;
import pebbleantivpn.pebbleantivpn.PebbleAntiVPNSpigot;

public class PlayerQuit implements Listener {

    private final SpigotHandler handler;

    public PlayerQuit(PebbleAntiVPNSpigot plugin) {
        this.handler = plugin.getHandler();
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        if (e.getPlayer().getAddress() != null) {
            String IP = e.getPlayer().getAddress().getHostName();
            this.handler.removeConnection(IP);
        }
    }

}
