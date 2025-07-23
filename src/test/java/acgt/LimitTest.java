package acgt;

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
        String text = "accccccccccccccccccccccccccgggggggggggggggggggggggggggggggaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaatttttttttttttttttttttttttgg";
        String pattern = "accgatgg";
        testBCGM(text, pattern, 126);
    }

    @Test
    public void testBCGM2() {
        String text = "accccccccccccccccccccccccccgggggggggggggggggggggggggggggggaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaatttttttttttttttttttttttttgg"
                + "accccccccccccccccccccccccccgggggggggggggggggggggggggggggggaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaatttttttttttttttttttttttttgg";
        String pattern = "accgatgg";
        testBCGM(text, pattern, 126);
    }

    @Test
    public void testSACF1() {
        String text = "acacaagaagagat";
        String pattern = "accgggt";
        testShiftAndColFull(text, pattern, 14);
    }

    @Test
    public void testSACF2() {
        String text = "acacaagaagagatacacaagaagagat";
        String pattern = "accgggt";
        testShiftAndColFull(text, pattern, 14);
    }

    @Test
    public void testSACR1() {
        String text = "acacaagaagagatt";
        String pattern = "accgggtt";
        testShiftAndColReduced(text, pattern, 15);
    }

    @Test
    public void testSACR2() {
        String text = "acacaagaagagattacacaagaagagatt";
        String pattern = "accgggtt";
        testShiftAndColReduced(text, pattern, 15);
    }

    @Test
    public void testSARF1() {
        String text = "acacaagaagagat";
        String pattern = "accgggt";
        testShiftAndRowFull(text, pattern, 14);
    }

    @Test
    public void testSARF2() {
        String text = "acacaagaagagatacacaagaagagat";
        String pattern = "accgggt";
        testShiftAndRowFull(text, pattern, 14);
    }

    @Test
    public void testSARR1() {
        String text = "acacaagaagagatt";
        String pattern = "accgggtt";
        testShiftAndRowReduced(text, pattern, 15);
    }

    @Test
    public void testSARR2() {
        String text = "acacaagaagagattacacaagaagagatt";
        String pattern = "accgggtt";
        testShiftAndRowReduced(text, pattern, 15);
    }

    @Test
    public void testSOCF1() {
        String text = "acacaagaagagat";
        String pattern = "accgggt";
        testShiftOrColFull(text, pattern, 14);
    }

    @Test
    public void testSOCF2() {
        String text = "acacaagaagagatacacaagaagagat";
        String pattern = "accgggt";
        testShiftOrColFull(text, pattern, 14);
    }

    @Test
    public void testSOCR1() {
        String text = "acacaagaagagatt";
        String pattern = "accgggtt";
        testShiftOrColReduced(text, pattern, 15);
    }

    @Test
    public void testSOCR2() {
        String text = "acacaagaagagattacacaagaagagatt";
        String pattern = "accgggtt";
        testShiftOrColReduced(text, pattern, 15);
    }

    @Test
    public void testSORF1() {
        String text = "acacaagaagagat";
        String pattern = "accgggt";
        testShiftOrRowFull(text, pattern, 14);
    }

    @Test
    public void testSORF2() {
        String text = "acacaagaagagatacacaagaagagat";
        String pattern = "accgggt";
        testShiftOrRowFull(text, pattern, 14);
    }

    @Test
    public void testSORR1() {
        String text = "acacaagaagagatt";
        String pattern = "accgggtt";
        testShiftOrRowReduced(text, pattern, 15);
    }

    @Test
    public void testSORR2() {
        String text = "acacaagaagagattacacaagaagagatt";
        String pattern = "accgggtt";
        testShiftOrRowReduced(text, pattern, 15);
    }

    @Test
    public void testWA1() {
        String text = "accgtagatcgtaac";
        String pattern = "accgtagtcgtaac";
        testWindowAutomaton(text, pattern, 15);
    }

    @Test
    public void testWA2() {
        String text = "accgtagatcgtaacaccgtagatcgtaac";
        String pattern = "accgtagtcgtaac";
        testWindowAutomaton(text, pattern, 15);
    }

    @Test
    public void testWAB1() {
        String text = "acacagtagtacgtaaac";
        String pattern = "acacagtagtacgtaac";
        testWindowAutomatonBit(text, pattern, 18);
    }

    @Test
    public void testWAB2() {
        String text = "acacagtagtacgtaaacacacagtagtacgtaaac";
        String pattern = "acacagtagtacgtaac";
        testWindowAutomatonBit(text, pattern, 18);
    }

    @Test
    public void testTP1() {
        String text = "acccgtaaacgtaac";
        String pattern = "accgtacgtaac";
        testTextPreprocessing(text, pattern, 15);
    }

    @Test
    public void testTP2() {
        String text = "acccgtaaacgtaacacccgtaaacgtaac";
        String pattern = "accgtacgtaac";
        testTextPreprocessing(text, pattern, 15);
    }

    @Test
    public void testTPMI1() {
        String text = "accagtacgtaacaccgagggacatttgggg";
        String pattern = "accgtacgtaacccggggacatttgggg";
        testTextPreprocessingMapInt(text, pattern, 31);
    }

    @Test
    public void testTPMI2() {
        String text = "accagtacgtaacaccgagggacatttggggaccagtacgtaacaccgagggacatttgggg";
        String pattern = "accgtacgtaacccggggacatttgggg";
        testTextPreprocessingMapInt(text, pattern, 31);
    }

}
