package com.profiq.filipvavera.nbcuniversal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Map;

import org.json.JSONObject;

public class Utils {

    private static SecureRandom random = new SecureRandom();
    public static String apiKey = "FABg8NWeqri3U3f9wGXzcVygHj8KTSrAthqoKoG7";
    private static String nasaHost = "api.nasa.gov";
    private static String soundEndpoint = "/planetary/sounds";
    private static String url = MessageFormat.format("https://{0}{1}", nasaHost, soundEndpoint);

    private static String constructSoundApiUrl(final Map<String, String> query) {
        StringBuilder urlBuilder = new StringBuilder(Utils.url);
        urlBuilder.append("?");

        boolean first = true;
        for (Map.Entry<String, String> entry : query.entrySet()) {
            if (!first) {
                urlBuilder.append("&");
            }
            first = false;

            urlBuilder.append(MessageFormat.format("{0}={1}", entry.getKey(), entry.getValue()));
        }

        return urlBuilder.toString();
    }

    private static HttpURLConnection getResponse(final String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        return connection;
    }

    public static HttpURLConnection getSoundResponse(final Map<String, String> query) throws IOException {
        return Utils.getResponse(Utils.constructSoundApiUrl(query));
    }

    private static JSONObject getApiResponse(final String url) throws IOException {
        HttpURLConnection connection = Utils.getResponse(url);

        if (connection.getResponseCode() < HttpURLConnection.HTTP_OK || connection.getResponseCode() >= HttpURLConnection.HTTP_MULT_CHOICE) {
            throw new IOException(MessageFormat.format("Response code is: {0}", connection.getResponseCode()));
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return new JSONObject(response.toString());
    }

    public static JSONObject getSoundApiResponse(final Map<String, String> query) throws IOException {
        return Utils.getApiResponse(Utils.constructSoundApiUrl(query));
    }

    public static String repeat(int count, String with) {
        return new String(new char[count]).replace("\0", with);
    }

    private static String generateString(String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(Utils.random.nextInt(characters.length()));
        }
        return new String(text);
    }

    public static String generateApiKey(final int length) {
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String upper = lower.toUpperCase();
        String numbers = "0123456789";

        return Utils.generateString(lower + upper + numbers, length);
    }

    public static String generateApiKey() {
        return Utils.generateApiKey(40);
    }
}
