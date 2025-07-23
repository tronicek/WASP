package binary;

/**
 * Naive algorithm.
 *
 * @author Zdenek Tronicek
 */
public class Naive {

    public static int search(String text, String pattern, int window) {
        int count = 0;
        int tlen = text.length();
        int plen = pattern.length();
        for (int i = 0; i <= tlen - window; i++) {
            int t = 0, p = 0;
            while (t < window && p < plen) {
                if (text.charAt(i + t) == pattern.charAt(p)) {
                    p++;
                }
                t++;
            }
            if (p == plen) {
                count++;
            }
        }
        return count;
    }

}
