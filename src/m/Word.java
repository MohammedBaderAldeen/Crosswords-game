package m;

import java.io.Serializable;

public class Word implements Serializable
{
    private WordClue wordClue;
    private int iLocation;
    private int jLocation;
    private int numOfChars;

    public Word(String clue, String word, int iLocation, int jLocation) {
        this.wordClue = new WordClue(word,clue);
        this.iLocation = iLocation;
        this.jLocation = jLocation;
        numOfChars = word.length();
    }

    public Word(WordClue wordClue, int iLocation, int jLocation) {
        this.wordClue = wordClue;
        this.iLocation = iLocation;
        this.jLocation = jLocation;
        this.numOfChars = wordClue.getWord().length();
    }

    public String getClue() {
        return wordClue.getClue();
    }

    public String getWord() {
        return wordClue.getWord();
    }

    public int getiLocation() {
        return iLocation;
    }

    public int getjLocation() {
        return jLocation;
    }

    public WordClue getWordClue(){
        return wordClue;
    }

    @Override
    public String toString() {
        return "Word{" +
                "clue='" + getClue() + '\'' +
                ", word='" + getWord() + '\'' +
                "i = " + iLocation +
                "j = " + jLocation +
                '}';
    }
}
