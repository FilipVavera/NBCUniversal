package com.profiq.filipvavera.nbcuniversal.standard;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.profiq.filipvavera.nbcuniversal.Utils;


public class Search {
    @Test(groups = {"standard", "search"})
    public void testSearchResults() {
        Map<String, String> query = new HashMap<>();
        query.put("q", "apollo");
        query.put("api_key", Utils.apiKey);
        query.put("foo", "boo");

        // TODO (Filip Vavera): define search behavior and implement test
    }
}
