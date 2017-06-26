package com.profiq.filipvavera.nbcuniversal.edge;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.profiq.filipvavera.nbcuniversal.Utils;

public class ApiKey {

    @Test(groups = {"edge", "apiKey"})
    public void emptyApiKey() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", "");

        Assert.assertEquals(Utils.getSoundResponse(query).getResponseCode(), HttpURLConnection.HTTP_FORBIDDEN);
    }

    @Test(groups = {"edge", "apiKey"})
    public void randomApiKey() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", Utils.generateApiKey());

        Assert.assertEquals(Utils.getSoundResponse(query).getResponseCode(), HttpURLConnection.HTTP_FORBIDDEN);
    }

    @Test(groups = {"edge", "apiKey"})
    public void longApiKey() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", Utils.generateApiKey(200));

        Assert.assertEquals(Utils.getSoundResponse(query).getResponseCode(), HttpURLConnection.HTTP_FORBIDDEN);
    }

    @Test(groups = {"edge", "apiKey"})
    public void reallyLongApiKey() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", Utils.generateApiKey(2000));

        Assert.assertEquals(Utils.getSoundResponse(query).getResponseCode(), HttpURLConnection.HTTP_FORBIDDEN);
    }

    @Test(groups = {"edge", "apiKey"})
    public void extremelyLongApiKey() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", Utils.generateApiKey(10000));

        Assert.assertEquals(Utils.getSoundResponse(query).getResponseCode(), HttpURLConnection.HTTP_REQ_TOO_LONG);
    }
}
