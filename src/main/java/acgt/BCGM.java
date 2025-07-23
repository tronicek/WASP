package acgt;

/**
 * BCGM algorithm for a DNA alphabet.
 *
 * @author Zdenek Tronicek
 */
public class BCGM {

    private static final int ALPH = 4;

    public static int search(String text, String pattern, int window) {
        int plen = pattern.length();
        int omega = log2(window + 2);
        int omega1 = omega + 1;
        if (omega1 * plen > 64) {
            System.out.println("BCGM: window/pattern too large");
            return -1;
        }
        long i0 = 0L;
        long i = (1L << omega) - 1;
        for (int j = 0; j < plen; j++) {
            i0 = (i0 << omega1) | i;
        }
        long f = i << ((plen - 1) * omega1);
        int[] pat = new int[plen];
        for (int j = 0; j < pat.length; j++) {
            pat[j] = charToInt(pattern.charAt(j));
        }
        long[] m = new long[ALPH];
        long k = i;
        for (int j = 0; j < plen; j++) {
            m[pat[j]] |= k;
            k <<= omega1;
        }
        long[] n = new long[ALPH];
        long p = i;
        for (int j = 0; j < pat.length; j++) {
            for (int r = 0; r < n.length; r++) {
                if (r == pat[j]) {
                    continue;
                }
                n[r] |= p;
            }
            p <<= omega1;
        }
        long e1 = 0L, e2 = 0L;
        for (int j = 0; j < plen; j++) {
            e1 = (e1 << omega1) | 1L;
            e2 = ((e2 << 1) | 1L) << omega;
        }
        long L = i0;
        for (int j = 0; j < window - 1; j++) {
            int c = charToInt(text.charAt(j));
            long t = ((L << omega1) & m[c]) + (L & n[c]) + e1;
            L = t - ((t & e2) >>> omega);
        }
        int count = 0;
        for (int j = window - 1; j < text.length(); j++) {
            int c = charToInt(text.charAt(j));
            long t = ((L << omega1) & m[c]) + (L & n[c]) + e1;
            L = t - ((t & e2) >>> omega);
            if (L < f) {
                long w = (L >>> (omega1 * (plen - 1)));
                if (w <= window) {
                    count++;
                }
            }
        }
        return count;
    }

    private static int log2(int n) {
        int c = 0, m = 1;
        while (m < n) {
            m <<= 1;
            c++;
        }
        return c;
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
