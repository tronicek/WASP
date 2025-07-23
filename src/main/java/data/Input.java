package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * Reads the genomic code into memory.
 *
 * @author Zdenek Tronicek
 */
public class Input {

    // data dir
    static String dataDir = "data";
    // Escherichia coli
    static final String E_COLI = "GCF_003018455.1_ASM301845v1_genomic.fna";
    // Malassezia japonica
    static final String MALASSEZIA = "GCA_029542785.1_ASM2954278v1_genomic.fna";
    // Neurospora hispaniola
    static final String NEUROSPORA = "GCA_033458365.1_Neuhi1_genomic.fna";
    // Penicillium chrysogenum
    static final String PENICILLIUM = "GCA_028827035.1_ASM2882703v1_genomic.fna";
    // Pyrococcus furiosus
    static final String PYROCOCCUS = "GCA_008245085.1_ASM824508v1_genomic.fna";
    // Saccharomyces cerevisiae
    static final String SACCHAROMYCES = "GCA_000146045.2_R64_genomic.fna";
    // Salmonella enterica
    static final String SALMONELLA = "GCA_000006945.2_ASM694v2_genomic.fna";
    // Staphylococcus aureus
    static final String STAPHYLOCOCCUS = "GCF_000418345.1_ASM41834v1_genomic.fna";

    public static void setDataDir(String dir) {
        dataDir = dir;
    }

    public static String readEColi() throws IOException {
        return read(E_COLI);
    }

    public static String readMalassezia() throws IOException {
        return read(MALASSEZIA);
    }

    public static String readNeurospora() throws IOException {
        return read(NEUROSPORA);
    }

    public static String readPenicillium() throws IOException {
        return read(PENICILLIUM);
    }

    public static String readPyrococcus() throws IOException {
        return read(PYROCOCCUS);
    }

    public static String readSaccharomyces() throws IOException {
        return read(SACCHAROMYCES);
    }

    public static String readSalmonella() throws IOException {
        return read(SALMONELLA);
    }

    public static String readStaphylococcus() throws IOException {
        return read(STAPHYLOCOCCUS);
    }

    public static String read(String path) throws IOException {
        Path input = Paths.get(dataDir, path);
        Charset cs = Charset.forName("UTF-8");
        List<String> lines = Files.readAllLines(input, cs);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith(">")) {
                continue;
            }
            sb.append(line);
        }
        return sb.toString().toLowerCase();
    }

    public static String generate(int alph, int len) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (; len > 0; len--) {
            int r = rand.nextInt(alph);
            char c = (char) ('a' + r);
            sb.append(c);
        }
        return sb.toString();
    }

    public static String generateDNA(int len) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (; len > 0; len--) {
            int i = rand.nextInt(4);
            switch (i) {
                case 0 -> sb.append('a');
                case 1 -> sb.append('c');
                case 2 -> sb.append('g');
                default -> sb.append('t');
            }
        }
        return sb.toString();
    }

    public static Set<Character> alphabet(String s) {
        Set<Character> alph = new TreeSet<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            alph.add(c);
        }
        return alph;
    }

    public static void main(String[] args) throws Exception {
        printInfo("Pyrococcus furiosus", readPyrococcus());
        printInfo("Staphylococcus aureus", readStaphylococcus());
        printInfo("Salmonella enterica", readSalmonella());
        printInfo("Escherichia coli", readEColi());
        printInfo("Malassezia japonica", readMalassezia());
        printInfo("Saccharomyces cerevisiae", readSaccharomyces());
        printInfo("Penicillium chrysogenum", readPenicillium());
        printInfo("Neurospora hispaniola", readNeurospora());
    }

    static void printHead(String path) throws IOException {
        Path input = Paths.get("data/" + path);
        Charset cs = Charset.forName("UTF-8");
        try ( BufferedReader in = Files.newBufferedReader(input, cs)) {
            for (int i = 0; i < 10; i++) {
                String line = in.readLine();
                System.out.println(line);
            }
        }
    }

    static void printInfo(String name, String genom) {
        Set<Character> alph = alphabet(genom);
        int len = genom.length();
        System.out.printf("%s: %d bases, alphabet: %s%n", name, len, alph);
    }

}
