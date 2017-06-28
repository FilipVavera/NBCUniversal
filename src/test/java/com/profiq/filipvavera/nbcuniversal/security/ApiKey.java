package com.profiq.filipvavera.nbcuniversal.security;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.json.HTTP;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.profiq.filipvavera.nbcuniversal.Utils;

public class ApiKey {

    @Test(groups = {"security", "apiKey"})
    public void noApiKey() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");

        HttpURLConnection response = Utils.getSoundConnection(query);

        Assert.assertEquals(response.getResponseCode(), HttpURLConnection.HTTP_FORBIDDEN);
    }


    @Test(groups = {"security", "apiKey"})
    public void emptyApiKey() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", "");

        HttpURLConnection response = Utils.getSoundConnection(query);

        Assert.assertEquals(response.getResponseCode(), HttpURLConnection.HTTP_FORBIDDEN);
    }

    @Test(groups = {"security", "apiKey"})
    public void invalidApiKey() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", "INVALID");

        HttpURLConnection response = Utils.getSoundConnection(query);

        Assert.assertEquals(response.getResponseCode(), HttpURLConnection.HTTP_FORBIDDEN);
    }
}
