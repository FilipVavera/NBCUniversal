package com.profiq.filipvavera.nbcuniversal.edge;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.profiq.filipvavera.nbcuniversal.Utils;

public class Limit {

    @Test(groups = {"edge", "limit", "broken"})
    public void emptyLimit() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        query.put("limit", "");

        // assuming the default value of limit should be taken
        Assert.assertEquals(Utils.getSoundConnection(query).getResponseCode(), HttpURLConnection.HTTP_OK);
        Assert.assertEquals(Utils.getSoundApiResponse(query).getInt("count"), 10);
        Assert.assertEquals(Utils.getSoundApiResponse(query).getJSONArray("results").length(), 10);
    }

    @Test(groups = {"edge", "limit", "broken"})
    public void notNumberLimit() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        query.put("limit", "aaa");

        // assuming the default value of limit should be taken
        Assert.assertEquals(Utils.getSoundConnection(query).getResponseCode(), HttpURLConnection.HTTP_OK);
        Assert.assertEquals(Utils.getSoundApiResponse(query).getInt("count"), 10);
        Assert.assertEquals(Utils.getSoundApiResponse(query).getJSONArray("results").length(), 10);
    }

    @Test(groups = {"edge", "limit"})
    public void zeroLimit() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        query.put("limit", "0");

        Assert.assertEquals(Utils.getSoundConnection(query).getResponseCode(), HttpURLConnection.HTTP_OK);
        Assert.assertEquals(Utils.getSoundApiResponse(query).getInt("count"), 0);
        Assert.assertEquals(Utils.getSoundApiResponse(query).getJSONArray("results").length(), 0);
    }

    @Test(groups = {"edge", "limit", "broken"})
    public void negativeIntegerLimit() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        query.put("limit", "-5");

        // assuming the default value of limit should be taken
        Assert.assertEquals(Utils.getSoundConnection(query).getResponseCode(), HttpURLConnection.HTTP_OK);
        Assert.assertEquals(Utils.getSoundApiResponse(query).getInt("count"), 10);
        Assert.assertEquals(Utils.getSoundApiResponse(query).getJSONArray("results").length(), 10);
    }

    @Test(groups = {"edge", "limit", "broken"})
    public void biggerThenMaxIntegerLimit() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        query.put("limit", "2147483648");

        // it seems that there are 64 sounds total
        Assert.assertEquals(Utils.getSoundConnection(query).getResponseCode(), HttpURLConnection.HTTP_OK);
        Assert.assertEquals(Utils.getSoundApiResponse(query).getInt("count"), 64);
        Assert.assertEquals(Utils.getSoundApiResponse(query).getJSONArray("results").length(), 64);
    }

    @Test(groups = {"edge", "limit", "broken"})
    public void smallerThenMinIntegerLimit() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        query.put("limit", "-2147483649");

        // assuming the default value of limit should be taken
        Assert.assertEquals(Utils.getSoundConnection(query).getResponseCode(), HttpURLConnection.HTTP_OK);
        Assert.assertEquals(Utils.getSoundApiResponse(query).getInt("count"), 10);
        Assert.assertEquals(Utils.getSoundApiResponse(query).getJSONArray("results").length(), 10);
    }
}
