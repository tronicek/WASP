package english;

/**
 * Window automaton.
 * The set of final states is implemented as a bit array.
 *
 * @author Zdenek Tronicek
 */
public class WindowAutomatonBit {

    private static final int ALPH = 'z' - 'a' + 1;
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
        if (window * BITS > 36) {
            System.out.println("WindowAutomatonBit: window too large");
            return -1;
        }
        int plen = pattern.length();
        int[] pat = new int[plen];
        for (int i = 0; i < pat.length; i++) {
            pat[i] = pattern.charAt(i) - 'a';
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
        int p = (int) ((1L << (window * BITS)) >>> 6);
        long[] fin = new long[p + 1];
        long fmask = (1L << 6) - 1;
        int[] stack = new int[window];
        int[] letter = new int[window];
        int cur, symb, ind = 0;
        long path = 0;
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
                    int k = (int) (path >>> 6);
                    fin[k] |= (1L << (path & fmask));
                    path >>>= BITS;
                    ind--;
                } else {
                    stack[ind] = st[cur][symb];
                    letter[ind] = 0;
                }
            }
        }
        long wmask = (1L << (window * BITS)) - 1;
        long[] vec = new long[ALPH];
        long m = 1L;
        for (int i = 0; i < plen; i++) {
            vec[pat[i]] |= m;
            m <<= BITS;
        }
        int count = 0;
        long state = 0;
        for (int i = 0; i < window; i++) {
            int j = text.charAt(i) - 'a';
            state = (state << BITS) | j;
        }
        int k = (int) (state >>> 6);
        if ((fin[k] & (1L << (state & fmask))) != 0) {
            count++;
        }
        for (int i = window; i < text.length(); i++) {
            int j = text.charAt(i) - 'a';
            state = ((state << BITS) & wmask) | j;
            int k2 = (int) (state >>> 6);
            if ((fin[k2] & (1L << (state & fmask))) != 0) {
                count++;
            }
        }
        return count;
    }

}
