package binary;

import common.Str;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import trie.Trie;

/**
 * Unit tests for all possible strings of specified lengths.
 *
 * @author Zdenek Tronicek
 */
public class ExhaustiveTest {

    private void test(int tlen, int plen) {
        for (String text : generate(tlen)) {
            for (String pat : generate(plen)) {
                for (int win = plen; win <= tlen; win++) {
                    int naive = Naive.search(text, pat, win);
                    int bcgm = BCGM.search(text, pat, win);
                    int sacf = ShiftAndColFull.search(text, pat, win);
                    int sacr = ShiftAndColReduced.search(text, pat, win);
                    int sarf = ShiftAndRowFull.search(text, pat, win);
                    int sarr = ShiftAndRowReduced.search(text, pat, win);
                    int socf = ShiftOrColFull.search(text, pat, win);
                    int socr = ShiftOrColReduced.search(text, pat, win);
                    int sorf = ShiftOrRowFull.search(text, pat, win);
                    int sorr = ShiftOrRowReduced.search(text, pat, win);
                    int wa = WindowAutomaton.search(text, pat, win);
                    int wab = WindowAutomatonBit.search(text, pat, win);
                    int[] freq = TextPreprocessing.preprocess(text, win);
                    int tp = TextPreprocessing.search(freq, pat, win);
                    Map<Long, Integer> freqMapi = TextPreprocessingMapInt.preprocess(text, win);
                    int tpmi = TextPreprocessingMapInt.search(freqMapi, pat, win);
                    Map<Str, Integer> freqMaps = TextPreprocessingMapStr.preprocess(text, win);
                    int tpms = TextPreprocessingMapStr.search(freqMaps, pat, win);
                    Trie trie = TextPreprocessingTrie.preprocess(text, win);
                    int tpt = TextPreprocessingTrie.search(trie, pat, win);
                    int[] wasp = FullPreprocessing.preprocess(text, plen, win);
                    int fp = FullPreprocessing.search(wasp, pat);
                    assertEquals(naive, bcgm);
                    assertEquals(naive, sacf);
                    assertEquals(naive, sacr);
                    assertEquals(naive, sarf);
                    assertEquals(naive, sarr);
                    assertEquals(naive, socf);
                    assertEquals(naive, socr);
                    assertEquals(naive, sorf);
                    assertEquals(naive, sorr);
                    assertEquals(naive, wa);
                    assertEquals(naive, wab);
                    assertEquals(naive, tp);
                    assertEquals(naive, tpmi);
                    assertEquals(naive, tpms);
                    assertEquals(naive, tpt);
                    assertEquals(naive, fp);
                }
            }
        }
    }

    private Set<String> generate(int len) {
        Set<String> p = new TreeSet<>();
        int m = (1 << len);
        for (int i = 0; i < m; i++) {
            String s = toStr(len, i);
            p.add(s);
        }
        return p;
    }

    private String toStr(int len, int val) {
        String s = Integer.toString(val, 2);
        for (int i = s.length(); i < len; i++) {
            s = '0' + s;
        }
        return s.replace('0', 'a').replace('1', 'b');
    }

    private void testAll(int tlen) {
        for (int plen = 1; plen <= tlen; plen++) {
            test(tlen, plen);
        }
    }
    
    @Test
    public void test1() {
        testAll(1);
    }

    @Test
    public void test2() {
        testAll(2);
    }

    @Test
    public void test3() {
        testAll(3);
    }

    @Test
    public void test4() {
        testAll(4);
    }

    @Test
    public void test5() {
        testAll(5);
    }

    @Test
    public void test6() {
        testAll(6);
    }

}
