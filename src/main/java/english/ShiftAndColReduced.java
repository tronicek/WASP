package english;

/**
 * Shift-And algorithm.
 * The NFA is encoded column by column. The state vector is reduced.
 *
 * @author Zdenek Tronicek
 */
public class ShiftAndColReduced {

    private static final int ALPH = 'z' - 'a' + 1;

    public static int search(String text, String pattern, int window) {
        int plen = pattern.length();
        int errs = window - plen;
        int shift = errs + 1;
        if (shift * plen > 64) {
            System.out.println("ShiftAndColReduced: window/pattern too large");
            return -1;
        }
        int[] pat = new int[plen];
        for (int i = 0; i < pat.length; i++) {
            pat[i] = pattern.charAt(i) - 'a';
        }
        long[] b = new long[ALPH];
        long k = (1L << shift) - 1;
        long ones = k;
        for (int i = 0; i < plen; i++) {
            b[pat[i]] |= k;
            k <<= shift;
        }
        long of = (1L << errs) - 1;
        for (int i = 0; i < plen; i++) {
            of |= (of << shift);
        }
        long f = (1L << (shift * plen - 1));
        long v = 0L;
        for (int i = 0; i < window - 1; i++) {
            int c = text.charAt(i) - 'a';
            v = (((v << shift) | ones) & b[c]) | ((v & of) << 1);
        }
        int count = 0;
        for (int i = window - 1; i < text.length(); i++) {
            int c = text.charAt(i) - 'a';
            v = (((v << shift) | ones) & b[c]) | ((v & of) << 1);
            if ((v & f) != 0) {
                count++;
            }
        }
        return count;
    }

}
