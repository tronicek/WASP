package trie;

/**
 * A node of the trie.
 * The edges are stored in an array.
 *
 * @author Zdenek Tronicek
 */
public class Node {

    private final Node[] next;
    private int freq;

    public Node(int alph) {
        next = new Node[alph];
    }

    public Node getChild(char c) {
        int i = c - 'a';
        return next[i];
    }

    public Node addChild(int alph, char c) {
        int i = c - 'a';
        next[i] = new Node(alph);
        return next[i];
    }

    public int getFreq() {
        return freq;
    }

    public void increaseFreq() {
        freq++;
    }

}
