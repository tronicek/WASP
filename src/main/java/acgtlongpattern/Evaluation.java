package acgtlongpattern;

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
        measurePyrococcus(plen, window);
        measureStaphylococcus(plen, window);
        measureSalmonella(plen, window);
        measureEColi(plen, window);
        measureMalassezia(plen, window);
        measureSaccharomyces(plen, window);
        measurePenicillium(plen, window);
        measureNeurospora(plen, window);
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

    static void measurePyrococcus(int plen, int window) throws Exception {
        System.out.println("Pyrococcus furiosus");
        String text = Input.readPyrococcus();
        String pattern = select(text, plen, window);
        measureAll(text, pattern, window);
    }

    static void measureStaphylococcus(int plen, int window) throws Exception {
        System.out.println("Staphylococcus aureus");
        String text = Input.readStaphylococcus();
        String pattern = select(text, plen, window);
        measureAll(text, pattern, window);
    }

    static void measureSalmonella(int plen, int window) throws Exception {
        System.out.println("Salmonella enterica");
        String text = Input.readSalmonella();
        String pattern = select(text, plen, window);
        measureAll(text, pattern, window);
    }

    static void measureEColi(int plen, int window) throws Exception {
        System.out.println("Escherichia coli");
        String text = Input.readEColi();
        String pattern = select(text, plen, window);
        measureAll(text, pattern, window);
    }

    static void measureMalassezia(int plen, int window) throws Exception {
        System.out.println("Malassezia japonica");
        String text = Input.readMalassezia();
        String pattern = select(text, plen, window);
        measureAll(text, pattern, window);
    }

    static void measureSaccharomyces(int plen, int window) throws Exception {
        System.out.println("Saccharomyces cerevisiae");
        String text = Input.readSaccharomyces();
        String pattern = select(text, plen, window);
        measureAll(text, pattern, window);
    }

    static void measurePenicillium(int plen, int window) throws Exception {
        System.out.println("Penicillium chrysogenum");
        String text = Input.readPenicillium();
        String pattern = select(text, plen, window);
        measureAll(text, pattern, window);
    }

    static void measureNeurospora(int plen, int window) throws Exception {
        System.out.println("Neurospora hispaniola");
        String text = Input.readNeurospora();
        String pattern = select(text, plen, window);
        measureAll(text, pattern, window);
    }

    static void measureAll(String text, String pattern, int window) throws Exception {
        measureNaive(text, pattern, window);
        measureBCGMBI(text, pattern, window);
        measureShiftAndMultipleWords(text, pattern, window);
        measureShiftAndBigInteger(text, pattern, window);
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
        printTimes("BCGMBI");
        printTimes("SAMW");
        printTimes("SABI");
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
        printDeltas("BCGMBI");
        printDeltas("SAMW");
        printDeltas("SABI");
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

    static void measureBCGMBI(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = BCGMBigInteger.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            long ms = time / 1_000_000L;
            System.out.printf("BCGMBI warming up: %d ms (%d positions)%n", ms, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = BCGMBigInteger.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("BCGMBI", text.length(), time);
            long ms = time / 1_000_000L;
            System.out.printf("BCGMBI: %d ms (%d positions)%n", ms, count);
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

    static void measureShiftAndBigInteger(String text, String pattern, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int count = ShiftAndBigInteger.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            long ms = time / 1_000_000L;
            System.out.printf("SABI warming up: %d ms (%d positions)%n", ms, count);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int count = ShiftAndBigInteger.search(text, pattern, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("SABI", text.length(), time);
            long ms = time / 1_000_000L;
            System.out.printf("SABI: %d ms (%d positions)%n", ms, count);
        }
    }

}
