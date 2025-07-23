package acgt;

/**
 * Shift-And algorithm.
 * The NFA is encoded row by row. The state vector is reduced.
 *
 * @author Zdenek Tronicek
 */
public class ShiftAndRowReduced {

    private static final int ALPH = 4;

    public static int search(String text, String pattern, int window) {
        int plen = pattern.length();
        int errs = window - plen;
        if ((errs + 1) * plen > 64) {
            System.out.println("ShiftAndRowReduced: window/pattern too large");
            return -1;
        }
        int[] pat = new int[plen];
        for (int i = 0; i < pat.length; i++) {
            pat[i] = charToInt(pattern.charAt(i));
        }
        long k = 1L;
        for (int i = 0; i < errs; i++) {
            k = (k << plen) | 1L;
        }
        long ones = k;
        long[] b = new long[ALPH];
        for (int i = 0; i < plen; i++) {
            b[pat[i]] |= k;
            k <<= 1;
        }
        long f = (1L << (plen * (errs + 1) - 1));
        long v = 0L;
        int count = 0;
        for (int i = 0; i < window - 1; i++) {
            int c = charToInt(text.charAt(i));
            v = (((v << 1) | ones) & b[c]) | (v << plen);
        }
        for (int i = window - 1; i < text.length(); i++) {
            int c = charToInt(text.charAt(i));
            v = (((v << 1) | ones) & b[c]) | (v << plen);
            if ((v & f) != 0) {
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
