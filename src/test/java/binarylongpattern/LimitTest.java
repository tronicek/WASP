package binarylongpattern;

import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for selected texts and patterns.
 *
 * @author Zdenek Tronicek
 */
public class LimitTest {

    private void testShiftAndRows(String text, String pattern, int window) {
        int expected = Naive.search(text, pattern, window);
        assertTrue(expected > 0);
        int result = ShiftAndRows.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testTextPreprocessingMapInt(String text, String pattern, int window) {
        int expected = Naive.search(text, pattern, window);
        assertTrue(expected > 0);
        Map<Long, Integer> map = TextPreprocessingMapInt.preprocess(text, window);
        int result = TextPreprocessingMapInt.search(map, pattern, window);
        assertEquals(expected, result);
    }

    @Test
    public void testSAR1() {
        String text = "abaabaaabaaaabaaaaabaaaaaabbaaaaaaabbaaaaaaaabaaaaaaaaabaaaaaaaaab";
        String pattern = "abaabaaabaaaabaaaaabaaaaaabaaaaaaabaaaaaaaabaaaaaaaaabaaaaaaaaab";
        testShiftAndRows(text, pattern, 66);
    }

    @Test
    public void testSAR2() {
        String text = "abaabaaabaaaabaaaaabaaaaaabbaaaaaaabbaaaaaaaabaaaaaaaaabaaaaaaaaab"
                + "abaabaaabaaaabaaaaabaaaaaabbaaaaaaabbaaaaaaaabaaaaaaaaabaaaaaaaaab";
        String pattern = "abaabaaabaaaabaaaaabaaaaaabaaaaaaabaaaaaaaabaaaaaaaaabaaaaaaaaab";
        testShiftAndRows(text, pattern, 66);
    }

    @Test
    public void testTPMI1() {
        String text = "abaabaaabaaaabaaaaabaaaaabababbaabaabaaabaaaabaaaaabaaaaabbbba";
        String pattern = "abaabaaabaaaabaaaaabaaaaabbbbaabaabaaabaaaabaaaaabaaaaabbbba";
        testTextPreprocessingMapInt(text, pattern, 62);
    }

    @Test
    public void testTPMI2() {
        String text = "abaabaaabaaaabaaaaabaaaaabababbaabaabaaabaaaabaaaaabaaaaabbbba"
                + "abaabaaabaaaabaaaaabaaaaabababbaabaabaaabaaaabaaaaabaaaaabbbba";
        String pattern = "abaabaaabaaaabaaaaabaaaaabbbbaabaabaaabaaaabaaaaabaaaaabbbba";
        testTextPreprocessingMapInt(text, pattern, 62);
    }

}
