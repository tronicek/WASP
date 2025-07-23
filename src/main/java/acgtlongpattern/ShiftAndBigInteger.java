package acgtlongpattern;

import java.math.BigInteger;

/**
 * Shift-And algorithm for long patterns.
 * The bit vector is stored in BigInteger.
 * 
 * @author Zdenek Tronicek
 */
public class ShiftAndBigInteger {
    
    private static final int ALPH = 4;

    public static int search(String text, String pattern, int window) {
        int plen = pattern.length();
        int errs = window - plen;
        int shift = plen + 1;
        int[] pat = new int[plen];
        for (int i = 0; i < pat.length; i++) {
            pat[i] = charToInt(pattern.charAt(i));
        }
        BigInteger k = BigInteger.ONE;
        for (int i = 0; i < errs; i++) {
            k = k.shiftLeft(shift)
                    .or(BigInteger.ONE);
        }
        BigInteger[] b = new BigInteger[ALPH];
        for (int i = 0; i < ALPH; i++) {
            b[i] = BigInteger.ZERO;
        }
        for (int i = 0; i < plen; i++) {
            k = k.shiftLeft(1);
            b[pat[i]] = b[pat[i]].or(k);
        }
        BigInteger f = BigInteger.ONE
                .shiftLeft(shift * errs + plen);
        BigInteger wmask = f.shiftLeft(1)
                .subtract(BigInteger.ONE);
        BigInteger v = BigInteger.ONE;
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            int c = charToInt(text.charAt(i));
            v = v.shiftLeft(1).and(b[c])
                    .or(v.shiftLeft(shift))
                    .or(BigInteger.ONE)
                    .and(wmask);
            if (!v.and(f).equals(BigInteger.ZERO)) {
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
