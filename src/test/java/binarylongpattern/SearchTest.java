package binarylongpattern;

import common.Str;
import java.math.BigInteger;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for selected texts and patterns.
 *
 * @author Zdenek Tronicek
 */
public class SearchTest {

    private void test(String text, String pattern, int window, int expected) {
        testNaive(text, pattern, window, expected);
        testBCGMBigInteger(text, pattern, window, expected);
        testShiftAndBigInteger(text, pattern, window, expected);
        testShiftAndMultipleWords(text, pattern, window, expected);
        testShiftAndRows(text, pattern, window, expected);
        testWindowAutomatonBigInteger(text, pattern, window, expected);
        testTextPreprocessingMapBigInteger(text, pattern, window, expected);
        testTextPreprocessingMapInt(text, pattern, window, expected);
        testTextPreprocessingMapStr(text, pattern, window, expected);
    }

    private void testNaive(String text, String pattern, int window, int expected) {
        int result = Naive.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testBCGMBigInteger(String text, String pattern, int window, int expected) {
        int result = BCGMBigInteger.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftAndBigInteger(String text, String pattern, int window, int expected) {
        int result = ShiftAndBigInteger.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftAndMultipleWords(String text, String pattern, int window, int expected) {
        int result = ShiftAndMultipleWords.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testShiftAndRows(String text, String pattern, int window, int expected) {
        int result = ShiftAndRows.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testWindowAutomatonBigInteger(String text, String pattern, int window, int expected) {
        int result = WindowAutomatonBigInteger.search(text, pattern, window);
        assertEquals(expected, result);
    }

    private void testTextPreprocessingMapBigInteger(String text, String pattern, int window, int expected) {
        Map<BigInteger, Integer> map = TextPreprocessingMapBigInteger.preprocess(text, window);
        int result = TextPreprocessingMapBigInteger.search(map, pattern, window);
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

    @Test
    public void test1() {
        test("aaaaa", "b", 1, 0);
    }

    @Test
    public void test2() {
        test("aaaaa", "a", 1, 5);
    }

    @Test
    public void test3() {
        test("ababab", "ab", 2, 3);
    }

    @Test
    public void test4() {
        test("bbaaa", "ab", 3, 0);
    }

    @Test
    public void test5() {
        test("aaabb", "ab", 3, 2);
    }

    @Test
    public void test6() {
        test("aaaab", "ab", 5, 1);
    }

    @Test
    public void test7() {
        test("abaab", "ab", 3, 2);
    }

    @Test
    public void test8() {
        test("aaaabb", "ab", 5, 2);
    }

    @Test
    public void test9() {
        test("abaaba", "aaa", 4, 2);
    }

    @Test
    public void test10() {
        test("ababab", "aa", 4, 3);
    }

    @Test
    public void test11() {
        test("abababab", "abab", 5, 4);
    }

    @Test
    public void test12() {
        test("abababababab", "aabb", 6, 4);
    }

    @Test
    public void test13() {
        test("baaaaaaaaaaaaaaaaaaaab", "baaaaaaaaaaaaaaaaaab", 22, 1);
    }

    @Test
    public void test14() {
        test("baaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab",
                "baaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab", 50, 1);
    }

    @Test
    public void test15() {
        test("baabaabaabaabaabaabaabaabaabaabaabaabaabaabaabaab", "bb", 4, 16);
    }

}
