# NBCUniversal - test task

**Author:** Filip Vavera <filip.vavera@profiq.com>

## Task
Propose API testing testing approach for [NASA Sound API](https://api.nasa.gov/api.html#sounds)

## Solution

**Test areas:**
 * [Standard behavior](#standard-behavior)
   * [Consistency](#consistency-automated)
   * [Search behavior](#search-behavior) (missing documentation)
   * [Limit is working](#limit-is-working-automated)
   * [API response structure](#api-response-structure-automated) (missing documentation)
   * [API key is working](#api-key-is-working)
 * [Edge cases behavior](#edge-case-behavior-automated)
   * [Search testing](#search-testing-q-param-automated)
   * [Limit testing](#limit-testing-limit-param-automated)
   * [API key testing](#api-key-testing-api_key-param-automated)
   * [Unexpected parameter](#unexpected-parameter-automated)
 * [Security testing](#security-testing)
   * SSL certificate testing
   * API key testing (what is his purpose?)
   * Executable code in query
   * ...
 * [Load testing](#load-testing)
   * Response time (what is the specification?)

#### Standard behavior
Testing if the expected inputs returns expected outputs.

##### Consistency *(automated)*
 * Check if two requests with identical query returns same result.
 * Check if two requests with different API key (but same q and limit) return same result.

##### Search behavior
Test if search behaves as expected. There is no documentation about how the search should work so I cannot check if the result is correct. But it could be implemented for example that way that searched query is in sound's description, tags or name.

This tests should be automated when the correct search behavior is documented. Until then it will be just guessing.

##### Limit is working *(automated)*
Test if `limit` argument is working as expected. Check the `count` field and also number of entries in `results` field.
 * Test when `limit` is not present there are 10 results
 * Test when `limit` is 1 there is 1 result
 * Test when `limit` is 10 there are 10 results
 * Test when `limit` is 20 there are 20 results
 * Test when `limit` is 57 there are 57 results
 * Test when `limit` is 64 there are 64 results
 * Test when `limit` is 75 there are 64 results (there are just 64 sounds in the API)

##### API response structure *(automated)*
Test if returned JSON has correct structure. There is no documentation in that field so I assume the example response has correct structure. Check if other responses has the same structure.
 * Test if every `results` list entry contains fields:
   * `description` - String or null
   * `download_url` - String
   * `duration` - Integer
   * `id` - Integer
   * `last_modified` - DateTime
   * `license` - String
   * `stream_url` - String
   * `tag_list` - String
   * `title` - String
 * Test if `download_url` has format `"https://api.soundcloud.com/tracks/{id}/download"`
 * Test if `stream_url` has format `"https://api.soundcloud.com/tracks/{id}/stream"`
 * Test if `last_modified` has format `"%Y/%M/%d %H:%m:%s %Z"`

##### API key is working
Test if API key is working correctly.
 * Test if request number 1001 in one hour returns correct response `429 Too many requests` when using normal API key.
 * Test if request number 31 in one hour returns correct response `429 Too many requests` when using DEMO_KEY from one IP address.
 * Test if request number 51 in one day returns correct response `429 Too many requests` when using DEMO_KEY from one IP address.

Difficult to test automatically (have to check to run the test just once per hour/day). Because of other tests there will not be exact number of requests available.

The headers can be tested for `X-RateLimit-Limit` and `X-RateLimit-Remaining` to check the limits but this will not check the actual behavior.

#### Edge case behavior *(automated)*
Test edge case, unexpected and extreme values.

##### Search testing (`q` param) *(automated)*
Test search ability for edge cases.
 * Test `q` param with no value
 * Test `q` param with various special characters
 * Test `q` param with quote chars in it
 * Test `q` param with eval (\`) in it
 * Test `q` param with large values (10 000 characters)

##### Limit testing (`limit` param) *(automated)*
Test limit ability for edge cases
 * Test `limit` param with no value
 * Test `limit` param with value which is not number
 * Test `limit` param with 0
 * Test `limit` param with negative integer
 * Test `limit` param with integer larger then max integer (2 147 483 647)
 * Test `limit` param with integer smaller then min integer (-2 147 483 648)
 * Test `limit` param with decimal number

##### API key testing (`api_key` param) *(automated)*
Test API key feature for edge cases
 * Test `api_key` param with no value
 * Test `api_key` param with randomly generated key
 * Test `api_key` param with large values (10 000 characters)

##### Unexpected parameter *(automated)*
Test API for passing unexpected parameter
 * Test parameter with string as value
 * Test parameter with number as value
 * Test parameter with no value

#### Security testing
Test API for security
 * Test `403 Forbidden` response is returned to request without API key *(automated)*
 * Test `403 Forbidden` response is returned to request with not valid API key *(automated)*
 * Test `429 Too many requests` is returned after 30 requests in hour when using DEMO_KEY from one IP address
 * Test `429 Too many requests` is returned after 50 requests in day when using DEMO_KEY from one IP address
 * Test `429 Too many requests` is returned after 1000 requests in hour when using proper API key
 * Test all HTTP methods are not accessible except the GET method *(automated)*
 * Test HTTP headers for security issues
 * Test 4xx and 5xx responses returns correct responses (not stack trace or other sensitive information)

#### Load testing
 * Test API under stress conditions
   * Difficult to test since there is limit 1000 requests per API key. Testing would require unlimited API key.
   * I would test it with JMeter. Create bunch of small servers on AWS or Google Cloud Platform and run in them Docker image of JMeter slave server (for example [hhcordero/docker-jmeter-server](https://hub.docker.com/r/hhcordero/docker-jmeter-server/) ). And then run lot of concurrent API requests from all of the servers at once and watch the server load. Then mark and compare with specification:
     * Standard load (around CPU load at 75%)
     * Critical load (around CPU load at 90%)
   * I would also closely look how the system behaves on the height load (request drop, higher response time, inconsistent behavior, ...)
   * I would also look how the system behaves on 100% and higher load. Does it crash? Is there some auto-scaling implemented? How it behaves? Etc. but this is highly depended on how the system is designed.
 * Test API standard response time
   * This can and should be automated but I have no information about what response time is acceptable.

#### Good practices testing
Test API for good practices
 * Test if 4xx and 5xx responses returns JSON response
 * Test if HEAD and OPTIONS request methods are available

### Test approach
Using Java with TestNG test framework

## Found issues

### 1. **Unexpected `limit` argument**

**Description:** when the `limit` argument in URL is empty, not-a-number, negative number, decimal number, bigger then max integer (2 147 483 647) or smaller then min integer (-2 147 483 648) the server exception is invoked and return code is `500 Server Error`

**Tested cases:** "", "aa", "agf5", "-5", "-245", "2.45", "8.012", "2147483648", "21474836471", "-2147483649", "-21474836485"

**Steps to reproduce:**
 1. Perform GET request to this URL [https://api.nasa.gov/planetary/sounds?q=apollo&api_key=DEMO_KEY&limit=aa](https://api.nasa.gov/planetary/sounds?q=apollo&api_key=DEMO_KEY&limit=aa)
 2. Check the response

 *Expected result:* default limit (10) is used or response is `400 Bad request`

 *Actual result:* response is `500 Server Error`

**Probable cause:** there is no check if the `hits` key is present in array

**Possible fix:** instead of `res = [x['_source'] for x in es_res['hits']['hits']]` use `res = [x.get('_source') for x in es_res.get('hits', {}).get('hits', {})`

### 2. Response `500 Server Error` contains stack trace - **!SECURITY RISK!**
**Description:** when Server Error happens the response contains stack trace

**Step to reproduce:**
 1. Perform request which causes `500 Server Error` for example with empty `limit` argument [https://api.nasa.gov/planetary/sounds?q=apollo&api_key=DEMO_KEY&limit=](https://api.nasa.gov/planetary/sounds?q=apollo&api_key=DEMO_KEY&limit=)
 2. Check the response

 *Expected result:* response contains just some general information about server experiencing error

 *Actual result:* response contains actual stack trace

**Probable cause:** the DEBUG mode is turn on on production server

**Possible fix:** turn off the DEBUG mode in production

### 3. Search doesn't seems to be working
**Description:** when trying to search for some results with `q` parameter the API returns the same set of results for every `q` value. In the documentation there is no description of how the search feature should work but this behavior doesn't seems right.

**Step to reproduce:**
 1. Call the API with different `q` params (for example "apollo", "voyager", "car", "fridge", "asasdads", etc.)
 2. Compare the results

 *Expected result:* responses should differ and each response should only contains the results with the `q` param value in description or name.

 *Actual result:* the responses are identical even when the `q` param is completely nonsense

**Probable cause:** search feature is not implemented

**Possible fix:** implement search feature

### 4. Headers contains sensitive information
**Description:** responses contains headers with sensitive information. For example:
 * `Server: openresty` - if somebody would decide to attack the API it is really valuable information to know on what server it is running. The attacker can focus on finding vulnerabilities in this server
 * `Via: http/1.1 api-umbrela (ApacheTrafficServer [cMsSf ])` - same as previous the attacker now know that the server is using api-umbrella with ApacheTrafficServer to limit requests for API key

**Step to reproduce:**
 1. Perform any request to the API
 2. Examine the headers

 *Expected result:* the headers containing sensitive information are not present

 *Actual result:* the headers `Server` and `Via` contains sensitive information about underlying servers

 **Probable cause:** the default server headers are used without proper server configuration

 **Possible fix:** remove this headers in the server settings

## Proposed improvements
 * Error responses (4xx and 5xx) should return JSON instead of HTML
   * It is convenient when the API is consistent and developers can relay on it that they gets proper description when when something went wrong
   * HTML is good for browser exploring so maybe enable `Accept` header
 * Enable `Accept` header when header is `Accept: application/json` return JSON (even when 4xx or 5xx errors occurs) when header is `Accept: text/html` return formatted JSON (for better reading in browser) and HTML error pages. The default should be `Accept: application/json`
 * Enable HEAD and OPTIONS requests
