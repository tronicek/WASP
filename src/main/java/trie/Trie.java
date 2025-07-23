package trie;

/**
 * The trie for a small alphabet.
 * It uses class Node.
 *
 * @author Zdenek Tronicek
 */
public class Trie {

    private final int alph;
    private final Node root;

    public Trie(int alph) {
        this.alph = alph;
        root = new Node(alph);
    }

    public void add(String s) {
        Node p = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            Node np = p.getChild(c);
            if (np == null) {
                np = p.addChild(alph, c);
            }
            p = np;
        }
        p.increaseFreq();
    }
    
    public int freq(String s) {
        Node p = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            p = p.getChild(c);
            if (p == null) {
                return 0;
            }
        }
        return p.getFreq();
    }

}
