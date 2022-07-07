package pebbleantivpn.data;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pebbleantivpn.pebbleantivpn.PebbleAntiVPNSpigot;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("all")
public class SpigotHandler {

    private FileConfiguration config;
    private FileConfiguration data;
    private FileConfiguration webhook;
    private final PebbleAntiVPNSpigot main;

    private final HashMap<String, Integer> IPConnections = new HashMap<>();

    public SpigotHandler(PebbleAntiVPNSpigot plugin) {
        this.main = plugin;
        updateConfig();
        updateWebhook();
        updateData();
    }

    public void updateConfig() {
        this.main.reloadConfig();
        this.config = this.main.getConfig();
    }

    public void updateData() {
        File file = getDataFile();
        this.data = YamlConfiguration.loadConfiguration(file);
    }

    public void updateWebhook() {
        File file = getWebhookFile();
        this.webhook = YamlConfiguration.loadConfiguration(file);
    }

    public void writeData(String key, Object value) throws IOException {
        this.data.set(key, value);
        this.data.save(getDataFile());
    }

    public Object getConfig(String key, boolean translate) {
        if (this.config.isSet(key)) {
            if (translate)
                return ChatColor.translateAlternateColorCodes('&', (String) Objects.requireNonNull(this.config.get(key))).replace("%nl%", "\n");
            return this.config.get(key);
        }
        return null;
    }

    public File getDataFile() {
        File file = new File(this.main.getDataFolder().getPath(), "data.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public File getWebhookFile() {
        File file = new File(this.main.getDataFolder().getPath(), "webhook.yml");
        if (!file.exists())
            this.main.saveResource("webhook.yml", false);
        return file;
    }

    public Object getWebhook(String key) {
        if (this.webhook.isSet(key)) {
            return this.webhook.get(key);
        }
        return null;
    }

    public Set<String> getConfigSelection(String key) {
        if (this.webhook.isSet(key))
            return this.webhook.getConfigurationSection(key).getKeys(false);
        return null;
    }

    public List<?> getList(String key) {
        if (this.config.isSet(key))
            return this.config.getList(key);
        return null;
    }

    public Object getData(String key) {
        if (this.data.isSet(key))
            return this.data.get(key);
        return null;
    }

    public boolean isSet(String key) {
        return this.data.isSet(key);
    }

    public int getConnections(String IP) {
        if (this.IPConnections.containsKey(IP))
            return this.IPConnections.get(IP);
        return 0;
    }

    public void addConnection(String IP) {
        int i = 0;
        if (this.IPConnections.containsKey(IP))
            i = this.IPConnections.get(IP);
        i++;
        this.IPConnections.remove(IP);
        this.IPConnections.put(IP, i);
    }

    public void removeConnection(String IP) {
        if (!this.IPConnections.containsKey(IP))
            return;
        int i = this.IPConnections.get(IP);
        if (i <= 0)
            return;
        i--;
        this.IPConnections.remove(IP);
        this.IPConnections.put(IP, i);
    }

}
