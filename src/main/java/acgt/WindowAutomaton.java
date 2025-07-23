package acgt;

/**
 * Window automaton.
 * The set of final states is implemented as an array of booleans.
 *
 * @author Zdenek Tronicek
 */
public class WindowAutomaton {

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

    public static int search(String text, String pattern, int window) {
        if (window * BITS > 30) {
            System.out.println("WindowAutomaton: window too large");
            return -1;
        }
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
        int wins = (1 << (window * BITS));
        boolean[] fin = new boolean[wins];
        int[] stack = new int[window];
        int[] letter = new int[window];
        int cur, symb, ind = 0, path = 0;
        stack[0] = 0;
        letter[0] = 0;
        while (ind >= 0) {
            cur = stack[ind];
            symb = letter[ind];
            if (symb >= ALPH) {
                path >>>= BITS;
                ind--;
            } else if (st[cur][symb] == 0) {
                letter[ind] = symb + 1;
            } else if (st[cur][symb] > 0) {
                path = (path << BITS) | symb;
                letter[ind] = symb + 1;
                ind++;
                if (ind == window) {
                    fin[path] = true;
                    path >>>= BITS;
                    ind--;
                } else {
                    stack[ind] = st[cur][symb];
                    letter[ind] = 0;
                }
            }
        }
        int wmask = wins - 1;
        int count = 0;
        int state = 0;
        for (int i = 0; i < window; i++) {
            int j = charToInt(text.charAt(i));
            state = (state << BITS) | j;
        }
        if (fin[state]) {
            count++;
        }
        for (int i = window; i < text.length(); i++) {
            int j = charToInt(text.charAt(i));
            state = (state << BITS) & wmask | j;
            if (fin[state]) {
                count++;
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
