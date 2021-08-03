package m;

import java.util.HashSet;
import java.util.SortedSet;

public class GridGenerator {

    private static HashSet<WordClue> startedWith = new HashSet<>();
    private static int blocks = 0;

    public static Grid generate(int size, Language language) {

        Grid grid = new Grid(size);
        grid.setLanguage(language);

        Dictionary.loadDict(language);
        int maxWords = countSizeableWords(size);

        while(true)
        {
            if(startedWith.size() >= maxWords) {
                startedWith.clear();
                blocks++;
                maxWords = countSizeableWords(size - blocks);
                if(maxWords == 0) {
                    blocks = 0;
                    return null;
                }
            }

            for(int k = 0; k < blocks; k++) {
                grid.web[0][k].setCorrect('*');
            }

            if(fillOutLine(grid,0,0)) {
                fillOutBlacks(grid);
                if(language == Language.Arabic) {
                    grid.flippityFlip();
                }
                startedWith.clear();
                blocks = 0;
                return grid;
            }

        }
    }

    private static int countSizeableWords(int size) {
        int count = 0;
        SortedSet set = Dictionary.getDict();
        for(Object item: set) {
            WordClue wordClue = (WordClue)item;
            if(wordClue.getWord().length() <= size) {
                count++;
            }
        }
        return count;
    }

    private static WordClue pickRandomWord() {
        SortedSet<WordClue> set = Dictionary.getDict();
        int random = (int) (Math.random() * set.size());
        return (WordClue)set.toArray()[random];
    }

    private static boolean completed(Grid grid) {
        System.out.println(grid);
        int verticalWords = grid.getVertical().size();
        int horizontalWords = grid.getHorizontal().size();
        int words = verticalWords + horizontalWords;

        if(grid.getLanguage() == Language.PerfectEnglish) {
            return words >= (grid.getSize() * 2);
        }

        switch ( grid.getSize() ) {
            case 4:
                return words >= 6;
            case 5:
                return words >= 8;
            case 7:
                return words >= 9;
            default:
                return words >= grid.getSize() * 1.5;
        }

    }

    private static void fillOutBlacks(Grid grid) {
        for(int i = 0; i < grid.getSize(); i++) {
            for(int j = 0; j < grid.getSize(); j++) {
                if(grid.web[i][j].isEmpty()) {
                    grid.web[i][j].setCorrect('*');
                    grid.setRemaining(grid.getRemaining() - 1);
                } else if (grid.web[i][j].getCorrect() == '*') {
                    grid.setRemaining(grid.getRemaining() - 1);
                }
            }
        }
    }

    private static String getAlreadyPlaced(Grid grid, int i, int j, boolean vertical){
        String result = "";
        while(i < grid.getSize() && j < grid.getSize() && !grid.web[i][j].isEmpty() && !(grid.web[i][j].getCorrect() == '*')) {
            result += grid.web[i][j].getCorrect();

            if ( vertical ) {
                i++;
            } else {
                j++;
            }
        }
        return result;
    }

    private static Location findFinalBlockinLine(Grid grid, int i, int j) {
        Location location = new Location();
        for(int k = 0; k < grid.getSize(); k++) {
            if(grid.web[i][k].getCorrect() == '*' && (location.j - k < 2 && location.i != -1)) {
                location.i = i;
                location.j = k;
            }
        }
        if(location.i == -1) {
            return new Location(i,j);
        } else {
            return location;
        }
    }

    private static Location findFinalBlockinColumn(Grid grid, int i, int j) {
        Location location = new Location();
        for(int k = 0; k < grid.getSize(); k++) {
            if ( grid.web[k][j].getCorrect() == '*' && (location.i - k < 2 && location.i != -1) ) {
                location.i = k;
                location.j = j;
            }
        }
        if(location.i == -1) {
            return new Location(i,j);
        } else {
            return location;
        }
    }

    private static boolean checkSizeFit(Grid grid, String word, int i, int j, boolean vertical) {
        int shi;
        if(vertical) {
            shi = i;
        } else {
            shi = j;
        }
        if (grid.getSize() >= word.length() + shi) {
            return checksSizeFit(grid, word, i, j, vertical);
        } else {
            return false;
        }
    }

    private static boolean checksSizeFit(Grid grid, String word, int i, int j, boolean vertical) {
        try{
            if(vertical) {
                for(int k = 0; k < word.length(); k++) {
                    if((grid.web[k + i][j].getCorrect() != word.charAt(k) && !grid.web[k + i][j].isEmpty()) || grid.web[k + i][j].getCorrect() == '*') {
                        return false;
                    }
                }
            } else {
                for(int k = 0; k < word.length(); k++) {
                    if((grid.web[i][k + j].getCorrect() != word.charAt(k) && !grid.web[i][k + j].isEmpty()) || grid.web[i][k + j].getCorrect() == '*') {
                        return false;
                    }
                }
            }
        } catch ( IndexOutOfBoundsException e ) {
            return false;
        }
        return true;
    }

    private static Location placeVerticalWord(Grid grid, WordClue word, int i, int j) {

        word.setUsed(true);
        Word wordToAdd;
        if(grid.getLanguage() == Language.English || grid.getLanguage() == Language.PerfectEnglish) {
            wordToAdd = new Word(word, i, j);
        } else {
            wordToAdd = new Word(word, i, grid.getSize() - j - 1);
        }
        int key = grid.getVertical().size() + 1;

        grid.getVertical().put(key, wordToAdd);

        for (int k = 0; k < word.getWord().length(); k++) {

            grid.web[k + i][j].setCorrect(word.getWord().charAt(k));
            grid.web[k + i][j].setVertical(key);
        }

        if (i + word.getWord().length() < grid.getSize()) {
            grid.web[i + word.getWord().length()][j].setCorrect('*');
            return new Location(i + word.getWord().length(),j);
        }
        return new Location(i + word.getWord().length() - 1,j);

    }

    private static Location placeHorizontalWord(Grid grid, WordClue word, int i, int j) {

        word.setUsed(true);
        Word wordToAdd;
        if(grid.getLanguage() == Language.English || grid.getLanguage() == Language.PerfectEnglish ) {
            wordToAdd = new Word(word, i, j);
        } else {
            wordToAdd = new Word(word, i, grid.getSize() - j - 1);
        }
        int key = grid.getHorizontal().size() + 1;
        grid.getHorizontal().put(key, wordToAdd);

        for ( int k = 0; k < word.getWord().length(); k++ ) {

            grid.web[i][k + j].setCorrect(word.getWord().charAt(k));
            grid.web[i][k + j].setHorizontal(key);

        }

        if ( j + word.getWord().length() < grid.getSize() ) {
            grid.web[i][j + word.getWord().length()].setCorrect('*');
            return new Location(i, j + word.getWord().length());
        }
        return new Location(i, j + word.getWord().length() - 1);
    }

    private static boolean fillOutLine(Grid grid,int i, int j) {
        if(completed(grid)) { return true; }

        boolean done = false;
        Square begining = grid.web[i][j];
        Location endingLocation;

        while(begining.getCorrect() == '*') {
            j++;
            begining = grid.web[i][j];
        }


        if(grid.getSize() - j < 3) { return false; }

        if(i == 0 && j == blocks) {
            if(begining.isEmpty()) {
                while ( true ) {
                    WordClue word = pickRandomWord();
                    if ( checkSizeFit(grid, word.getWord(), i, j, false) && !word.isUsed() ) {
                        if ( startedWith.contains(word) ) {
                            continue;
                        }
                        startedWith.add(word);
                        endingLocation = placeHorizontalWord(grid, word, i, j);
                        break;
                    }
                }
                fillRestOfLine(grid, endingLocation.i, endingLocation.j);
                done = fillOutColumn(grid, 0, i);
                if ( !done ) {
                    removeLine(grid, i);
                    return false;
                } else {
                    return true;
                }
            } else { return false; }
        } else {
            String lastChar = (grid.getLanguage() == Language.English  || grid.getLanguage() == Language.PerfectEnglish)? "ZZZZZZZZZZ" : "ييييييييييي";

            Location location = findFinalBlockinLine(grid,i,j);
            i = location.i;
            j = location.j;

            if(grid.web[i][j].getCorrect() == '*') {
                j++;
            }

            String alreadyPlacedCharacters = getAlreadyPlaced(grid,i,j,false);
            WordClue beginWith = new WordClue(alreadyPlacedCharacters,"");
            WordClue endWith = new WordClue(alreadyPlacedCharacters + lastChar,"");

            SortedSet<WordClue> suitableWordClues = Dictionary.getDict().subSet(beginWith, endWith);

            for(WordClue word: suitableWordClues) {
                if ( checkSizeFit(grid, word.getWord(), i, j, false) && !word.isUsed() ) {
                    endingLocation = placeHorizontalWord(grid, word, i, j);

                    fillRestOfLine(grid, endingLocation.i, endingLocation.j);

                    done = fillOutColumn(grid, 0, i);
                    if ( !done ) {
                        removeLine(grid, i);
                    }
                    else {
                        return true;
                    }
                }
            }
//            j = j + alreadyPlacedCharacters.length();
//            grid.web[i][j].setCorrect('*');
//            return fillOutLine(grid,i,j + 1);
            return false;
        }
    }

    private static boolean fillOutColumn(Grid grid, int i, int j) {
        if(completed(grid)) { return true; }


        boolean done = false;
        Square begining = grid.web[i][j];
        Location endingLocation;

        while(begining.getCorrect() == '*') {
            i++;
            begining = grid.web[i][j];
        }

        if(grid.getSize() - i < 3) { return false; }

//        if(begining.isEmpty()) {
//            while ( true ) {
//                WordClue word = pickRandomWord();
//                if(checkSizeFit(grid,word.getWord(),i,j,true) && !word.isUsed() ){
//                    endingLocation = placeVerticalWord(grid,word,i,j);
//                    break;
//                }
//            }
//            fillRestOfColumn(grid,endingLocation.i,endingLocation.j);
//
//            done = fillOutLine(grid, j + 1,0);
//            if ( !done ) {
//                removeColumn(grid, j);
//                return false;
//            } else {
//                return true;
//            }
//        } else {
            String lastChar = (grid.getLanguage() == Language.English || grid.getLanguage() == Language.PerfectEnglish)? "ZZZZZZZZZZ" : "ييييييييييي";

            Location location = findFinalBlockinColumn(grid,i,j);
            i = location.i;
            j = location.j;

            if(grid.web[i][j].getCorrect() == '*') {
                i++;
            }

            String alreadyPlacedCharacters = getAlreadyPlaced(grid,i,j,true);
            WordClue beginWith = new WordClue(alreadyPlacedCharacters,"");
            WordClue endWith = new WordClue(alreadyPlacedCharacters + lastChar,"");


            SortedSet<WordClue> suitableWordClues = Dictionary.getDict().subSet(beginWith, endWith);

            for(WordClue word: suitableWordClues) {
                if ( checkSizeFit(grid, word.getWord(), i, j, true) && !word.isUsed() ) {
                    endingLocation = placeVerticalWord(grid, word, i, j);
                    fillRestOfColumn(grid, endingLocation.i, endingLocation.j);

                    done = fillOutLine(grid, j + 1,0);
                    if ( !done ) {
                        removeColumn(grid, j);
                    }
                    else {
                        return true;
                    }
                }
            }
            //i = i + alreadyPlacedCharacters.length();
            //grid.web[i][j].setCorrect('*');
            //return fillOutColumn(grid,i + 1,j);
            return false;
        //}
    }

    private static void removeLine(Grid grid,int i) {
        for(int j = 0; j < grid.getSize(); j++) {

            int horizontalWordNumber = grid.web[i][j].getHorizontal();
            Word horizontalWordToRemove = grid.getHorizontal().get(horizontalWordNumber);

            if ( horizontalWordToRemove != null) {
                grid.getHorizontal().get(horizontalWordNumber).getWordClue().setUsed(false);
                grid.getHorizontal().remove(horizontalWordNumber);
            }
            if(grid.web[i][j].getCorrect() == '*') { grid.web[i][j].setCorrect('.'); }
            grid.web[i][j].setHorizontal(-1);
        }
    }

    private static void removeColumn(Grid grid, int j) {
        for(int i = 0; i < grid.getSize(); i++) {

            int verticalWordNumber = grid.web[i][j].getVertical();
            Word verticalWordToRemove = grid.getVertical().get(verticalWordNumber);

            if ( verticalWordToRemove != null) {
                grid.getVertical().get(verticalWordNumber).getWordClue().setUsed(false);
                grid.getVertical().remove(verticalWordNumber);
            }

            if(grid.web[i][j].getCorrect() == '*') { grid.web[i][j].setCorrect('.'); }
            grid.web[i][j].setVertical(-1);
        }
    }

    private static void fillRestOfLine(Grid grid, int line,int lastEnding) {
        WordClue word = new WordClue("","");
        int remainingSquares = grid.getSize() - lastEnding - 1;

        switch ( remainingSquares ) {
            case 0: return;
            case 3: grid.web[line][lastEnding + 3].setCorrect('*');
            case 2:
                grid.web[line][lastEnding + 2].setCorrect('*');
                grid.web[line][lastEnding + 1].setCorrect(randomChar());
                break;
            case 1:
                grid.web[line][lastEnding + 1].setCorrect(randomChar());
                break;
            default:
                if(remainingSquares < 0) {return;}
                while (true) {
                    word = pickRandomWord();
                    if(checkSizeFit(grid, word.getWord(), line,lastEnding + 1,false)) {
                        placeHorizontalWord(grid,word,line,lastEnding + 1);
                        break;
                    }
                }
                break;
        }
    }

    private static void fillRestOfColumn(Grid grid, int lastEnding, int column) {
        WordClue word = new WordClue("","");
        int remainingSquares = grid.getSize() - lastEnding - 1;

        switch ( remainingSquares ) {
            case 0: return;
            case 3:
                grid.web[lastEnding + 3][column].setCorrect('*');
            case 2:
                grid.web[lastEnding + 2][column].setCorrect('*');
                grid.web[lastEnding + 1][column].setCorrect(randomChar());
                break;
            case 1:
                grid.web[lastEnding + 1][column].setCorrect(randomChar());
                break;
            default:
                if(remainingSquares < 0) {return;}
                while (true) {
                    word = pickRandomWord();
                    if(checkSizeFit(grid, word.getWord(), lastEnding + 1, column, true)) {
                        placeVerticalWord(grid,word,lastEnding + 1,column);
                        break;
                    }
                }
                break;
        }
    }

    private static char randomChar() {
        int random = (int) (Math.random() * 25);
        random += 65;
        return (char)random;
    }
}
