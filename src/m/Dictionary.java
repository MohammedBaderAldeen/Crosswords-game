package m;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.SortedSet;

public class Dictionary {
    private static SortedSet<WordClue> dict;
    private static Language language;

    private static String arabicFilename = "arabic.words";
    private static String englishFilename = "english.words";
    private static String perfectEnglishFilename = "perfectEnglish.words";

    public static void loadDict(Language l) {
        language = l;
        String filename = "";
        switch ( language ) {
            case English:
                filename = englishFilename;
                break;
            case Arabic:
                filename = arabicFilename;
                break;
            case PerfectEnglish:
                filename = perfectEnglishFilename;
                break;
        }

        ObjectInputStream input = null;

        try {
            input = new ObjectInputStream(new FileInputStream(filename));
            dict = (SortedSet<WordClue>) input.readObject();
        } catch ( Exception e ) {

        }finally {
            try {
                if(input != null) {
                    input.close();
                }
            } catch ( IOException e ) {}
        }
    }

    public static SortedSet<WordClue> getDict(){
        return dict;
    }
}
