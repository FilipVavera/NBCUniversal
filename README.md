# NBCUniversal - test task

**Author:** Filip Vavera <filip.vavera@profiq.com>

## Task
Propose API testing testing approach for [NASA Sound API](https://api.nasa.gov/api.html#sounds)

## Solution

**Test areas:**
 * [ ] Standard behavior
   * [x] Consistency
   * [ ] Search behavior (missing documentation)
   * [x] Limit is working
   * [ ] API response structure (missing documentation)
   * [ ] API key working correctly
 * [x] Edge cases behavior
   * [x] Search testing
     * [x] Empty search query
     * [x] Special characters
     * [x] Quote characters (", ')
     * [x] Eval character (`)
     * [x] Executable code in query
     * [x] Query extremely long (more then 10000 characters)
   * [x] Limit testing
     * [x] Empty limit
     * [x] Limit not a number
     * [x] Limit 0
     * [x] Limit negative integer
     * [x] Limit decimal number
     * [x] Limit bigger then max integer (2147483647)
     * [x] Limit lower then min integer (-2147483648)
   * [x] API key testing
     * [x] Empty API key
     * [x] Random API key
     * [x] API key extremely long (more then 10000 characters)
   * [x] Unexpected parameter
     * [x] Unexpected parameter with string
     * [x] Unexpected parameter with number
     * [x] Unexpected empty parameter
 * [ ] Security testing
   * [ ] SSL certificate testing
   * [ ] API key testing (what is his purpose?)
   * [ ] Executable code in query
   * [ ] ...
 * [ ] Load testing
   * [ ] Response time (what is the specification?)

#### Standard behavior
Testing if the expected inputs returns expected outputs.

##### Consistency
Check if two requests with identical query returns same result.

Check if two requests with different API key (but same q and limit) return same result.

##### Search behavior
Test if search behaves as expected. There is no documentation about how the search should work so I cannot check if the result is correct. But it could be implemented for example that way that searched query is in sound's description or in tags.

##### Limit is working
Test if `limit` argument is working as expected. When no argument is passed there should be 10 results by default, when argument is passed there should be that many results.

##### API response structure
Test if returned JSON has correct structure. There is no documentation in that field so I assume the example response has correct structure. Check if other responses has the same structure.

##### API key working correctly
Test if API key is working correctly. There should be limit 1000 requests per hour for one API key. Test if request 1001 returns correct response (not documented so I cannot check for sure). Difficult to test automatically (have to check to run the test just once per hour).

#### Edge case behavior
Test edge case, unexpected and extreme values.

##### Search testing (`q` param)
Test search ability for edge cases.

Test `q` param with no value

Test `q` param with various special characters

Test `q` param with quote chars in it

Test `q` param with eval (\`) in it

Test `q` param with large values (10 000 characters)

##### Limit testing (`limit` param)
Test limit ability for edge cases

Test `limit` param with no value

Test `limit` param with value which is not number

Test `limit` param with 0

Test `limit` param with negative integer

Test `limit` param with integer larger then max integer (2147483647)

Test `limit` param with integer smaller then min integer (-2147483648)

Test `limit` param with decimal number

##### API key testing (`api_key` param)
Test API key feature for edge cases

Test `api_key` param with no value

Test `api_key` param with randomly generated key

Test `api_key` param with large values (10 000 characters)

##### Unexpected parameter
Test API for passing unexpected parameter

Test parameter with string as value

Test parameter with number as value

Test parameter with no value

#### Security testing
Test API for security

Test 403 Unauthorized response is returned to request without API key

Test 403 Unauthorized response is returned to request with not valid API key

Test all HTTP methods are not accessible except the GET method

Test HTTP headers for security issues

Test 4xx and 5xx responses returns correct responses (not stack trace or other sensitive information)
 
#### Load testing
Test API under stress conditions

Test API standard response time

TODO

#### Good practices testing
Test API for good practices

Test if 4xx and 5xx responses returns JSON response

Test if HEAD request is available

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

## Proposed improvements
 * Error responses (4xx and 5xx) should return JSON instead of HTML
