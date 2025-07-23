package trie;

import java.util.HashMap;
import java.util.Map;

/**
 * A node of the trie.
 * The edges are stored in a map.
 *
 * @author Zdenek Tronicek
 */
public class NodeM {

    private final Map<Character, NodeM> nextMap = new HashMap<>();
    private int freq;

    public NodeM getChild(char c) {
        return nextMap.get(c);
    }

    public NodeM addChild(char c) {
        NodeM p = new NodeM();
        nextMap.put(c, p);
        return p;
    }

    public int getFreq() {
        return freq;
    }

    public void increaseFreq() {
        freq++;
    }

}
