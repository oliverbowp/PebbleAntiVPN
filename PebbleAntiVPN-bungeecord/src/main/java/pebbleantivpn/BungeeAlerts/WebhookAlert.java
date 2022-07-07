package pebbleantivpn.BungeeAlerts;

import discord.DiscordWebhooks;
import net.md_5.bungee.config.Configuration;
import pebbleantivpn.data.BungeeHandler;
import pebbleantivpn.pebbleantivpn.PebbleAntiVPNBungeeCord;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class WebhookAlert {

    private final BungeeHandler handler;
    private final PebbleAntiVPNBungeeCord main;

    public WebhookAlert(PebbleAntiVPNBungeeCord plugin) {
        this.main = plugin;
        this.handler = plugin.getHandler();
    }

    public void discordAlert(String IP, String name, String country, String countryCode, String time) throws IOException {
        if (isEnabled()) {
            DiscordWebhooks webhook = new DiscordWebhooks(getWebhook());
            webhook.addEmbed(getEmbed(IP, name, country, countryCode, time));
            webhook.execute();
        }
    }

    public boolean isEnabled() {
        return (boolean) this.handler.getWebhook("discord-webhook.enabled");
    }

    private Color generateRandomColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return new Color(r, g, b);
    }

    private String getWebhook() {
        return (String) this.handler.getWebhook("discord-webhook.webhook-link");
    }

    private DiscordWebhooks.EmbedObject getEmbed(String IP, String player, String country, String countryCode, String time) {
        DiscordWebhooks.EmbedObject embed = new DiscordWebhooks.EmbedObject();
        if ((boolean) this.handler.getWebhook("discord-webhook.embed.title.enabled"))
            embed.setTitle(this.handler.getWebhook("discord-webhook.embed.title.text").toString().replace("%IP%", IP).replace("%player%", player).replace("%time%", time).replace("%country%", country).replace("%countryCode%", countryCode));
        if ((boolean) this.handler.getWebhook("discord-webhook.embed.description.enabled"))
            embed.setDescription(this.handler.getWebhook("discord-webhook.embed.description.text").toString().replace("%IP%", IP).replace("%player%", player).replace("%time%", time).replace("%country%", country).replace("%countryCode%", countryCode));

        if ((boolean) this.handler.getWebhook("discord-webhook.embed.color.random")) {
            embed.setColor(generateRandomColor());
        } else {
            int r = (int) this.handler.getWebhook("discord-webhook.embed.color.rgb-colors.R");
            int g = (int) this.handler.getWebhook("discord-webhook.embed.color.rgb-colors.G");
            int b = (int) this.handler.getWebhook("discord-webhook.embed.color.rgb-colors.B");
            if ((r > 255 || r < 0) || (g > 255 || g < 0) || (b > 255 || b < 0)) {
                this.main.getLogger().warning("§cThe max value of each of the colors is 255 and the minimum value is 0");
                embed.setColor(new Color(0, 0, 0));
            } else {
                embed.setColor(new Color(r, g, b));
            }
        }
        if ((boolean) this.handler.getWebhook("discord-webhook.embed.fields.enabled")) {
            Configuration fields = this.handler.getConfigSelection("discord-webhook.embed.fields.embed-fields");
            for (String f : fields.getKeys()) {
                String name = this.handler.getWebhook("discord-webhook.embed.fields.embed-fields." + f + ".name").toString().replace("%ip%", IP).replace("%player%", player).replace("%time%", time).replace("%country%", country).replace("%countryCode%", countryCode);
                String value = this.handler.getWebhook("discord-webhook.embed.fields.embed-fields." + f + ".value").toString().replace("%ip%", IP).replace("%player%", player).replace("%time%", time).replace("%country%", country).replace("%countryCode%", countryCode);
                boolean inline = (boolean) this.handler.getWebhook("discord-webhook.embed.fields.embed-fields." + f + ".inline");
                embed.addField(name, value, inline);
            }
        }
        if ((boolean) this.handler.getWebhook("discord-webhook.embed.footer.enabled")) {
            String iconURL = null;
            if ((boolean) this.handler.getWebhook("discord-webhook.embed.footer.icon.enabled"))
                iconURL = this.handler.getWebhook("discord-webhook.embed.footer.icon.url").toString().replace("%ip%", IP).replace("%player%", player).replace("%country%", country).replace("%countryCode%", countryCode);
            embed.setFooter(this.handler.getWebhook("discord-webhook.embed.footer.text").toString().replace("%IP%", IP).replace("%player%", player).replace("%time%", time).replace("%country%", country).replace("%countryCode%", countryCode), iconURL);
        }
        if ((boolean) this.handler.getWebhook("discord-webhook.embed.thumbnail.enabled"))
            embed.setThumbnail(this.handler.getWebhook("discord-webhook.embed.thumbnail.icon").toString().replace("%ip%", IP).replace("%player%", player).replace("%country%", country).replace("%countryCode%", countryCode));
        return embed;

    }

}
