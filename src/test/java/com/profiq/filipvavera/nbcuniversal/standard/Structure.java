package com.profiq.filipvavera.nbcuniversal.standard;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.profiq.filipvavera.nbcuniversal.Utils;

public class Structure {

    @Test(groups = {"standard", "structure"})
    public void resultFields() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        // to make sure every sound is included
        query.put("limit", "1000");

        JSONObject response = Utils.getSoundApiResponse(query);
        JSONArray results = response.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);

            Assert.assertTrue(result.has("description"));
            Assert.assertTrue(result.has("download_url"));
            Assert.assertTrue(result.has("duration"));
            Assert.assertTrue(result.has("id"));
            Assert.assertTrue(result.has("last_modified"));
            Assert.assertTrue(result.has("license"));
            Assert.assertTrue(result.has("stream_url"));
            Assert.assertTrue(result.has("tag_list"));
            Assert.assertTrue(result.has("title"));
        }
    }

    @Test(groups = {"standard", "structure"})
    public void downloadUrlField() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        // to make sure every sound is included
        query.put("limit", "1000");

        JSONObject response = Utils.getSoundApiResponse(query);
        JSONArray results = response.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String expected = MessageFormat.format("https://api.soundcloud.com/tracks/{0}/download",
                    result.getBigInteger("id").toString());

            Assert.assertEquals(result.getString("download_url"), expected);
        }
    }

    @Test(groups = {"standard", "structure"})
    public void streamUrlField() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        // to make sure every sound is included
        query.put("limit", "1000");

        JSONObject response = Utils.getSoundApiResponse(query);
        JSONArray results = response.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String expected = MessageFormat.format("https://api.soundcloud.com/tracks/{0}/stream",
                    result.getBigInteger("id").toString());

            Assert.assertEquals(result.getString("stream_url"), expected);
        }
    }
}
