import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class APITest {

    @BeforeClass
    public void setUp() {
        // setup the test
    }

    @Test(groups = { "api" })
    public void apiTest() {
        int len = 50;

        JSONObject response = Utils.getSoundApi("apollo", len, "DEMO_KEY");
        assert response.getJSONArray("results").length() == len;
    }
}
