package chong.mycamera;


import android.util.Base64;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testString() throws UnsupportedEncodingException {
        String base64String = "X19iaXo9TWpNNU9EWXpNRGN3TUElM0QlM0Qmc2NlbmU9JnVybF9saXN0PSU3QiUyMnVybF9saXN0\\nJTIyJTNBJTVCJTIyaHR0cHMlMjUzQSUyNTJGJTI1MkZtcC53ZWl4aW4ucXEuY29tJTI1MkZtcCUy\\nNTJGcHJvZmlsZV9leHQlMjUzRmFjdGlvbiUyNTNEaG9tZSUyNTI2X19iaXolMjUzRE1qTTVPRFl6\\nTURjd01BJTI1M0QlMjUzRCUyNTI2ZGV2aWNldHlwZSUyNTNEYW5kcm9pZC0yMyUyNTI2dmVyc2lv\\nbiUyNTNEMjcwMDA1MzclMjUyNmxhbmclMjUzRHpoX0NOJTI1MjZuZXR0eXBlJTI1M0QzZ25ldCUy\\nNTI2YThzY2VuZSUyNTNEMSUyNTI2cGFzc190aWNrZXQlMjUzRHk1UkxMQWxpWSUyNTI1MkI0NUkl\\nMjUyNTJCWkx0S1NEbEZhd1p1cWppM05qaW9kZERFVUklMjUyNTJGNjJOcW8yanhFWFJKclZ3OUdZ\\nSWY0dnclMjUyNnd4X2hlYWRlciUyNTNEMSUyMiU1RCU3RA==\\n";
        base64String = base64String.replace("\\n","");
        byte[] b =java.util.Base64.getDecoder().decode(base64String);
        String s = new String(b, Charset.forName("utf8"));
        String decode = URLDecoder.decode(s, "utf-8");
        decode = URLDecoder.decode(decode, "utf-8");
        System.out.println(decode);
    }
}

