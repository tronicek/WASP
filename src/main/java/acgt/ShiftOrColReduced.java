package acgt;

/**
 * Shift-Or algorithm.
 * The NFA is encoded column by column. The state vector is reduced.
 *
 * @author Zdenek Tronicek
 */
public class ShiftOrColReduced {

    private static final int ALPH = 4;

    public static int search(String text, String pattern, int window) {
        int plen = pattern.length();
        int errs = window - plen;
        int shift = errs + 1;
        if (shift * plen > 64) {
            System.out.println("ShiftOrColReduced: window/pattern too large");
            return -1;
        }
        int[] pat = new int[plen];
        for (int i = 0; i < pat.length; i++) {
            pat[i] = charToInt(pattern.charAt(i));
        }
        long[] b = new long[ALPH];
        long k = (1L << shift) - 1;
        for (int i = 0; i < plen; i++) {
            b[pat[i]] |= k;
            k <<= shift;
        }
        for (int i = 0; i < ALPH; i++) {
            b[i] = ~b[i];
        }
        long of = (1L << errs);
        for (int i = 1; i < plen; i++) {
            of |= (of << shift);
        }
        long f = (1L << (shift * plen - 1));
        long v = -1L;
        for (int i = 0; i < window - 1; i++) {
            int c = charToInt(text.charAt(i));
            v = ((v << shift) | b[c]) & (((v | of) << 1) | 1L);
        }
        int count = 0;
        for (int i = window - 1; i < text.length(); i++) {
            int c = charToInt(text.charAt(i));
            v = ((v << shift) | b[c]) & (((v | of) << 1) | 1L);
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
