package acgt;

import data.Input;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Performance evaluation.
 *
 * @author Zdenek Tronicek
 */
public class Evaluation {

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
        String dataDir = conf.getProperty("dataDir");
        Input.setDataDir(dataDir);
        warm = Integer.parseInt(conf.getProperty("warm"));
        measure = Integer.parseInt(conf.getProperty("measure"));
        int plen = Integer.parseInt(args[1]);
        int window = Integer.parseInt(args[2]);
        String pattern = Input.generateDNA(plen);
        System.out.printf("Pattern: %s%n", pattern);
        measurePyrococcus(pattern, window);
        measureStaphylococcus(pattern, window);
        measureSalmonella(pattern, window);
        measureEColi(pattern, window);
        measureMalassezia(pattern, window);
        measureSaccharomyces(pattern, window);
        measurePenicillium(pattern, window);
        measureNeurospora(pattern, window);
        printTimes();
        printDeltas();
    }

    static void measurePyrococcus(String pattern, int window) throws Exception {
        System.out.println("Pyrococcus furiosus");
        String text = Input.readPyrococcus();
        measureAll(text, pattern, window);
    }

    static void measureStaphylococcus(String pattern, int window) throws Exception {
        System.out.println("Staphylococcus aureus");
        String text = Input.readStaphylococcus();
        measureAll(text, pattern, window);
    }

    static void measureSalmonella(String pattern, int window) throws Exception {
        System.out.println("Salmonella enterica");
        String text = Input.readSalmonella();
        measureAll(text, pattern, window);
    }

    static void measureEColi(String pattern, int window) throws Exception {
        System.out.println("Escherichia coli");
        String text = Input.readEColi();
        measureAll(text, pattern, window);
    }

    static void measureMalassezia(String pattern, int window) throws Exception {
        System.out.println("Malassezia japonica");
        String text = Input.readMalassezia();
        measureAll(text, pattern, window);
    }

    static void measureSaccharomyces(String pattern, int window) throws Exception {
        System.out.println("Saccharomyces cerevisiae");
        String text = Input.readSaccharomyces();
        measureAll(text, pattern, window);
    }

    static void measurePenicillium(String pattern, int window) throws Exception {
        System.out.println("Penicillium chrysogenum");
        String text = Input.readPenicillium();
        measureAll(text, pattern, window);
    }

    static void measureNeurospora(String pattern, int window) throws Exception {
        System.out.println("Neurospora hispaniola");
        String text = Input.readNeurospora();
        measureAll(text, pattern, window);
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
        measureWindowAutomaton(text, pattern, window);
        measureWindowAutomatonBit(text, pattern, window);
        measureTextPreprocessing(text, pattern, window);
    }

    static void printTimes() {
        System.out.println();
        System.out.println("Time in ms");
        System.out.print("     ");
        Map<Integer, List<Long>> submap = map.get("Naïve");
        for (int tlen : submap.keySet()) {
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
        printTimes("WA");
        printTimes("WAB");
        printTimes("TP");
    }

    static void printTimes(String algo) {
        System.out.print(algo);
        Map<Integer, List<Long>> submap = map.get(algo);
        for (int tlen : submap.keySet()) {
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
        Map<Integer, List<Long>> submap = map.get("Naïve");
        for (int tlen : submap.keySet()) {
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
        printDeltas("WA");
        printDeltas("WAB");
        printDeltas("TP");
    }

    static void printDeltas(String algo) {
        System.out.print(algo);
        Map<Integer, List<Long>> submap = map.get(algo);
        for (int tlen : submap.keySet()) {
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
            long ms = time / 1_000_000L;
            System.out.printf("Naïve warming up: %d ms (%d positions)%n", ms, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = Naive.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("Naïve", text.length(), time);
            long ms = time / 1_000_000L;
            System.out.printf("Naïve: %d ms (%d positions)%n", ms, count);
        }
    }

    static void measureBCGM(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = BCGM.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            long ms = time / 1_000_000L;
            System.out.printf("BCGM warming up: %d ms (%d positions)%n", ms, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = BCGM.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("BCGM", text.length(), time);
            long ms = time / 1_000_000L;
            System.out.printf("BCGM: %d ms (%d positions)%n", ms, count);
        }
    }

    static void measureShiftAndColFull(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftAndColFull.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            long ms = time / 1_000_000L;
            System.out.printf("SACF warming up: %d ms (%d positions)%n", ms, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftAndColFull.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SACF", text.length(), time);
            long ms = time / 1_000_000L;
            System.out.printf("SACF: %d ms (%d positions)%n", ms, count);
        }
    }

    static void measureShiftAndColReduced(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftAndColReduced.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            long ms = time / 1_000_000L;
            System.out.printf("SACR warming up: %d ms (%d positions)%n", ms, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftAndColReduced.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SACR", text.length(), time);
            long ms = time / 1_000_000L;
            System.out.printf("SACR: %d ms (%d positions)%n", ms, count);
        }
    }

    static void measureShiftAndRowFull(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftAndRowFull.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            long ms = time / 1_000_000L;
            System.out.printf("SARF warming up: %d ms (%d positions)%n", ms, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftAndRowFull.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SARF", text.length(), time);
            long ms = time / 1_000_000L;
            System.out.printf("SARF: %d ms (%d positions)%n", ms, count);
        }
    }

    static void measureShiftAndRowReduced(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftAndRowReduced.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            long ms = time / 1_000_000L;
            System.out.printf("SARR warming up: %d ms (%d positions)%n", ms, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftAndRowReduced.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SARR", text.length(), time);
            long ms = time / 1_000_000L;
            System.out.printf("SARR: %d ms (%d positions)%n", ms, count);
        }
    }

    static void measureShiftOrColFull(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftOrColFull.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            long ms = time / 1_000_000L;
            System.out.printf("SOCF warming up: %d ms (%d positions)%n", ms, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftOrColFull.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SOCF", text.length(), time);
            long ms = time / 1_000_000L;
            System.out.printf("SOCF: %d ms (%d positions)%n", ms, count);
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

    static void measureWindowAutomaton(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = WindowAutomaton.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            long ms = time / 1_000_000L;
            System.out.printf("WA warming up: %d ms (%d positions)%n", ms, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = WindowAutomaton.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("WA", text.length(), time);
            long ms = time / 1_000_000L;
            System.out.printf("WA: %d ms (%d positions)%n", ms, count);
        }
    }

    static void measureWindowAutomatonBit(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = WindowAutomatonBit.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            long ms = time / 1_000_000L;
            System.out.printf("WAB warming up: %d ms (%d positions)%n", ms, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = WindowAutomatonBit.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("WAB", text.length(), time);
            long ms = time / 1_000_000L;
            System.out.printf("WAB: %d ms (%d positions)%n", ms, count);
        }
    }

    static void measureTextPreprocessing(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int[] freq = TextPreprocessing.preprocess(text, window);
            int count = TextPreprocessing.search(freq, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            long ms = time / 1_000_000L;
            System.out.printf("TP warming up: %d ms (%d positions)%n", ms, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int[] freq = TextPreprocessing.preprocess(text, window);
            int count = TextPreprocessing.search(freq, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("TP", text.length(), time);
            long ms = time / 1_000_000L;
            System.out.printf("TP: %d ms (%d positions)%n", ms, count);
        }
    }

}
