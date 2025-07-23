package binarylongpattern;

/**
 * Shift-And algorithm for large patterns.
 * Each row of NFA is stored in a separate word.
 *
 * @author Zdenek Tronicek
 */
public class ShiftAndRows {

    private static final int ALPH = 2;

    public static int search(String text, String pattern, int window) {
        int plen = pattern.length();
        if (plen > 64) {
            System.out.println("ShiftAndRows: pattern too long");
            return -1;
        }
        int errs = window - plen;
        int[] p = new int[plen];
        for (int i = 0; i < p.length; i++) {
            p[i] = pattern.charAt(i) - 'a';
        }
        long[] b = new long[ALPH];
        long k = 1L;
        for (int i = 0; i < plen; i++) {
            b[p[i]] |= k;
            k <<= 1;
        }
        long f = (1L << (plen - 1));
        long[] v = new long[errs + 1];
        int count = 0;
        for (int i = 0; i < window - 1; i++) {
            int c = text.charAt(i) - 'a';
            for (int j = errs; j > 0; j--) {
                v[j] = (((v[j] << 1) | 1L) & b[c]) | v[j - 1];
            }
            v[0] = ((v[0] << 1) | 1L) & b[c];
        }
        for (int i = window - 1; i < text.length(); i++) {
            int c = text.charAt(i) - 'a';
            for (int j = errs; j > 0; j--) {
                v[j] = (((v[j] << 1) | 1L) & b[c]) | v[j - 1];
            }
            v[0] = ((v[0] << 1) | 1L) & b[c];
            if ((v[errs] & f) != 0) {
                count++;
            }
        }
        return count;
    }

}
