package acgtlongpattern;

/**
 * Shift-And algorithm for large patterns.
 * Each row of NFA is stored in a separate word.
 *
 * @author Zdenek Tronicek
 */
public class ShiftAndRows {

    private static final int ALPH = 4;

    public static int search(String text, String pattern, int window) {
        int plen = pattern.length();
        if (plen > 64) {
            System.out.println("ShiftAndRows: pattern too long");
            return -1;
        }
        int errs = window - plen;
        int[] p = new int[plen];
        for (int i = 0; i < p.length; i++) {
            p[i] = charToInt(pattern.charAt(i));
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
            int c = charToInt(text.charAt(i));
            for (int j = errs; j > 0; j--) {
                v[j] = (((v[j] << 1) | 1L) & b[c]) | v[j - 1];
            }
            v[0] = ((v[0] << 1) | 1L) & b[c];
        }
        for (int i = window - 1; i < text.length(); i++) {
            int c = charToInt(text.charAt(i));
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
