package acgt;

import common.Str;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import trie.Trie;

/**
 * Unit tests for selected texts and patterns.
 *
 * @author Zdenek Tronicek
 */
public class SearchTest {

    private void test(String text, String pattern, int window, int expected) {
        testNaive(text, pattern, window, expected);
        testBCGM(text, pattern, window, expected);
        testShiftAndColFull(text, pattern, window, expected);
        testShiftAndColReduced(text, pattern, window, expected);
        testShiftAndRowFull(text, pattern, window, expected);
        testShiftAndRowReduced(text, pattern, window, expected);
        testShiftOrColFull(text, pattern, window, expected);
        testShiftOrColReduced(text, pattern, window, expected);
        testShiftOrRowFull(text, pattern, window, expected);
        testShiftOrRowReduced(text, pattern, window, expected);
        testWindowAutomaton(text, pattern, window, expected);
        testWindowAutomatonBit(text, pattern, window, expected);
        testTextPreprocessing(text, pattern, window, expected);
        testTextPreprocessingMapInt(text, pattern, window, expected);
        testTextPreprocessingMapStr(text, pattern, window, expected);
        testTextPreprocessingTrie(text, pattern, window, expected);
        testFullPreprocessing(text, pattern, window, expected);
    }

    private void testNaive(String text, String pattern, int window, int expected) {
        int result = Naive.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testBCGM(String text, String pattern, int window, int expected) {
        int result = BCGM.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftAndColFull(String text, String pattern, int window, int expected) {
        int result = ShiftAndColFull.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftAndColReduced(String text, String pattern, int window, int expected) {
        int result = ShiftAndColReduced.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftAndRowFull(String text, String pattern, int window, int expected) {
        int result = ShiftAndRowFull.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftAndRowReduced(String text, String pattern, int window, int expected) {
        int result = ShiftAndRowReduced.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftOrColFull(String text, String pattern, int window, int expected) {
        int result = ShiftOrColFull.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftOrColReduced(String text, String pattern, int window, int expected) {
        int result = ShiftOrColReduced.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftOrRowFull(String text, String pattern, int window, int expected) {
        int result = ShiftOrRowFull.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftOrRowReduced(String text, String pattern, int window, int expected) {
        int result = ShiftOrRowReduced.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testWindowAutomaton(String text, String pattern, int window, int expected) {
        int result = WindowAutomaton.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testWindowAutomatonBit(String text, String pattern, int window, int expected) {
        int result = WindowAutomatonBit.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testTextPreprocessing(String text, String pattern, int window, int expected) {
        int[] freq = TextPreprocessing.preprocess(text, window);
        int result = TextPreprocessing.search(freq, pattern, window);
        assertEquals(expected, result);
    }

    private void testTextPreprocessingMapInt(String text, String pattern, int window, int expected) {
        Map<Long, Integer> map = TextPreprocessingMapInt.preprocess(text, window);
        int result = TextPreprocessingMapInt.search(map, pattern, window);
        assertEquals(expected, result);
    }

    private void testTextPreprocessingMapStr(String text, String pattern, int window, int expected) {
        Map<Str, Integer> map = TextPreprocessingMapStr.preprocess(text, window);
        int result = TextPreprocessingMapStr.search(map, pattern, window);
        assertEquals(expected, result);
    }

    private void testTextPreprocessingTrie(String text, String pattern, int window, int expected) {
        Trie trie = TextPreprocessingTrie.preprocess(text, window);
        int result = TextPreprocessingTrie.search(trie, pattern, window);
        assertEquals(expected, result);
    }

    private void testFullPreprocessing(String text, String pattern, int window, int expected) {
        int[] wasp = FullPreprocessing.preprocess(text, pattern.length(), window);
        int result = FullPreprocessing.search(wasp, pattern);
        assertEquals(expected, result);
    }

    @Test
    public void test1() {
        test("acgt", "acgt", 4, 1);
    }

    @Test
    public void test2() {
        test("acgtacgtacgtacgt", "acgt", 4, 4);
    }

    @Test
    public void test3() {
        test("acgacgacgacgacgt", "acgt", 4, 1);
    }

    @Test
    public void test4() {
        test("acccccccgt", "acgt", 10, 1);
    }

    @Test
    public void test5() {
        test("accccccccgt", "acgt", 10, 0);
    }

}
