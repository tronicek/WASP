package binary;

/**
 * Preprocessing the text.
 * The dictionary is implemented in an array.
 *
 * @author Zdenek Tronicek
 */
public class TextPreprocessing {

    private static final int ALPH = 2;

    public static int[] preprocess(String text, int window) {
        if (window > 30) {
            System.out.println("TextPreprocessing: window too large");
            return null;
        }
        int wins = (1 << window);
        int wmask = wins - 1;
        int[] freq = new int[wins];
        int state = 0;
        for (int i = 0; i < window; i++) {
            int j = text.charAt(i) - 'a';
            state = (state << 1) | j;
        }
        freq[state]++;
        for (int i = window; i < text.length(); i++) {
            int j = text.charAt(i) - 'a';
            state = (state << 1) & wmask | j;
            freq[state]++;
        }
        return freq;
    }

    public static int search(int[] freq, String pattern, int window) {
        int plen = pattern.length();
        int[] pat = new int[plen];
        for (int i = 0; i < pat.length; i++) {
            pat[i] = pattern.charAt(i) - 'a';
        }
        int[][] st = new int[(plen + 1) * (window - plen + 1)][ALPH];
        for (int i = 0; i < window - plen; i++) {
            for (int j = 0; j < plen; j++) {
                int k = i * (plen + 1) + j;
                for (int a = 0; a < ALPH; a++) {
                    st[k][a] = k + plen + 1;
                }
                st[k][pat[j]] = k + 1;
            }
            int c = i * (plen + 1) + plen;
            for (int a = 0; a < ALPH; a++) {
                st[c][a] = c + plen + 1;
            }
        }
        for (int j = 0; j < plen; j++) {
            int k = (window - plen) * (plen + 1) + j;
            st[k][pat[j]] = k + 1;
        }
        int[] stack = new int[window];
        int[] letter = new int[window];
        int cur, symb, ind = 0, path = 0;
        int count = 0;
        stack[0] = 0;
        letter[0] = 0;
        while (ind >= 0) {
            cur = stack[ind];
            symb = letter[ind];
            if (symb >= ALPH) {
                path >>>= 1;
                ind--;
            } else if (st[cur][symb] == 0) {
                letter[ind] = symb + 1;
            } else if (st[cur][symb] > 0) {
                path = (path << 1) | symb;
                letter[ind] = symb + 1;
                ind++;
                if (ind == window) {
                    count += freq[path];
                    path >>>= 1;
                    ind--;
                } else {
                    stack[ind] = st[cur][symb];
                    letter[ind] = 0;
                }
            }
        }
        return count;
    }

}
