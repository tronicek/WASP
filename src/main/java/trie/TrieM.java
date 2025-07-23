package trie;

/**
 * The trie for a large alphabet.
 * It uses NodeM.
 *
 * @author Zdenek Tronicek
 */
public class TrieM {

    private final NodeM root = new NodeM();

    public void add(String s) {
        NodeM p = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            NodeM np = p.getChild(c);
            if (np == null) {
                np = p.addChild(c);
            }
            p = np;
        }
        p.increaseFreq();
    }
    
    public int freq(String s) {
        NodeM p = root;
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
