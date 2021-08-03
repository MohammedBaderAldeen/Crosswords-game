package m;

import java.io.Serializable;

public class WordClue implements Serializable, Comparable {
    private String word;
    private String clue;
    private boolean used;

    public WordClue(String word, String clue) {
        this.word = word;
        this.clue = clue;
        this.used = false;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }

    public boolean isUsed() { return used; }

    public void setUsed(boolean used) { this.used = used; }

    @Override
    public int compareTo(Object o) {
        return this.word.compareTo(((WordClue)o).getWord());
    }

    @Override
    public String toString() {
        return word+": "+clue+".";
    }
}
