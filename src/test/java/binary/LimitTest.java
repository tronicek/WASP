package binary;

import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for selected texts and patterns.
 *
 * @author Zdenek Tronicek
 */
public class LimitTest {

    private void testBCGM(String text, String pattern, int window) {
        int expected = Naive.search(text, pattern, window);
        assertTrue(expected > 0);
        int result = BCGM.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftAndColFull(String text, String pattern, int window) {
        int expected = Naive.search(text, pattern, window);
        assertTrue(expected > 0);
        int result = ShiftAndColFull.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftAndColReduced(String text, String pattern, int window) {
        int expected = Naive.search(text, pattern, window);
        assertTrue(expected > 0);
        int result = ShiftAndColReduced.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftAndRowFull(String text, String pattern, int window) {
        int expected = Naive.search(text, pattern, window);
        assertTrue(expected > 0);
        int result = ShiftAndRowFull.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftAndRowReduced(String text, String pattern, int window) {
        int expected = Naive.search(text, pattern, window);
        assertTrue(expected > 0);
        int result = ShiftAndRowReduced.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftOrColFull(String text, String pattern, int window) {
        int expected = Naive.search(text, pattern, window);
        assertTrue(expected > 0);
        int result = ShiftOrColFull.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftOrColReduced(String text, String pattern, int window) {
        int expected = Naive.search(text, pattern, window);
        assertTrue(expected > 0);
        int result = ShiftOrColReduced.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftOrRowFull(String text, String pattern, int window) {
        int expected = Naive.search(text, pattern, window);
        assertTrue(expected > 0);
        int result = ShiftOrRowFull.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftOrRowReduced(String text, String pattern, int window) {
        int expected = Naive.search(text, pattern, window);
        assertTrue(expected > 0);
        int result = ShiftOrRowReduced.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testWindowAutomaton(String text, String pattern, int window) {
        int expected = Naive.search(text, pattern, window);
        assertTrue(expected > 0);
        int result = WindowAutomaton.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testWindowAutomatonBit(String text, String pattern, int window) {
        int expected = Naive.search(text, pattern, window);
        assertTrue(expected > 0);
        int result = WindowAutomatonBit.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testTextPreprocessing(String text, String pattern, int window) {
        int expected = Naive.search(text, pattern, window);
        assertTrue(expected > 0);
        int[] freq = TextPreprocessing.preprocess(text, window);
        int result = TextPreprocessing.search(freq, pattern, window);
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
    public void testBCGM1() {
        String text = "abaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbb";
        String pattern = "abaaabbb";
        testBCGM(text, pattern, 126);
    }

    @Test
    public void testBCGM2() {
        String text = "abaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbb"
                + "abaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbb";
        String pattern = "abaaabbb";
        testBCGM(text, pattern, 126);
    }

    @Test
    public void testSACF1() {
        String text = "abaabbbbbbbbaa";
        String pattern = "abaabaa";
        testShiftAndColFull(text, pattern, 14);
    }

    @Test
    public void testSACF2() {
        String text = "abaabbbbbbbbaaabaabbbbbbbbaa";
        String pattern = "abaabaa";
        testShiftAndColFull(text, pattern, 14);
    }

    @Test
    public void testSACR1() {
        String text = "abaabbbbbababba";
        String pattern = "abaabaaa";
        testShiftAndColReduced(text, pattern, 15);
    }

    @Test
    public void testSACR2() {
        String text = "abaabbbbbababbaabaabbbbbababba";
        String pattern = "abaabaaa";
        testShiftAndColReduced(text, pattern, 15);
    }

    @Test
    public void testSARF1() {
        String text = "abaabbbbbbbbaa";
        String pattern = "abaabaa";
        testShiftAndRowFull(text, pattern, 14);
    }

    @Test
    public void testSARF2() {
        String text = "abaabbbbbbbbaaabaabbbbbbbbaa";
        String pattern = "abaabaa";
        testShiftAndRowFull(text, pattern, 14);
    }

    @Test
    public void testSARR1() {
        String text = "abaabbbbbababba";
        String pattern = "abaabaaa";
        testShiftAndRowReduced(text, pattern, 15);
    }

    @Test
    public void testSARR2() {
        String text = "abaabbbbbababbaabaabbbbbababba";
        String pattern = "abaabaaa";
        testShiftAndRowReduced(text, pattern, 15);
    }

    @Test
    public void testSOCF1() {
        String text = "abaabbbbbbbbaa";
        String pattern = "abaabaa";
        testShiftOrColFull(text, pattern, 14);
    }

    @Test
    public void testSOCF2() {
        String text = "abaabbbbbbbbaaabaabbbbbbbbaa";
        String pattern = "abaabaa";
        testShiftOrColFull(text, pattern, 14);
    }

    @Test
    public void testSOCR1() {
        String text = "abaabbbbbababba";
        String pattern = "abaabaaa";
        testShiftOrColReduced(text, pattern, 15);
    }

    @Test
    public void testSOCR2() {
        String text = "abaabbbbbababbaabaabbbbbababba";
        String pattern = "abaabaaa";
        testShiftOrColReduced(text, pattern, 15);
    }

    @Test
    public void testSORF1() {
        String text = "abaabbbbbbbbaa";
        String pattern = "abaabaa";
        testShiftOrRowFull(text, pattern, 14);
    }

    @Test
    public void testSORF2() {
        String text = "abaabbbbbbbbaaabaabbbbbbbbaa";
        String pattern = "abaabaa";
        testShiftOrRowFull(text, pattern, 14);
    }

    @Test
    public void testSORR1() {
        String text = "abaabbbbbababba";
        String pattern = "abaabaaa";
        testShiftOrRowReduced(text, pattern, 15);
    }

    @Test
    public void testSORR2() {
        String text = "abaabbbbbababbaabaabbbbbababba";
        String pattern = "abaabaaa";
        testShiftOrRowReduced(text, pattern, 15);
    }

    @Test
    public void testWA1() {
        String text = "abaabaaabaaaaabbbaaaaaabaaaaab";
        String pattern = "abaabaaabaaaabbbaaaaabaaaaab";
        testWindowAutomaton(text, pattern, 30);
    }

    @Test
    public void testWA2() {
        String text = "abaabaaabaaaaabbbaaaaaabaaaaababaabaaabaaaaabbbaaaaaabaaaaab";
        String pattern = "abaabaaabaaaabbbaaaaabaaaaab";
        testWindowAutomaton(text, pattern, 30);
    }

    @Test
    public void testWAB1() {
        String text = "abaabaaabaaaaaabbbaaaaabaaaaabababbb";
        String pattern = "abaabaaabaaaaaabaaaaabaaaaabababbb";
        testWindowAutomatonBit(text, pattern, 36);
    }

    @Test
    public void testWAB2() {
        String text = "abaabaaabaaaaaabbbaaaaabaaaaabababbbabaabaaabaaaaaabbbaaaaabaaaaabababbb";
        String pattern = "abaabaaabaaaaaabaaaaabaaaaabababbb";
        testWindowAutomatonBit(text, pattern, 36);
    }

    @Test
    public void testTP1() {
        String text = "abaabaaabaaaabaaaaaaabaaaaabbb";
        String pattern = "abaabaaabaaaabaaaaabaaaaabbb";
        testTextPreprocessing(text, pattern, 30);
    }

    @Test
    public void testTP2() {
        String text = "abaabaaabaaaabaaaaaaabaaaaabbbabaabaaabaaaabaaaaaaabaaaaabbb";
        String pattern = "abaabaaabaaaabaaaaabaaaaabbb";
        testTextPreprocessing(text, pattern, 30);
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
