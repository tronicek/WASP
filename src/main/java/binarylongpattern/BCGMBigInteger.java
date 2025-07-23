package binarylongpattern;

import java.math.BigInteger;

/**
 * BCGM algorithm for long patterns.
 * The bit vector is stored in BigInteger.
 *
 * @author Zdenek Tronicek
 */
public class BCGMBigInteger {

    private static final int ALPH = 2;

    public static int search(String text, String pattern, int window) {
        int plen = pattern.length();
        int omega = log2(window + 2);
        int omega1 = omega + 1;
        BigInteger i0 = BigInteger.ZERO;
        BigInteger i = BigInteger.ONE
                .shiftLeft(omega)
                .subtract(BigInteger.ONE);
        for (int j = 0; j < plen; j++) {
            i0 = i0.shiftLeft(omega1)
                    .or(i);
        }
        BigInteger f = i.shiftLeft((plen - 1) * omega1);
        int[] pat = new int[plen];
        for (int j = 0; j < pat.length; j++) {
            pat[j] = pattern.charAt(j) - 'a';
        }
        BigInteger[] m = new BigInteger[ALPH];
        for (int j = 0; j < ALPH; j++) {
            m[j] = BigInteger.ZERO;
        }
        BigInteger k = i;
        for (int j = 0; j < plen; j++) {
            m[pat[j]] = m[pat[j]].or(k);
            k = k.shiftLeft(omega1);
        }
        BigInteger[] n = new BigInteger[ALPH];
        for (int j = 0; j < ALPH; j++) {
            n[j] = BigInteger.ZERO;
        }
        BigInteger p = i;
        for (int j = 0; j < pat.length; j++) {
            for (int r = 0; r < n.length; r++) {
                if (r == pat[j]) {
                    continue;
                }
                n[r] = n[r].or(p);
            }
            p = p.shiftLeft(omega1);
        }
        BigInteger e1 = BigInteger.ZERO;
        BigInteger e2 = BigInteger.ZERO;
        for (int j = 0; j < plen; j++) {
            e1 = e1.shiftLeft(omega1)
                    .or(BigInteger.ONE);
            e2 = e2.shiftLeft(1)
                    .or(BigInteger.ONE)
                    .shiftLeft(omega);
        }
        BigInteger L = i0;
        for (int j = 0; j < window - 1; j++) {
            int c = text.charAt(j) - 'a';
            BigInteger t = L.shiftLeft(omega1)
                    .and(m[c])
                    .add(L.and(n[c]))
                    .add(e1);
            L = t.subtract(t.and(e2).shiftRight(omega));
        }
        int count = 0;
        BigInteger win = BigInteger.valueOf(window);
        for (int j = window - 1; j < text.length(); j++) {
            int c = text.charAt(j) - 'a';
            BigInteger t = L.shiftLeft(omega1)
                    .and(m[c])
                    .add(L.and(n[c]))
                    .add(e1);
            L = t.subtract(t.and(e2).shiftRight(omega));
            if (L.compareTo(f) < 0) {
                BigInteger w = L.shiftRight(omega1 * (plen - 1));
                if (w.compareTo(win) <= 0) {
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

}
