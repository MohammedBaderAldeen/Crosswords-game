package m;

import java.io.Serializable;

public class Square implements Serializable {
    private char correct, input;
    private int vertical, horizontal;

    public Square() {
        vertical = -1;
        horizontal = -1;
        correct = '.';
    }

    public Square(Square square) {
        this.correct = square.correct;
        this.input = square.input;
        this.vertical = square.vertical;
        this.horizontal = square.horizontal;
    }

    public Square(char correct, int vertical, int horizontal){
        this.correct = correct;
        this.vertical = vertical;
        this.horizontal = horizontal;
    }

    public Square(char correct){
        this(correct,-1,-1);
    }

    public char getCorrect() {
        return correct;
    }

    public void setCorrect(char correct) { this.correct = correct; }

    public int checkCorrect(char _new) {

        char old = input;
        input = _new;
        int result;

        if(old == correct && _new != correct ) {
            result = -1;
        } else if(old != correct && _new == correct) {
            result = 1;
        } else {
            result = 0;
        }
        return result;
    }

    public char getInput() {
        return input;
    }

    public void setInput(char input) {
        this.input = input;
    }

    public int getVertical() {
        return vertical;
    }

    public void setVertical(int vertical) {
        this.vertical = vertical;
    }

    public int getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(int horizontal) {
        this.horizontal = horizontal;
    }

    public boolean isEmpty() { return vertical == -1 && horizontal == -1 && correct!='*'; }

    @Override
    public String toString(){
        return "Square{" +
                "correct=" + correct +
                ", vertical=" + vertical +
                ", horizontal=" + horizontal +
                '}';
    }
}
