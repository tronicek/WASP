package binarylongpattern;

import common.Str;
import data.Input;
import java.io.FileReader;
import java.math.BigInteger;
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

    static final int ALPH = 2;
    static final int[] tlengths = {
        1_000_000,
        2_000_000,
        3_000_000,
        4_000_000,
        5_000_000,
        6_000_000,
        7_000_000,
        8_000_000
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
        try ( FileReader in = new FileReader(args[0])) {
            conf.load(in);
        }
        warm = Integer.parseInt(conf.getProperty("warm"));
        measure = Integer.parseInt(conf.getProperty("measure"));
        int plen = Integer.parseInt(args[1]);
        int window = Integer.parseInt(args[2]);
        for (int tlen : tlengths) {
            String text = Input.generate(ALPH, tlen);
            String pattern = select(text, plen, window);
            measureAll(text, pattern, window);
        }
        printTimes();
        printDeltas();
    }
    
    static String select(String text, int plen, int window) {
        Random rand = new Random();
        int start = rand.nextInt(text.length() - window);
        String s = text.substring(start, start + window);
        StringBuilder sb = new StringBuilder(s);
        for (int i = window - plen; i > 0; i--) {
            int j = rand.nextInt(sb.length());
            sb.deleteCharAt(j);
        }
        return sb.toString();
    }

    static void measureAll(String text, String pattern, int window) throws Exception {
        measureNaive(text, pattern, window);
        measureBCGMBigInteger(text, pattern, window);
        measureShiftAndMultipleWords(text, pattern, window);
        measureShiftAndBigInteger(text, pattern, window);
        measureShiftAndRows(text, pattern, window);
        measureWindowAutomatonBigInteger(text, pattern, window);
        measureTextPreprocessingMapBigInteger(text, pattern, window);
        measureTextPreprocessingMapInt(text, pattern, window);
        measureTextPreprocessingMapStr(text, pattern, window);
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
        printTimes("BCGMBI");
        printTimes("SAMW");
        printTimes("SABI");
        printTimes("SAR");
        printTimes("WABI");
        printTimes("TPMBI");
        printTimes("TPMI");
        printTimes("TPMS");
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
        printDeltas("BCGMBI");
        printDeltas("SAMW");
        printDeltas("SABI");
        printDeltas("SAR");
        printDeltas("WABI");
        printDeltas("TPMBI");
        printDeltas("TPMI");
        printDeltas("TPMS");
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

    static void measureBCGMBigInteger(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = BCGMBigInteger.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            System.out.printf("BCGMBI warming up: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = BCGMBigInteger.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("BCGMBI", text.length(), time);
            System.out.printf("BCGMBI: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
    }

    static void measureShiftAndMultipleWords(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftAndMultipleWords.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            System.out.printf("SAMW warming up: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftAndMultipleWords.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SAMW", text.length(), time);
            System.out.printf("SAMW: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
    }

    static void measureShiftAndBigInteger(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftAndBigInteger.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            System.out.printf("SABI warming up: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftAndBigInteger.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SABI", text.length(), time);
            System.out.printf("SABI: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
    }

    static void measureShiftAndRows(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftAndRows.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            System.out.printf("SAR warming up: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftAndRows.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SAR", text.length(), time);
            System.out.printf("SAR: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
    }

    static void measureWindowAutomatonBigInteger(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = WindowAutomatonBigInteger.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            System.out.printf("WABI warming up: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = WindowAutomatonBigInteger.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("WABI", text.length(), time);
            System.out.printf("WABI: %d ms (%d positions)%n",
                    time / 1_000_000L, count);
        }
    }

    static void measureTextPreprocessingMapBigInteger(String text, String pattern, int window) throws Exception {
        for (int i = 0; i < warm; i++) {
            long start2 = System.nanoTime();
            Map<BigInteger, Integer> freqMap = TextPreprocessingMapBigInteger.preprocess(text, window);
            int count = TextPreprocessingMapBigInteger.search(freqMap, pattern, window);
            long end2 = System.nanoTime();
            long time2 = end2 - start2;
            System.out.printf("TPMBI warming up: %d ms (%d positions)%n",
                    time2 / 1_000_000L, count);
        }
        for (int i = 0; i < measure; i++) {
            long start2 = System.nanoTime();
            Map<BigInteger, Integer> freqMap = TextPreprocessingMapBigInteger.preprocess(text, window);
            int count = TextPreprocessingMapBigInteger.search(freqMap, pattern, window);
            long end2 = System.nanoTime();
            long time2 = end2 - start2;
            storeTime("TPMBI", text.length(), time2);
            System.out.printf("TPMBI: %d ms (%d positions)%n",
                    time2 / 1_000_000L, count);
        }
    }

    static void measureTextPreprocessingMapInt(String text, String pattern, int window) throws Exception {
        for (int i = 0; i < warm; i++) {
            long start2 = System.nanoTime();
            Map<Long, Integer> freqMap = TextPreprocessingMapInt.preprocess(text, window);
            int count = TextPreprocessingMapInt.search(freqMap, pattern, window);
            long end2 = System.nanoTime();
            long time2 = end2 - start2;
            System.out.printf("TPMI warming up: %d ms (%d positions)%n",
                    time2 / 1_000_000L, count);
        }
        for (int i = 0; i < measure; i++) {
            long start2 = System.nanoTime();
            Map<Long, Integer> freqMap = TextPreprocessingMapInt.preprocess(text, window);
            int count = TextPreprocessingMapInt.search(freqMap, pattern, window);
            long end2 = System.nanoTime();
            long time2 = end2 - start2;
            storeTime("TPMI", text.length(), time2);
            System.out.printf("TPMI: %d ms (%d positions)%n",
                    time2 / 1_000_000L, count);
        }
    }

    static void measureTextPreprocessingMapStr(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start2 = System.nanoTime();
            Map<Str, Integer> freqMap = TextPreprocessingMapStr.preprocess(text, window);
            int count = TextPreprocessingMapStr.search(freqMap, pattern, window);
            long end2 = System.nanoTime();
            long time2 = end2 - start2;
            System.out.printf("TPMS warming up: %d ms (%d positions)%n",
                    time2 / 1_000_000L, count);
        }
        for (int i = 0; i < measure; i++) {
            long start2 = System.nanoTime();
            Map<Str, Integer> freqMap = TextPreprocessingMapStr.preprocess(text, window);
            int count = TextPreprocessingMapStr.search(freqMap, pattern, window);
            long end2 = System.nanoTime();
            long time2 = end2 - start2;
            storeTime("TPMS", text.length(), time2);
            System.out.printf("TPMS: %d ms (%d positions)%n",
                    time2 / 1_000_000L, count);
        }
    }

}
