package binarylongpattern;

import java.math.BigInteger;
import java.util.Map;

/**
 * Full preprocessing.
 *
 * @author Zdenek Tronicek
 */
public class FullPreprocessing {

    public static int[] preprocess(String text, int plen, int window) {
        Map<BigInteger, Integer> freq = TextPreprocessingMapBigInteger.preprocess(text, window);
        String[] pats = generatePatterns(plen);
        int[] wasp = new int[pats.length];
        for (int j = 0; j < pats.length; j++) {
            wasp[j] = TextPreprocessingMapBigInteger.search(freq, pats[j], window);
        }
        return wasp;
    }

    private static String[] generatePatterns(int plen) {
        int n = (1 << plen);
        String[] pats = new String[n];
        for (int i = 0; i < pats.length; i++) {
            pats[i] = toString(i, plen);
        }
        return pats;
    }

    private static String toString(int m, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = ((m & 1) == 0) ? 'a' : 'b';
            sb.append(c);
            m >>>= 1;
        }
        return sb.reverse().toString();
    }

    public static int search(int[] wasp, String pattern) {
        int p = 0;
        for (int i = 0; i < pattern.length(); i++) {
            int j = pattern.charAt(i) - 'a';
            p = (p << 1) | j;
        }
        return wasp[p];
    }

}
