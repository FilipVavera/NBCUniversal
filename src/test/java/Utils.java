import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import org.json.JSONObject;

class Utils {

    private static String nasaHost = "api.nasa.gov";
    private static String soundEndpoint = "/planetary/sounds";
    private static String url = MessageFormat.format("https://{0}{1}", nasaHost, soundEndpoint);

    static JSONObject getApi(final String url) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new JSONObject(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    static JSONObject getSoundApi(final String q, final int limit, final String apiKey) {
        StringBuilder url = new StringBuilder(Utils.url);

        if (q != null || limit != -1 || apiKey != null) {
            url.append("?");
            if (q != null) url.append(MessageFormat.format("q={0}", q));
            if (limit != -1) url.append(MessageFormat.format("&limit={0}", limit));
            if (apiKey != null) url.append(MessageFormat.format("&api_key={0}", apiKey));
        }

        return getApi(url.toString());
    }

    static JSONObject getSoundApi(final String q, final String apiKey) {
        return getSoundApi(q, -1, apiKey);
    }

    static JSONObject getSoundApi(final String q, final int limit) {
        return getSoundApi(q, limit, null);
    }

    static JSONObject getSoundApi(final String q) {
        return getSoundApi(q, -1, null);
    }

    static JSONObject getSoundApi(final int limit, final String apiKey) {
        return getSoundApi(null, limit, apiKey);
    }

    static JSONObject getSoundApi(final int limit) {
        return getSoundApi(null, limit, null);
    }

    static JSONObject getSoundApi() {
        return getSoundApi(null, -1, null);
    }
}
