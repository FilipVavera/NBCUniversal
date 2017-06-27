package com.profiq.filipvavera.nbcuniversal.edge;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.profiq.filipvavera.nbcuniversal.Utils;

public class Unexpected {

    @Test(groups = {"edge", "unexpected"})
    public void unexpectedKeyWithString() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        query.put("foo", "boo");

        Assert.assertEquals(Utils.getSoundConnection(query).getResponseCode(), HttpURLConnection.HTTP_OK);
    }

    @Test(groups = {"edge", "unexpected"})
    public void unexpectedKeyWithInteger() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        query.put("foo", "10");

        Assert.assertEquals(Utils.getSoundConnection(query).getResponseCode(), HttpURLConnection.HTTP_OK);
    }

    @Test(groups = {"edge", "unexpected"})
    public void unexpectedEmptyKey() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        query.put("foo", "");

        Assert.assertEquals(Utils.getSoundConnection(query).getResponseCode(), HttpURLConnection.HTTP_OK);
    }
}
