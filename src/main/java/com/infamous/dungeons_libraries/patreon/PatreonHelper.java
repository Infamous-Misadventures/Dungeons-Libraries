package com.infamous.dungeons_libraries.patreon;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PatreonHelper {
    private static final Map<UUID, Patreon> patreons = new HashMap<>();

    public static void loadPatreons(Runnable runnable){
        new Thread(() -> {
            try
            {
                final SSLSocketFactory sslsocketfactory = HttpsURLConnection.getDefaultSSLSocketFactory();
                final URL url = new URL("https://raw.githubusercontent.com/Patrigan/Dungeons-Libraries/alpha/src/main/resources/patreon/patreons.json");
                final HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                conn.setSSLSocketFactory(sslsocketfactory);

                final InputStream responseBody = conn.getInputStream();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody));

                JsonArray asJsonArray = new JsonParser().parse(reader).getAsJsonArray();
                patreons.clear();
                asJsonArray.forEach(jsonElement -> {
                    if(jsonElement.isJsonObject()) {
                        JsonObject asJsonObject = jsonElement.getAsJsonObject();
                        if (asJsonObject.has("uuid") && asJsonObject.has("level")){
                            String uuidString = asJsonObject.get("uuid").getAsString();
                            try{
                                UUID uuid = UUID.fromString(uuidString);
                                patreons.put(
                                        uuid,
                                        new Patreon(
                                                uuid,
                                                PatreonLevel.byName(asJsonObject.get("level").getAsString(), PatreonLevel.UNKNOWN),
                                                asJsonObject.has("username") ? asJsonObject.get("username").getAsString() : ""
                                        )
                                );
                            } catch (IllegalArgumentException e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
                runnable.run();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    public static Patreon getPatreon(UUID uuid){
        return patreons.getOrDefault(uuid, Patreon.DEFAULT);
    }
}
