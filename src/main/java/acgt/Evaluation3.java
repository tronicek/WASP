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
public class Evaluation3 {

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

    static void measurePyrococcus(int plen, int window) throws Exception {
        System.out.println("Pyrococcus furiosus");
        String text = Input.readPyrococcus();
        measureTextPreprocessing(text, plen, window);
    }

    static void measureStaphylococcus(int plen, int window) throws Exception {
        System.out.println("Staphylococcus aureus");
        String text = Input.readStaphylococcus();
        measureTextPreprocessing(text, plen, window);
    }

    static void measureSalmonella(int plen, int window) throws Exception {
        System.out.println("Salmonella enterica");
        String text = Input.readSalmonella();
        measureTextPreprocessing(text, plen, window);
    }

    static void measureEColi(int plen, int window) throws Exception {
        System.out.println("Escherichia coli");
        String text = Input.readEColi();
        measureTextPreprocessing(text, plen, window);
    }

    static void measureMalassezia(int plen, int window) throws Exception {
        System.out.println("Malassezia japonica");
        String text = Input.readMalassezia();
        measureTextPreprocessing(text, plen, window);
    }

    static void measureSaccharomyces(int plen, int window) throws Exception {
        System.out.println("Saccharomyces cerevisiae");
        String text = Input.readSaccharomyces();
        measureTextPreprocessing(text, plen, window);
    }

    static void measurePenicillium(int plen, int window) throws Exception {
        System.out.println("Penicillium chrysogenum");
        String text = Input.readPenicillium();
        measureTextPreprocessing(text, plen, window);
    }

    static void measureNeurospora(int plen, int window) throws Exception {
        System.out.println("Neurospora hispaniola");
        String text = Input.readNeurospora();
        measureTextPreprocessing(text, plen, window);
    }

    static void printTimes() {
        System.out.println();
        System.out.println("Time in ms");
        System.out.print("     ");
        Map<Integer, List<Long>> submap = map.get("FP");
        for (int tlen : submap.keySet()) {
            System.out.printf(" %d", tlen);
        }
        System.out.println();
        printTimes("FP");
    }
    
    static void printDeltas() {
        System.out.println();
        System.out.println("Delta in %");
        System.out.print("     ");
        Map<Integer, List<Long>> submap = map.get("FP");
        for (int tlen : submap.keySet()) {
            System.out.printf(" %d", tlen);
        }
        System.out.println();
        printDeltas("FP");
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

    static void measureTextPreprocessing(String text, int plen, int window) {
        for (int i = 0; i < warm; i++) {
            long start = System.nanoTime();
            int[] wasp = FullPreprocessing.preprocess(text, plen, window);
            long end = System.nanoTime();
            long time = end - start;
            long pos = 0L;
            for (int w : wasp) {
                pos += w;
            }
            System.out.printf("FP warming up: %d ms (%d patterns, %d positions)%n", 
                    time / 1_000_000L, wasp.length, pos);
        }
        for (int i = 0; i < measure; i++) {
            long start = System.nanoTime();
            int[] wasp = FullPreprocessing.preprocess(text, plen, window);
            long end = System.nanoTime();
            long time = end - start;
            storeTime("FP", text.length(), time);
            long pos = 0L;
            for (int w : wasp) {
                pos += w;
            }
            System.out.printf("FP: %d ms (%d patterns, %d positions)%n",
                    time / 1_000_000L, wasp.length, pos);
        }
    }

}
