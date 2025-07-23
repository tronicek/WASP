package english;

/**
 * Shift-Or algorithm.
 * The NFA is encoded column by column.
 * 
 * @author Zdenek Tronicek
 */
public class ShiftOrColFull {

    private static final int ALPH = 'z' - 'a' + 1;

    public static int search(String text, String pattern, int window) {
        int plen = pattern.length();
        int errs = window - plen;
        int shift = errs + 1;
        if (shift * (plen + 1) > 64) {
            System.out.println("ShiftOrColFull: window/pattern too large");
            return -1;
        }
        int[] pat = new int[plen];
        for (int i = 0; i < pat.length; i++) {
            pat[i] = pattern.charAt(i) - 'a';
        }
        long[] b = new long[ALPH];
        long k = (1L << shift) - 1;
        for (int i = 0; i < plen; i++) {
            k <<= shift;
            b[pat[i]] |= k;
        }
        for (int i = 0; i < ALPH; i++) {
            b[i] = ~(b[i] | 1L);
        }
        long of = (1L << errs);
        for (int i = 0; i < plen; i++) {
            of |= (of << shift);
        }
        long f = (1L << (shift * plen + errs));
        long v = -2L;
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            int c = text.charAt(i) - 'a';
            v = ((v << shift) | b[c]) & ((v | of) << 1);
            if ((v & f) == 0) {
                count++;
            }
        }
        return count;
    }

}
