package com.profiq.filipvavera.nbcuniversal.standard;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.profiq.filipvavera.nbcuniversal.Utils;

public class Limit {

    @Test(groups = {"standard", "limit"})
    public void testDefaultLimit() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);

        JSONObject response = Utils.getSoundApiResponse(query);
        Assert.assertEquals(response.getInt("count"), 10);
        Assert.assertEquals(response.getJSONArray("results").length(), 10);
    }

    @Test(groups = {"standard", "limit"})
    public void testLimit1() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        query.put("limit", "1");

        JSONObject response = Utils.getSoundApiResponse(query);
        Assert.assertEquals(response.getInt("count"), 1);
        Assert.assertEquals(response.getJSONArray("results").length(), 1);
    }

    @Test(groups = {"standard", "limit"})
    public void testLimit10() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        query.put("limit", "10");

        JSONObject response = Utils.getSoundApiResponse(query);
        Assert.assertEquals(response.getInt("count"), 10);
        Assert.assertEquals(response.getJSONArray("results").length(), 10);
    }

    @Test(groups = {"standard", "limit"})
    public void testLimit20() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        query.put("limit", "20");

        JSONObject response = Utils.getSoundApiResponse(query);
        Assert.assertEquals(response.getInt("count"), 20);
        Assert.assertEquals(response.getJSONArray("results").length(), 20);
    }

    @Test(groups = {"standard", "limit"})
    public void testLimit57() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        query.put("limit", "57");

        JSONObject response = Utils.getSoundApiResponse(query);
        Assert.assertEquals(response.getInt("count"), 57);
        Assert.assertEquals(response.getJSONArray("results").length(), 57);
    }

    @Test(groups = {"standard", "limit"})
    public void testLimit64() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        query.put("limit", "64");

        JSONObject response = Utils.getSoundApiResponse(query);
        Assert.assertEquals(response.getInt("count"), 64);
        Assert.assertEquals(response.getJSONArray("results").length(), 64);
    }

    @Test(groups = {"standard", "limit"})
    public void testLimit75() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        query.put("limit", "75");

        JSONObject response = Utils.getSoundApiResponse(query);

        // there are 64 entries now, can change in the future
        Assert.assertEquals(response.getInt("count"), 64);
        Assert.assertEquals(response.getJSONArray("results").length(), 64);
    }
}
