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
    private static String nasaHost = "api.nasa.gov";
    private static String soundEndpoint = "/planetary/sounds";
    private static String url = MessageFormat.format("https://{0}{1}", nasaHost, soundEndpoint);

    public static String apiKey = "FABg8NWeqri3U3f9wGXzcVygHj8KTSrAthqoKoG7";

    /**
     * Construct URL with query params
     *
     * @param query query params for the URL
     * @return URL as String
     */
    private static String constructSoundApiUrl(final Map<String, String> query) {
        StringBuilder urlBuilder = new StringBuilder(Utils.url);
        urlBuilder.append("?");

        boolean first = true;
        for (Map.Entry<String, String> entry : query.entrySet()) {
            if (!first) urlBuilder.append("&");
            else first = false;

            urlBuilder.append(MessageFormat.format("{0}={1}", entry.getKey(), entry.getValue()));
        }

        return urlBuilder.toString();
    }

    /**
     * Create HTTP connection to the given URL
     *
     * @param url endpoint to create connection
     * @param method HTTP request method
     * @return HttpURLConnection
     * @throws IOException when URL cannot be parsed
     */
    private static HttpURLConnection connection(final String url, final String method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);

        return connection;
    }

    /**
     * Create HTTP GET connection to the given URL
     *
     * @param url endpoint to create connection
     * @return HttpURLConnection
     * @throws IOException when URL cannot be parsed
     */
    private static HttpURLConnection getConnection(final String url) throws IOException {
        return Utils.connection(url, "GET");
    }

    /**
     * Create HTTP POST connection to the given URL
     *
     * @param url endpoint to create connection
     * @return HttpURLConnection
     * @throws IOException when URL cannot be parsed
     */
    private static HttpURLConnection postConnection(final String url) throws IOException {
        return Utils.connection(url, "POST");
    }

    /**
     * Create HTTP PUT connection to the given URL
     *
     * @param url endpoint to create connection
     * @return HttpURLConnection
     * @throws IOException when URL cannot be parsed
     */
    private static HttpURLConnection putConnection(final String url) throws IOException {
        return Utils.connection(url, "PUT");
    }

    /**
     * Create HTTP DELETE connection to the given URL
     *
     * @param url endpoint to create connection
     * @return HttpURLConnection
     * @throws IOException when URL cannot be parsed
     */
    private static HttpURLConnection deleteConnection(final String url) throws IOException {
        return Utils.connection(url, "DELETE");
    }

    /**
     * Create HTTP GET connection to the NASA Sound API endpoint with given query params
     *
     * @param query query params for the NASA Sound API endpoint
     * @return HttpURLConnection
     */
    public static HttpURLConnection getSoundConnection(final Map<String, String> query) throws IOException {
        return Utils.getConnection(Utils.constructSoundApiUrl(query));
    }

    /**
     * Create HTTP POST connection to the NASA Sound API endpoint
     *
     * @return HttpURLConnection
     */
    public static HttpURLConnection postSoundConnection(final Map<String, String> query) throws IOException {
        return Utils.postConnection(Utils.constructSoundApiUrl(query));
    }

    /**
     * Create HTTP PUT connection to the NASA Sound API endpoint
     *
     * @return HttpURLConnection
     */
    public static HttpURLConnection putSoundConnection(final Map<String, String> query) throws IOException {
        return Utils.putConnection(Utils.constructSoundApiUrl(query));
    }

    /**
     * Create HTTP DELETE connection to the NASA Sound API endpoint
     *
     * @return HttpURLConnection
     */
    public static HttpURLConnection deleteSoundConnection(final Map<String, String> query) throws IOException {
        return Utils.deleteConnection(Utils.constructSoundApiUrl(query));
    }

    /**
     * Parse HttpURLConnection to the JSONObject response
     *
     * @param connection connection to parse
     * @return response as JSONObject
     */
    private static JSONObject parseJsonResponse(final HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() < HttpURLConnection.HTTP_OK
                || connection.getResponseCode() >= HttpURLConnection.HTTP_MULT_CHOICE) {
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

    /**
     * Get response from NASA Sound API as JSONObject
     *
     * @param query query for the Sound endpoint
     * @return response as JSONObject
     */
    public static JSONObject getSoundApiResponse(final Map<String, String> query) throws IOException {
        return Utils.parseJsonResponse(Utils.getSoundConnection(query));
    }

    /**
     * Generate string
     *
     * @param count how many times repeat
     * @param with what string repeat
     * @return new string
     */
    public static String repeat(int count, String with) {
        return new String(new char[count]).replace("\0", with);
    }

    /**
     * Generate random String with given characters and length
     *
     * @param characters permitted characters in generated string
     * @param length desired length of the new string
     * @return new string
     */
    private static String generateString(String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(Utils.random.nextInt(characters.length()));
        }
        return new String(text);
    }

    /**
     * Generate new random API key
     *
     * API key can contain only alphanumeric characters
     *
     * @param length desired length of the API key
     * @return new API key
     */
    public static String generateApiKey(final int length) {
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String upper = lower.toUpperCase();
        String numbers = "0123456789";

        return Utils.generateString(lower + upper + numbers, length);
    }

    /**
     * Generate new random API key with default length (40)
     *
     * @return new API key
     */
    public static String generateApiKey() {
        return Utils.generateApiKey(40);
    }
}
