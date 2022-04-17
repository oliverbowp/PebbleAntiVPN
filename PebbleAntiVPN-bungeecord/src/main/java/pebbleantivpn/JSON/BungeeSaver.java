package pebbleantivpn.JSON;

import pebbleantivpn.pebbleantivpn.PebbleAntiVPNBungeeCord;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class BungeeSaver {

    public static void saveJSONData() throws IOException {
        File f = new File("plugins/PebbleAntiVPN/data.json");
        if (f.exists())
            f.delete();
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("plugins/PebbleAntiVPN/data.json"), StandardCharsets.UTF_8))) {
            writer.write(String.valueOf(PebbleAntiVPNBungeeCord.IPs));
        }
    }

}
