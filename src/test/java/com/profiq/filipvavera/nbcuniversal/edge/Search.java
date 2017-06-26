package com.profiq.filipvavera.nbcuniversal.edge;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.profiq.filipvavera.nbcuniversal.Utils;

public class Search {
    @Test(groups = {"edge", "search"})
    public void emptySearchQuery() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", Utils.apiKey);
        query.put("q", "");

        Assert.assertEquals(Utils.getSoundResponse(query).getResponseCode(), HttpURLConnection.HTTP_OK);
    }

    @Test(groups = {"edge", "search"})
    public void specialCharacters() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", Utils.apiKey);
        query.put("q", "aaa%aaa");

        // TODO (Filip Vavera): add more test cases

        Assert.assertEquals(Utils.getSoundResponse(query).getResponseCode(), HttpURLConnection.HTTP_OK);
    }

    @Test(groups = {"edge", "search"})
    public void simpleQuoteCharacters() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", Utils.apiKey);
        query.put("q", "aaa\'aaa\'aaa");

        Assert.assertEquals(Utils.getSoundResponse(query).getResponseCode(), HttpURLConnection.HTTP_OK);
    }

    @Test(groups = {"edge", "search"})
    public void doubleQuoteCharacters() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", Utils.apiKey);
        query.put("q", "aaa\"aaa\"aaa");

        Assert.assertEquals(Utils.getSoundResponse(query).getResponseCode(), HttpURLConnection.HTTP_OK);
    }

    @Test(groups = {"edge", "search"})
    public void evalCharacter() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", Utils.apiKey);
        query.put("q", "aaa`aaa`aaa");

        Assert.assertEquals(Utils.getSoundResponse(query).getResponseCode(), HttpURLConnection.HTTP_OK);
    }

    @Test(groups = {"edge", "search"})
    public void longQuery() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", Utils.apiKey);
        query.put("q", Utils.repeat(500, "c"));

        Assert.assertEquals(Utils.getSoundResponse(query).getResponseCode(), HttpURLConnection.HTTP_OK);
    }

    @Test(groups = {"edge", "search"})
    public void reallyLongQuery() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", Utils.apiKey);
        query.put("q", Utils.repeat(2500, "c"));

        Assert.assertEquals(Utils.getSoundResponse(query).getResponseCode(), HttpURLConnection.HTTP_OK);
    }

    @Test(groups = {"edge", "search"})
    public void extremelyLongQuery() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", Utils.apiKey);
        query.put("q", Utils.repeat(10000, "c"));

        Assert.assertEquals(Utils.getSoundResponse(query).getResponseCode(), HttpURLConnection.HTTP_REQ_TOO_LONG);
    }
}
