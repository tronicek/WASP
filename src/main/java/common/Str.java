package common;

/**
 * String class. It can get hash code as a parameter to the constructor.
 *
 * @author Zdenek Tronicek
 */
public class Str {

    private final char[] text;
    private final int start;
    private final int end;
    private final int hash;

    public Str(char[] text, int start, int end) {
        this.text = text;
        this.start = start;
        this.end = end;
        hash = computeHash();
    }

    public Str(char[] text, int start, int end, int hash) {
        this.text = text;
        this.start = start;
        this.end = end;
        this.hash = hash;
    }

    private int computeHash() {
        int h = 0;
        for (int i = start; i < end; i++) {
            //h = 31 * h + text.charAt(i);
            h = (h << 5) - h + text[i];
        }
        return h;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Str that) {
            if (end - start != that.end - that.start) {
                return false;
            }
            for (int i = start, j = that.start; i < end; i++, j++) {
                if (text[i] != that.text[j]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
