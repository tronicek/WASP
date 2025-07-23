package acgtlongpattern;

/**
 * Shift-And Algorithm for long patterns.
 * The state vector is stored in multiple words.
 *
 * @author Zdenek Tronicek
 */
public class ShiftAndMultipleWords {

    private static final int ALPH = 4;

    public static int search(String text, String pattern, int window) {
        int plen = pattern.length();
        int errs = window - plen;
        int shift = plen + 1;
        int[] pat = new int[plen];
        for (int i = 0; i < pat.length; i++) {
            pat[i] = charToInt(pattern.charAt(i));
        }
        int bits = (plen + 1) * (errs + 1);
        int slots = (bits >>> 6) + 1;
        long[] k = new long[slots];
        k[0] = 1L;
        for (int i = 0; i < errs; i++) {
            leftShift(k, shift);
            k[0] |= 1L;
        }
        long[] ones = new long[slots];
        System.arraycopy(k, 0, ones, 0, k.length);
        long[][] b = new long[ALPH][slots];
        for (int i = 0; i < plen; i++) {
            leftShift(k, 1);
            or(b[pat[i]], k);
        }
        long f = (1L << (bits - 1));
        long[] v = new long[slots];
        v[0] = 1L;
        int count = 0;
        long[] v2 = new long[v.length];
        for (int i = 0; i < text.length(); i++) {
            int c = charToInt(text.charAt(i));
            System.arraycopy(v, 0, v2, 0, v.length);
            leftShift(v, 1);
            and(v, b[c]);
            leftShift(v2, shift);
            or(v, v2);
            or(v, ones);
            if ((v[v.length - 1] & f) != 0) {
                count++;
            }
        }
        return count;
    }

    private static void leftShift(long[] words, int shift) {
        int m = shift / 64;
        if (m > 0) {
            for (int i = words.length - 1; i >= m; i--) {
                words[i] = words[i - m];
            }
            for (int i = m - 1; i >= 0; i--) {
                words[i] = 0L;
            }
        }
        int n = shift % 64;
        if (n > 0) {
            for (int i = words.length - 1; i > 0; i--) {
                words[i] = (words[i] << n) | (words[i - 1] >>> (64 - n));
            }
            words[0] <<= n;
        }
    }

    private static void or(long[] words, long[] words2) {
        for (int i = 0; i < words.length; i++) {
            words[i] |= words2[i];
        }
    }

    private static void and(long[] words, long[] words2) {
        for (int i = 0; i < words.length; i++) {
            words[i] &= words2[i];
        }
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
