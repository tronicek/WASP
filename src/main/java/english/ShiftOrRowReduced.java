package english;

/**
 * Shift-Or algorithm.
 * The NFA is encoded row by row. The state vector is reduced.
 *
 * @author Zdenek Tronicek
 */
public class ShiftOrRowReduced {

    static final int ALPH = 'z' - 'a' + 1;

    public static int search(String text, String pattern, int window) {
        int plen = pattern.length();
        int errs = window - plen;
        if (plen * (errs + 1) > 64) {
            System.out.println("ShiftOrRowReduced: window/pattern too large");
            return -1;
        }
        int[] pat = new int[plen];
        for (int i = 0; i < pat.length; i++) {
            pat[i] = pattern.charAt(i) - 'a';
        }
        long k = 1L;
        for (int i = 0; i < errs; i++) {
            k = (k << plen) | 1L;
        }
        long zeros = ~k;
        long ones = (1L << plen) - 1;
        long[] b = new long[ALPH];
        for (int i = 0; i < plen; i++) {
            b[pat[i]] |= k;
            k <<= 1;
        }
        for (int i = 0; i < ALPH; i++) {
            b[i] = ~b[i];
        }
        long f = (1L << (plen * (errs + 1) - 1));
        long v = ~0L;
        int count = 0;
        for (int i = 0; i < window - 1; i++) {
            int c = text.charAt(i) - 'a';
            v = (((v << 1) & zeros) | b[c]) & ((v << plen) | ones);
        }
        for (int i = window - 1; i < text.length(); i++) {
            int c = text.charAt(i) - 'a';
            v = (((v << 1) & zeros) | b[c]) & ((v << plen) | ones);
            if ((v & f) == 0) {
                count++;
            }
        }
        return count;
    }

}
