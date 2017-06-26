package com.profiq.filipvavera.nbcuniversal.standard;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.profiq.filipvavera.nbcuniversal.Utils;

public class Consistency {

    @Test(groups = {"standard", "consistency"})
    public void sameResultWithSameQuery() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("limit", "10");
        query.put("api_key", Utils.apiKey);

        JSONObject request1 = Utils.getSoundApiResponse(query);
        JSONObject request2 = Utils.getSoundApiResponse(query);

        Assert.assertEquals(request1.toString(), request2.toString());
    }

    @Test(groups = {"standard", "consistency"})
    public void sameResultWithDifferentApiKey() throws IOException {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("limit", "10");
        query.put("api_key", Utils.apiKey);

        JSONObject request1 = Utils.getSoundApiResponse(query);

        query.replace("api_key", "DEMO_KEY");
        JSONObject request2 = Utils.getSoundApiResponse(query);

        Assert.assertEquals(request1.toString(), request2.toString());
    }
}
