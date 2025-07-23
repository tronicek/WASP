package data;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Counts unique substrings of the specified size.
 * 
 * @author Zdenek Tronicek
 */
public class Test {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("argument: window size");
            return;
        }
        int win = Integer.parseInt(args[0]);
        Set<String> set = new TreeSet<>();
        String s = Input.readEColi();
        for (int i = 0; i + win <= s.length(); i++) {
            String ws = s.substring(i, i + win);
            set.add(ws);
        }
        int wc = s.length() - win + 1;
        int uniq = set.size();
        double pc = 100.0 * uniq / wc;
        System.out.printf("win: %d, substrings: %d, unique: %d (%.2f%%)%n", win, wc, uniq, pc);
    }

}
