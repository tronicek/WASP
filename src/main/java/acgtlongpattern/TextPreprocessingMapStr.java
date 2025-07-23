package acgtlongpattern;

import common.Str;
import java.util.HashMap;
import java.util.Map;

/**
 * Preprocessing the text for large windows.
 * The dictionary is implemented as a map of Str to integers.
 *
 * @author Zdenek Tronicek
 */
public class TextPreprocessingMapStr {

    private static final int ALPH = 4;

    public static Map<Str, Integer> preprocess(String text, int window) {
        char[] characters = text.toCharArray();
        Map<Str, Integer> freqMap = new HashMap<>(text.length() * 2);
        Str t = new Str(characters, 0, window);
        freqMap.put(t, 1);
        int hash = t.hashCode();
        int pow = 1;
        for (int i = 1; i < window; i++) {
            pow = (pow << 5) - pow;
        }
        for (int i = 1; i <= text.length() - window; i++) {
            hash -= text.charAt(i - 1) * pow;
            hash = (hash << 5) - hash + text.charAt(i + window - 1);
            Str s = new Str(characters, i, i + window, hash);
            Integer freq = freqMap.get(s);
            if (freq == null) {
                freqMap.put(s, 1);
            } else {
                freqMap.put(s, freq + 1);
            }
        }
        return freqMap;
    }

    public static int search(Map<Str, Integer> freqMap, String pattern, int window) {
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
        stack[0] = 0;
        letter[0] = 0;
        while (ind >= 0) {
            cur = stack[ind];
            symb = letter[ind];
            if (symb >= ALPH) {
                ind--;
            } else if (st[cur][symb] == 0) {
                letter[ind] = symb + 1;
            } else if (st[cur][symb] > 0) {
                letter[ind] = symb + 1;
                ind++;
                if (ind == window) {
                    char[] chars = new char[ind];
                    for (int i = 0; i < ind; i++) {
                        char c = intToChar(letter[i] - 1);
                        chars[i] = c;
                    }
                    Integer freq = freqMap.get(new Str(chars, 0, ind));
                    if (freq != null) {
                        count += freq;
                    }
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

    private static char intToChar(int i) {
        return switch (i) {
            case 0 -> 'a';
            case 1 -> 'c';
            case 2 -> 'g';
            case 3 -> 't';
            default -> throw new AssertionError();
        };
    }

}
