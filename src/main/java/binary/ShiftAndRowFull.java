package binary;

/**
 * Shift-And algorithm.
 * The NFA is encoded row by row.
 * 
 * @author Zdenek Tronicek
 */
public class ShiftAndRowFull {
    
    private static final int ALPH = 2;

    public static int search(String text, String pattern, int window) {
        int plen = pattern.length();
        int errs = window - plen;
        int shift = plen + 1;
        if ((errs + 1) * shift > 64) {
            System.out.println("ShiftAndRowFull: window/pattern too large");
            return -1;
        }
        int[] pat = new int[plen];
        for (int i = 0; i < pat.length; i++) {
            pat[i] = pattern.charAt(i) - 'a';
        }
        long k = 1L;
        for (int i = 0; i < errs; i++) {
            k = (k << shift) | 1L;
        }
        long[] b = new long[ALPH];
        for (int i = 0; i < plen; i++) {
            k <<= 1;
            b[pat[i]] |= k;
        }
        long f = (1L << (shift * errs + plen));
        long v = 1L;
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            int c = text.charAt(i) - 'a';
            v = ((v << 1) & b[c]) | (v << shift) | 1L;
            if ((v & f) != 0) {
                count++;
            }
        }
        return count;
    }

}
