package com.profiq.filipvavera.nbcuniversal.security;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.profiq.filipvavera.nbcuniversal.Utils;

public class HttpMethods {

    @Test(groups = {"security", "httpMethods"})
    public void postMethod() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", Utils.apiKey);

        HttpURLConnection connection = Utils.postSoundConnection(query);

        Assert.assertEquals(connection.getResponseCode(), HttpURLConnection.HTTP_BAD_METHOD);
    }

    @Test(groups = {"security", "httpMethods"})
    public void putMethod() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", Utils.apiKey);

        HttpURLConnection connection = Utils.putSoundConnection(query);

        Assert.assertEquals(connection.getResponseCode(), HttpURLConnection.HTTP_BAD_METHOD);
    }

    // TODO (Filip Vavera): add PATCH method test (HttpURLConnection doesn't support PATCH method)

    @Test(groups = {"security", "httpMethods"})
    public void deleteMethod() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", Utils.apiKey);

        HttpURLConnection connection = Utils.deleteSoundConnection(query);

        Assert.assertEquals(connection.getResponseCode(), HttpURLConnection.HTTP_BAD_METHOD);
    }
}
