package english;

import data.Input;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.TreeMap;

/**
 * Performance evaluation.
 *
 * @author Zdenek Tronicek
 */
public class Evaluation {

    static final int ALPH = 'z' - 'a' + 1;
    static final int[] tlengths = {
        10_000_000,
        20_000_000,
        30_000_000,
        40_000_000,
        50_000_000,
        60_000_000,
        70_000_000,
        80_000_000
    };
    static final Map<String, Map<Integer, List<Long>>> map = new HashMap<>();
    static int warm;
    static int measure;

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("expected arguments: properties plen window");
            return;
        }
        Properties conf = new Properties();
        try (FileReader in = new FileReader(args[0])) {
            conf.load(in);
        }
        warm = Integer.parseInt(conf.getProperty("warm"));
        measure = Integer.parseInt(conf.getProperty("measure"));
        int plen = Integer.parseInt(args[1]);
        int window = Integer.parseInt(args[2]);
        for (int tlen : tlengths) {
            String text = Input.generate(ALPH, tlen);
            String pattern = selectSubseq(text, plen, window);
            measureAll(text, pattern, window);
        }
        printTimes();
        printDeltas();
    }
    
    static String selectSubseq(String text, int plen, int window) {
        Random rand = new Random();
        int i = rand.nextInt(text.length() - window);
        String s = text.substring(i, i + window);
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() > plen) {
            int j = rand.nextInt(sb.length());
            sb.deleteCharAt(j);
        }
        return sb.toString();
    }

    static void measureAll(String text, String pattern, int window) throws Exception {
        measureNaive(text, pattern, window);
        measureBCGM(text, pattern, window);
        measureShiftAndColFull(text, pattern, window);
        measureShiftAndColReduced(text, pattern, window);
        measureShiftAndRowFull(text, pattern, window);
        measureShiftAndRowReduced(text, pattern, window);
        measureShiftOrColFull(text, pattern, window);
        measureShiftOrColReduced(text, pattern, window);
        measureShiftOrRowFull(text, pattern, window);
        measureShiftOrRowReduced(text, pattern, window);
        measureShiftAndMultipleWords(text, pattern, window);
    }
    
    static void printTimes() {
        System.out.println();
        System.out.println("Time in ms");
        System.out.print("     ");
        for (int tlen : tlengths) {
            System.out.printf(" %d", tlen);
        }
        System.out.println();
        printTimes("Naïve");
        printTimes("BCGM");
        printTimes("SACF");
        printTimes("SACR");
        printTimes("SARF");
        printTimes("SARR");
        printTimes("SOCF");
        printTimes("SOCR");
        printTimes("SORF");
        printTimes("SORR");
        printTimes("SAMW");
    }

    static void printTimes(String algo) {
        System.out.print(algo);
        Map<Integer, List<Long>> submap = map.get(algo);
        for (int tlen : tlengths) {
            List<Long> times = submap.get(tlen);
            long avg = average(times);
            System.out.printf(" %d", avg / 1_000_000L);
        }
        System.out.println();
    }

    static long average(List<Long> times) {
        long sum = 0L;
        for (long time : times) {
            sum += time;
        }
        return sum / times.size();
    }

    static void printDeltas() {
        System.out.println();
        System.out.println("Delta in %");
        System.out.print("     ");
        for (int tlen : tlengths) {
            System.out.printf(" %d", tlen);
        }
        System.out.println();
        printDeltas("Naïve");
        printDeltas("BCGM");
        printDeltas("SACF");
        printDeltas("SACR");
        printDeltas("SARF");
        printDeltas("SARR");
        printDeltas("SOCF");
        printDeltas("SOCR");
        printDeltas("SORF");
        printDeltas("SORR");
        printDeltas("SAMW");
    }

    static void printDeltas(String algo) {
        System.out.print(algo);
        Map<Integer, List<Long>> submap = map.get(algo);
        for (int tlen : tlengths) {
            List<Long> times = submap.get(tlen);
            long avg = average(times);
            double dt = delta(avg, times) * 100.0 / avg;
            System.out.printf(" %.0f", dt);
        }
        System.out.println();
    }

    static long delta(long avg, List<Long> times) {
        long d = 0L;
        for (long time : times) {
            long dn = Math.abs(time - avg);
            if (dn > d) {
                d = dn;
            }
        }
        return d;
    }

    static void storeTime(String algo, int tlen, long time) {
        Map<Integer, List<Long>> submap = map.get(algo);
        if (submap == null) {
            submap = new TreeMap<>();
            map.put(algo, submap);
        }
        List<Long> times = submap.get(tlen);
        if (times == null) {
            times = new ArrayList<>();
            submap.put(tlen, times);
        }
        times.add(time);
    }
    
    static void measureNaive(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = Naive.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            System.out.printf("Naïve warming up: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = Naive.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("Naïve", text.length(), time);
            System.out.printf("Naïve: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
    }

    static void measureBCGM(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = BCGM.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            System.out.printf("BCGM warming up: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = BCGM.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("BCGM", text.length(), time);
            System.out.printf("BCGM: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
    }

    static void measureShiftAndColFull(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftAndColFull.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            System.out.printf("SACF warming up: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftAndColFull.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SACF", text.length(), time);
            System.out.printf("SACF: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
    }

    static void measureShiftAndColReduced(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftAndColReduced.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            System.out.printf("SACR warming up: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftAndColReduced.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SACR", text.length(), time);
            System.out.printf("SACR: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
    }

    static void measureShiftAndRowFull(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftAndRowFull.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            System.out.printf("SARF warming up: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftAndRowFull.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SARF", text.length(), time);
            System.out.printf("SARF: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
    }

    static void measureShiftAndRowReduced(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftAndRowReduced.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            System.out.printf("SARR warming up: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftAndRowReduced.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SARR", text.length(), time);
            System.out.printf("SARR: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
    }

    static void measureShiftOrColFull(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftOrColFull.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            System.out.printf("SOCF warming up: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftOrColFull.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SOCF", text.length(), time);
            System.out.printf("SOCF: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
    }

    static void measureShiftOrColReduced(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftOrColReduced.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            long ms = time / 1_000_000L;
            System.out.printf("SOCR warming up: %d ms (%d positions)%n", ms, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftOrColReduced.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SOCR", text.length(), time);
            long ms = time / 1_000_000L;
            System.out.printf("SOCR: %d ms (%d positions)%n", ms, count);
        }
    }

    static void measureShiftOrRowFull(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftOrRowFull.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            long ms = time / 1_000_000L;
            System.out.printf("SORF warming up: %d ms (%d positions)%n", ms, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftOrRowFull.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SORF", text.length(), time);
            long ms = time / 1_000_000L;
            System.out.printf("SORF: %d ms (%d positions)%n", ms, count);
        }
    }

    static void measureShiftOrRowReduced(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftOrRowReduced.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            long ms = time / 1_000_000L;
            System.out.printf("SORR warming up: %d ms (%d positions)%n", ms, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftOrRowReduced.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SORR", text.length(), time);
            long ms = time / 1_000_000L;
            System.out.printf("SORR: %d ms (%d positions)%n", ms, count);
        }
    }

    static void measureShiftAndMultipleWords(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftAndMultipleWords.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            long ms = time / 1_000_000L;
            System.out.printf("SAMW warming up: %d ms (%d positions)%n", ms, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftAndMultipleWords.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SAMW", text.length(), time);
            long ms = time / 1_000_000L;
            System.out.printf("SAMW: %d ms (%d positions)%n", ms, count);
        }
    }

}
