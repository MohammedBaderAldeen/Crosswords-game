package m;


import java.io.*;
import java.util.*;

public class Grid implements Serializable
{
    public Square[][] web;
    private int size;
    private int remaining;
    private HashMap<Integer,Word> vertical, horizontal;
    private Language language;
    private int time;

    public Grid(int size) {
        this.size = size;
        web = new Square[size][size];
        for( int i = 0; i < size ; i++) {
            web[i] = new Square[size];
            for(int j = 0; j < size; j++) {
                web[i][j] = new Square();
            }
        }
        remaining = size * size;
        vertical = new HashMap<>();
        horizontal = new HashMap<>();
        language = Language.English;
        time = 0;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public HashMap<Integer, Word> getVertical() {
        return vertical;
    }

    public void setVertical(HashMap<Integer, Word> vertical) {
        this.vertical = vertical;
    }
    
    public HashMap<Integer, Word> getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(HashMap<Integer,Word> horizontal) {
        this.horizontal = horizontal;
    }

    public void setSquare(int x,int y,char letter) {
        remaining -= web[x][y].checkCorrect(letter);
    }

    public void revealSquare(int x, int y) {
        setSquare(x,y,web[x][y].getCorrect());
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void flippityFlip() {
        Square temp[][] = new Square[size][size];

        for(int i = 0; i < size; i ++) {
            for(int j = 0; j < size; j++) {
                temp[i][size - j - 1] = new Square(web[i][j]);
            }
        }

//        for(Map.Entry<Integer,Word> entry: horizontal.entrySet()) {
//            Word word = (Word)entry.getValue();
//            word.
//        }

        this.web = temp;
    }

    public int calculateRemaining() {
        remaining = size * size;
        for( int i = 0; i < size; i++ ) {
            for( int j = 0; j < size; j++) {
                if(web[i][j].getCorrect() == '*') {
                    remaining--;
                }
            }
        }
        return remaining;
    }

    public int getTime(){
        return time;
    }

    public void setTime(int time){
        this.time = time;
    }

    @Override
    public String toString() {
        String array = "";
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(web[i][j].isEmpty()) { array += "i("+i+")j("+j+"): "+'.'+"    "; }
                else { array += "i("+i+")j("+j+"): "+web[i][j].getCorrect()+ "    "; }
            }
            array+="\n";
        }
        return array;
    }
}
