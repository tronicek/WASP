package acgt;

/**
 * Full preprocessing.
 *
 * @author Zdenek Tronicek
 */
public class FullPreprocessing {

    private static final int ALPH = 4;
    private static final int BITS = log2(ALPH);

    private static int log2(int n) {
        int c = 0, m = 1;
        while (m < n) {
            m <<= 1;
            c++;
        }
        return c;
    }

    public static int[] preprocess(String text, int plen, int window) {
        int[] freq = TextPreprocessing.preprocess(text, window);
        String[] pats = generatePatterns(plen);
        int[] wasp = new int[pats.length];
        for (int j = 0; j < pats.length; j++) {
            wasp[j] = TextPreprocessing.search(freq, pats[j], window);
        }
        return wasp;
    }

    private static String[] generatePatterns(int plen) {
        int n = (1 << (plen * 2));
        String[] pats = new String[n];
        for (int i = 0; i < pats.length; i++) {
            pats[i] = toString(i, plen);
        }
        return pats;
    }

    private static String toString(int m, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = switch (m & 0b11) {
                case 0 -> 'a';
                case 1 -> 'c';
                case 2 -> 'g';
                default -> 't';
            };
            sb.append(c);
            m >>>= 2;
        }
        return sb.reverse().toString();
    }

    public static int search(int[] wasp, String pattern) {
        int p = 0;
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            int j = charToInt(c);
            p = (p << BITS) | j;
        }
        return wasp[p];
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
