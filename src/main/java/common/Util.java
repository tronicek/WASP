package common;

/**
 * Auxiliary methods.
 *
 * @author Zdenek Tronicek
 */
public class Util {

    public static String toBinary(long v, int len) {
        String s = Long.toBinaryString(v);
        for (int i = s.length(); i < len; i++) {
            s = "0" + s;
        }
        return s;
    }

    public static String toBinary(long[] v, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = v.length - 1; i >= 0; i--) {
            String s = toBinary(v[i], len);
            sb.append(s).append(' ');
        }
        return sb.toString();
    }

}
