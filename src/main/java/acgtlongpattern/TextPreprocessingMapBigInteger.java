package acgtlongpattern;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Preprocessing the text.
 * The dictionary is implemented as a map of BigIntegers to integers.
 *
 * @author Zdenek Tronicek
 */
public class TextPreprocessingMapBigInteger {

    private static final int ALPH = 4;
    private static final int BITS = log2(ALPH);

    private static int log2(int n) {
        int c = 0, m = 1;
        while (m < n) {
            m <<= 1;
            c++;
        }
        return c;
    }

    public static Map<BigInteger, Integer> preprocess(String text, int window) {
        BigInteger wmask = BigInteger.ONE
                .shiftLeft(window * BITS)
                .subtract(BigInteger.ONE);
        BigInteger state = BigInteger.ZERO;
        for (int i = 0; i < window; i++) {
            int j = charToInt(text.charAt(i));
            state = state.shiftLeft(BITS)
                    .or(BigInteger.valueOf(j));
        }
        Map<BigInteger, Integer> freqMap = new HashMap<>();
        freqMap.put(state, 1);
        for (int i = window; i < text.length(); i++) {
            int j = charToInt(text.charAt(i));
            state = state.shiftLeft(BITS)
                    .and(wmask)
                    .or(BigInteger.valueOf(j));
            Integer freq = freqMap.get(state);
            if (freq == null) {
                freqMap.put(state, 1);
            } else {
                freqMap.put(state, freq + 1);
            }
        }
        return freqMap;
    }

    public static int search(Map<BigInteger, Integer> freqMap, String pattern, int window) {
        int plen = pattern.length();
        int[] pat = new int[plen];
        for (int i = 0; i < pat.length; i++) {
            pat[i] = charToInt(pattern.charAt(i));
        }
        int[][] st = new int[(plen + 1) * (window - plen + 1)][ALPH];
        for (int i = 0; i < window - plen; i++) {
            for (int j = 0; j < plen; j++) {
                int k = i * (plen + 1) + j;
                for (int a = 0; a < ALPH; a++) {
                    st[k][a] = k + plen + 1;
                }
                st[k][pat[j]] = k + 1;
            }
            int c = i * (plen + 1) + plen;
            for (int a = 0; a < ALPH; a++) {
                st[c][a] = c + plen + 1;
            }
        }
        for (int j = 0; j < plen; j++) {
            int k = (window - plen) * (plen + 1) + j;
            st[k][pat[j]] = k + 1;
        }
        int count = 0;
        int[] stack = new int[window];
        int[] letter = new int[window];
        int cur, symb, ind = 0;
        BigInteger path = BigInteger.ZERO;
        stack[0] = 0;
        letter[0] = 0;
        while (ind >= 0) {
            cur = stack[ind];
            symb = letter[ind];
            if (symb >= ALPH) {
                path = path.shiftRight(BITS);
                ind--;
            } else if (st[cur][symb] == 0) {
                letter[ind] = symb + 1;
            } else if (st[cur][symb] > 0) {
                path = path.shiftLeft(BITS)
                        .or(BigInteger.valueOf(symb));
                letter[ind] = symb + 1;
                ind++;
                if (ind == window) {
                    Integer freq = freqMap.get(path);
                    if (freq != null) {
                        count += freq;
                    }
                    path = path.shiftRight(BITS);
                    ind--;
                } else {
                    stack[ind] = st[cur][symb];
                    letter[ind] = 0;
                }
            }
        }
        return count;
    }

    private static int charToInt(char c) {
        return switch (c) {
            case 'a' -> 0;
            case 'c' -> 1;
            case 'g' -> 2;
            case 't' -> 3;
            default -> throw new AssertionError();
        };
    }

}
