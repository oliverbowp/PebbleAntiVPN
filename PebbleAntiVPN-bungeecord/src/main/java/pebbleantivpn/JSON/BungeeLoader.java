package pebbleantivpn.JSON;

import org.json.JSONObject;
import pebbleantivpn.pebbleantivpn.PebbleAntiVPNBungeeCord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BungeeLoader {

    public static void loadJSONData() throws IOException {
        File f = new File("plugins/PebbleAntiVPN/data.json");
        if (!f.exists())
            f.createNewFile();
        String text = getTextFromFile(f);
        if (text == null) return;
        PebbleAntiVPNBungeeCord.IPs = new JSONObject(text);
    }

    public static String getTextFromFile(File f) throws IOException {
        BufferedReader br
                = new BufferedReader(new FileReader(f));
        return br.readLine();
    }

}
