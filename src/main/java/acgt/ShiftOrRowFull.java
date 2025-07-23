package acgt;

/**
 * Shift-Or algorithm.
 * The NFA is encoded row by row.
 *
 * @author Zdenek Tronicek
 */
public class ShiftOrRowFull {

    private static final int ALPH = 4;

    public static int search(String text, String pattern, int window) {
        int plen = pattern.length();
        int errs = window - plen;
        int shift = plen + 1;
        if ((errs + 1) * shift > 64) {
            System.out.println("ShiftOrRowFull: window/pattern too large");
            return -1;
        }
        int[] pat = new int[plen];
        for (int i = 0; i < pat.length; i++) {
            pat[i] = charToInt(pattern.charAt(i));
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
        for (int i = 0; i < ALPH; i++) {
            b[i] = ~(b[i] | 1L);
        }
        long f = (1L << (shift * errs + plen));
        long v = -2L;
        int count = 0;
        long ones = (1 << shift) - 1;
        for (int i = 0; i < text.length(); i++) {
            int c = charToInt(text.charAt(i));
            v = ((v << 1) | b[c]) & ((v << shift) | ones);
            if ((v & f) == 0) {
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
