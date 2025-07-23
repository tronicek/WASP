package acgt;

import trie.Trie;

/**
 * Preprocessing the text.
 * The dictionary is implemented as a trie.
 *
 * @author Zdenek Tronicek
 */
public class TextPreprocessingTrie {

    private static final int ALPH = 4;

    public static Trie preprocess(String text, int window) {
        String str = text.replace('c', 'b')
                .replace('g', 'c')
                .replace('t', 'd');
        Trie trie = new Trie(ALPH);
        for (int i = 0; i <= str.length() - window; i++) {
            String s = str.substring(i, i + window);
            trie.add(s);
        }
        return trie;
    }

    public static int search(Trie trie, String pattern, int window) {
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
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < ind; i++) {
                        char c = (char) ('a' + letter[i] - 1);
                        sb.append(c);
                    }
                    count += trie.freq(sb.toString());
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
