# NBCUniversal - test task

**Author:** Filip Vavera <filip.vavera@profiq.com>

## Task
Propose API testing testing approach for [NASA Sound API](https://api.nasa.gov/api.html#sounds)

## Solution

**Test areas:**
 * Standard behavior
   * Consistency
   * Search behavior (missing documentation)
   * Limit is working
   * API response structure (missing documentation)
   * API key working correctly (what is the purpose?)
 * Edge cases behavior
   * Search testing
     * Empty search query
     * Special characters
     * Quote characters (", ')
     * Eval character (`)
     * Executable code in query
     * Query extremely long (more then 2000 characters)
   * Limit testing
     * Empty limit
     * Limit not a number
     * Limit 0
     * Limit negative integer
     * Limit decimal number
     * Limit bigger then max integer (2147483647)
     * Limit lower then min integer (-2147483648)
   * API key testing
     * Empty API key
     * Random API key
     * API key extremely long (more then 2000 characters)
   * Unexpected parameter
     * ...
 * Security testing
   * SSL certificate testing
   * API key testing (what is his purpose?)
   * Executable code in query
   * ...
 * Load testing
   * Response time (what is the specification?)

### Test approach
Using Java with TestNG test framework

## Found issues

### 1. **Unexpected `limit` argument**

**Description:** when the `limit` argument in URL is empty, not-a-number, negative number, decimal number, bigger then max integer (2147483647) or smaller then min integer (-2147483648) the server exception is invoked and return code is `500 Server Error`

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
