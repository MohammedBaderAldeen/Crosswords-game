package m;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GameController
{
    private Button charHintButton = new Button("Hint");
    private Button wordHintButton = new Button("Word Hint");
    private Button exitButton = new Button("Exit");
    private Button saveButton = new Button("Save");

    private Label timerLabel = new Label("Time:");
    private Label timer = new Label("00:00");

    private Label scoreLabel = new Label("Score:");
    private Label scoring = new Label();

    private HBox timeHbox = new HBox();
    private HBox scoreHbox = new HBox();
    private HBox buttonHbox = new HBox();

    private HBox infobar = new HBox();

    private Label verticalLabel = new Label("Vertical");
    private Label horizontalLabel = new Label("Horizontal");

    private VBox cluesVbox = new VBox();

    private GridPane webLayout = new GridPane();
    private HBox top = new HBox();

    private String selectedStyle = "-fx-background-color: linear-gradient(#ff5400,#be1d00);\n" +
            "    -fx-background-radius: 30;\n" +
            "    -fx-background-insets: 0;\n" +
            "    -fx-text-fill: white;\n";

    private String defaultStyle = "-fx-background-color:\n" +
            "\t\t\tlinear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),\n" +
            "\t\t\tlinear-gradient(#020b02, #3a3a3a),\n" +
            "\t\t\tlinear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%),\n" +
            "\t\t\tlinear-gradient(#f5f5f5 0%, #dbdbdb 50%, #cacaca 51%, #d7d7d7 100%);\n" +
            "\t-fx-background-insets: 0,1,4,5;\n" +
            "\t-fx-background-radius: 9,8,5,4;\n" +
            "\t-fx-padding: 15 30 15 30;\n" +
            "\t-fx-font-family: \"Helvetica\";\n" +
            "\t-fx-font-size: 18px;\n" +
            "\t-fx-font-weight: bold;\n" +
            "\t-fx-text-fill: #333333;\n" +
            "\t-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);";

    private String correctStyle = "-fx-background-color:\n" +
            "            #090a0c,\n" +
            "            linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
            "            linear-gradient(#20262b, #191d22),\n" +
            "            radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
            "    -fx-background-radius: 5,4,3,5;\n" +
            "    -fx-background-insets: 0,1,2,0;\n" +
            "    -fx-text-fill: white;\n" +
            "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
            "    -fx-font-family: \"Arial\" bold;\n" +
            "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
            "    -fx-font-size: 20px;\n" +
            "    -fx-padding: 10 20 10 20;";


    private String disabledStyle = "-fx-opacity: 0.5";
    private String oldFocusStyle = defaultStyle;

    private VBox fullLayout = new VBox();

    private Button[][] game;
    private Grid grid;
    private int size;
    private Language language;
    private boolean horizontal = true;
    private boolean wordHinting = false;

    private Button focus;
    private int focusI, focusJ;
    private Button selectedButton;

    private int score = 0;
    private int secondsPassed;
    private Timer timeTimer;

    private TextField gameName = new TextField();

    private Scene scene;
    private HashMap<Integer,Button> verticalWordButtons = new HashMap<>();
    private HashMap<Integer,Button> horizontalWordButtons = new HashMap<>();

    public GameController(Grid grid) {

        exitButton.setOnAction(e ->  {
            addScore(0);
            Main.show();
        } );

        if(grid == null) {
            Label failedLabel = new Label("Failed to generate grid");
            exitButton.setPrefSize(500,50);

            fullLayout = new VBox();
            fullLayout.getChildren().addAll(failedLabel,exitButton);
            fullLayout.setSpacing(20);
            fullLayout.setAlignment(Pos.CENTER);

            scene = new Scene(fullLayout,700,700);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            Main.window.setScene(scene);
            return;
        }

        this.language = grid.getLanguage();
        this.grid = grid;
        this.size = grid.getSize();
        this.secondsPassed = grid.getTime();

        loadScore();
        scoring.setText(score + "");

        timer.setText(convertTime(secondsPassed));

        timeHbox.getChildren().addAll(timerLabel, timer);
        scoreHbox.getChildren().addAll(scoreLabel, scoring);
        buttonHbox.getChildren().addAll(charHintButton, wordHintButton, exitButton, saveButton, gameName);

        timeHbox.setSpacing(10);
        scoreHbox.setSpacing(10);
        buttonHbox.setSpacing(10);

        timeHbox.setAlignment(Pos.CENTER);
        scoreHbox.setAlignment(Pos.CENTER);
        buttonHbox.setAlignment(Pos.CENTER);

        timeHbox.setOpaqueInsets(new Insets(20));
        scoreHbox.setOpaqueInsets(new Insets(20));
        buttonHbox.setOpaqueInsets(new Insets(20));

        infobar.getChildren().addAll(timeHbox, scoreHbox, buttonHbox);

        infobar.setSpacing(10);

        charHintButton.setOnAction(e -> charHint());
        wordHintButton.setOnAction(e -> wordHint(0));

        gameName.setVisible(false);
        saveButton.setOnAction(e -> {
            if(gameName.isVisible()) {
                saveGame(gameName.getText());
                gameName.setVisible(false);
            } else {
                gameName.setVisible(true);
            }

        });

        game = new Button[size][size];
        for( int i = 0; i < size ; i++ ) {
            game[i] = new Button[size];
            for ( int j = 0; j < size; j++ ) {

                game[i][j] = new Button();
                String text = grid.web[i][j].getInput() + "";
                if(!text.isEmpty()) {
                    game[i][j].setText(text);
                }
                //game[i][j].setText(grid.web[i][j].getCorrect()+"");
                game[i][j].setMinSize(100,100);
                game[i][j].setMaxSize(100,100);
                game[i][j].setStyle(defaultStyle);
                if(grid.web[i][j].getCorrect() == '*') {
                    game[i][j].setDisable(true);
                    game[i][j].setStyle("-fx-background-color:\n" +
                            "\t\t\tlinear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),\n" +
                            "\t\t\tlinear-gradient(#020b02, #3a3a3a),\n" +
                            "\t\t\tlinear-gradient(#010101 0%, #000000 20%, #000000 80%, #000000 100%),\n" +
                            "\t\t\tlinear-gradient(#000000 0%, #000000 50%, #000000 51%, #000000 100%);\n" +
                            "\t-fx-background-insets: 0,1,4,5;\n" +
                            "\t-fx-background-radius: 9,8,5,4;\n" +
                            "\t-fx-padding: 15 30 15 30;\n" +
                            "\t-fx-font-family: \"Helvetica\";\n" +
                            "\t-fx-font-size: 18px;\n" +
                            "\t-fx-font-weight: bold;\n" +
                            "\t-fx-text-fill: #333333;\n" +
                            "\t-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);");
                }

                int x = i, y = j;
                game[i][j].setOnAction(e -> { changeFocusTo(x,y); } );
                GridPane.setConstraints(game[i][j], j, i);
                webLayout.getChildren().add(game[i][j]);

            }
        }

        cluesVbox.getChildren().add(horizontalLabel);
        for(Map.Entry entry: grid.getHorizontal().entrySet()) {
            Button clue = new Button();
            entry.getKey();
            Word word = (Word)(entry.getValue());
            clue.setText(word.getClue());
            clue.setStyle(defaultStyle);
            if(language == Language.Arabic) {
                clue.setFont(Font.font("Arial"));
            }

            clue.setOnAction( e -> { selectWord((Integer)entry.getKey(), false); } );
            cluesVbox.getChildren().add(clue);
            horizontalWordButtons.put((Integer)entry.getKey(),clue);
        }

        cluesVbox.getChildren().add(verticalLabel);
        for(Map.Entry entry: grid.getVertical().entrySet()) {
            Button clue = new Button();
            entry.getKey();
            Word word = (Word)(entry.getValue());
            clue.setText(word.getClue());
            clue.setStyle(defaultStyle);
            if(language == Language.Arabic) {
                clue.setFont(Font.font("Arial"));
            }

            clue.setOnAction( e -> { selectWord((Integer)entry.getKey(), true); } );
            cluesVbox.getChildren().add(clue);
            verticalWordButtons.put((Integer)entry.getKey(),clue);
        }

        cluesVbox.setSpacing(5);

        if(score < 50) {
            charHintButton.setDisable(true);
            charHintButton.setStyle(disabledStyle);

            wordHintButton.setDisable(true);
            wordHintButton.setStyle(disabledStyle);
        } else if(score < 100) {
            wordHintButton.setDisable(true);
            wordHintButton.setStyle(disabledStyle);
        }

        if(language == Language.English  || grid.getLanguage() == Language.PerfectEnglish) {
            top.getChildren().addAll(webLayout, cluesVbox);
        } else if(language == Language.Arabic) {
            top.getChildren().addAll(cluesVbox, webLayout);
        }

        top.setSpacing(10);

        fullLayout.getChildren().addAll(top, infobar);

        fullLayout.setSpacing(10);

        focusI = 0;
        focusJ = 0;
        //nextAvailableTextField();

        scene = new Scene(fullLayout);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        scene.setOnKeyTyped(e->textChangedIn(e.getCharacter()));

        startTimer();
        Main.window.setScene(scene);
    }

    private void textChangedIn(String input) {

        if(input.isEmpty()) {
            return;
        }
        char letter = input.toUpperCase().charAt(0);
        setSquareAndCheckGame(letter);
        focus.setText(letter+"");
        nextAvailableTextField();
    }

    private void nextAvailableTextField() {

        if(language == Language.Arabic) {
            nextAvailableArabic();
            return;
        }

        int first = horizontal? focusJ : focusI;
        int second = horizontal? focusI : focusJ;
        Square standing;
        Button button;
        do {
            first++;
            if(first >= game.length) {
                first = 0;
//                if(second+1 < game.length) {
//                    second++;
//                }
                second = (second + 1) % grid.getSize();
            }
            standing = horizontal? grid.web[second][first] : grid.web[first][second];
            button = horizontal? game[second][first] : game[first][second];
            if (grid.getRemaining() == 0) {
                return;
            }
        } while(standing.getCorrect() == '*' || button.isDisabled());

        int resultI = horizontal? second : first;
        int resultJ = horizontal? first : second;
        changeFocusTo(resultI,resultJ);
    }

    private void nextAvailableArabic() {
        int i = focusI;
        int j = focusJ;

        if(horizontal) {
            do {
                j--;
                if(j < 0) {
                    j = grid.getSize() - 1;
                    i = (i + 1) % grid.getSize();
                }
            } while(grid.web[i][j].getCorrect() == '*' || game[i][j].isDisabled());
        } else {
            do {
                i++;
                if(i == grid.getSize()) {
                    i = 0;
                    j--;
                    if( j < 0 ) {
                        j = grid.getSize() - 1;
                    }
                }
            } while(grid.web[i][j].getCorrect() == '*' || game[i][j].isDisabled());
        }

        changeFocusTo(i,j);

    }

    private void selectWord(int key, boolean vertical) {
        if(wordHinting){
            horizontal = !vertical;
            wordHint(key);
            return;
        }
        HashMap words;
        int newWord;
        HashMap<Integer,Button> list;

        if (vertical) {
            words = grid.getVertical();

            if (verticalWordButtons.get(key) == selectedButton) {
                horizontal = !horizontal;
                newWord = grid.web[focusI][focusJ].getHorizontal();
                if(newWord != -1) {
                    changeSelectedButton(horizontalWordButtons.get(newWord));
                }
                return;
            }
            list = verticalWordButtons;
        } else {
            words = grid.getHorizontal();

            if (horizontalWordButtons.get(key) == selectedButton) {
                horizontal = !horizontal;
                newWord = grid.web[focusI][focusJ].getVertical();
                if(newWord != -1) {
                    changeSelectedButton(verticalWordButtons.get(newWord));
                }
                return;
            }
            list = horizontalWordButtons;
        }

        horizontal = !vertical;

        changeSelectedButton(list.get(key));

        Word word = (Word)(words.get(key));

        changeFocusTo(word.getiLocation(),word.getjLocation());
        if(game[focusI][focusJ].isDisabled()) {
            nextAvailableTextField();
        }
    }

    void gameOver() {
        addScore(grid.getSize() * 100);
        GameOverController goc = new GameOverController(grid.getSize(), timer.getText(), grid.getLanguage());
    }

    private void changeSelectedButton(Button newButton) {
        if(selectedButton!=null) {
            selectedButton.setStyle(defaultStyle);
        }
        selectedButton = newButton;
        selectedButton.setStyle(selectedStyle);
    }

    private void changeFocusTo(int i, int j) {
        if(focus == game[i][j]) {
            horizontal = !horizontal;
        }


        int wordNumber;
        HashMap<Integer,Button> wordButtonList;

        do
        {
            if (horizontal) {
                wordNumber = grid.web[i][j].getHorizontal();
                wordButtonList = horizontalWordButtons;
            }
            else {
                wordNumber = grid.web[i][j].getVertical();
                wordButtonList = verticalWordButtons;
            }
            if (wordNumber != -1) {
                changeSelectedButton(wordButtonList.get(wordNumber));
            } else {
                horizontal = !horizontal;
            }
        } while(wordNumber == -1);

        if(focus!=null) {
            focus.setStyle(oldFocusStyle);
        }

        focus = game[i][j];
        oldFocusStyle = focus.getStyle();
        focusI = i;
        focusJ = j;
        focus.setStyle(selectedStyle);
    }

    private void charHint() {
        if(focus == null) { return; }
        if(score >= 50) {
            score -= 50;
            scoring.setText("" + score);
        } else {
            charHintButton.setDisable(true);
            charHintButton.setStyle(disabledStyle);
            return;
        }

        if( score < 50 ) {
            charHintButton.setDisable(true);
            charHintButton.setStyle(disabledStyle);

            wordHintButton.setDisable(true);
            wordHintButton.setStyle(disabledStyle);
        }

        if(!focus.getText().isEmpty()) {
            if (focus.getText().charAt(0) == grid.web[focusI][focusJ].getCorrect()) {
                oldFocusStyle = correctStyle;
                focus.setDisable(true);
                nextAvailableTextField();
                return;
            }
        }
        focus.setText(grid.web[focusI][focusJ].getCorrect() + "");
        oldFocusStyle = correctStyle;
        focus.setDisable(true);
        setSquareAndCheckGame(grid.web[focusI][focusJ].getCorrect());
        nextAvailableTextField();
    }

    private void wordHint(int wordNumber) {
        HashMap<Integer, Word> list;
        Word word;

        if (!wordHinting) {
            wordHinting = true;
            wordHintButton.setStyle(selectedStyle);
            return;
        } else if(wordNumber == 0) {
            wordHinting = false;
            wordHintButton.setStyle(defaultStyle);
            return;
        } else {
            if(score >= 100) {
                score -= 100;
                scoring.setText(score + "");
            } else {
                wordHintButton.setDisable(true);
                wordHintButton.setStyle(disabledStyle);
                return;
            }

            if(score < 100) {
                wordHintButton.setDisable(true);
                wordHintButton.setStyle(disabledStyle);
            }

            wordHinting = false;
            wordHintButton.setStyle(defaultStyle);
            list = horizontal? grid.getHorizontal() : grid.getVertical();
            word = list.get(wordNumber);
            if(!horizontal) {
                for( int i = word.getiLocation(); i < word.getiLocation() + word.getWord().length(); i++) {

                    grid.revealSquare(i,word.getjLocation());
                    Square square = grid.web[i][word.getjLocation()];
                    Button button = game[i][word.getjLocation()];
                    button.setText(square.getCorrect() + "");
                    button.setStyle(correctStyle);
                    button.setDisable(true);

                    if (button == focus) {
                        oldFocusStyle = correctStyle;
                        nextAvailableTextField();
                    }

                    if(grid.getRemaining() == 0) {
                        gameOver();
                        break;
                    }
                }
            } else {
                if(language == Language.English  || grid.getLanguage() == Language.PerfectEnglish) {
                    for ( int j = word.getjLocation(); j < word.getjLocation() + word.getWord().length(); j++ ) {

                        grid.revealSquare(word.getiLocation(), j);
                        Square square = grid.web[word.getiLocation()][j];
                        Button button = game[word.getiLocation()][j];
                        button.setText(square.getCorrect() + "");
                        button.setStyle(correctStyle);
                        button.setDisable(true);
                        if ( button == focus ) {
                            oldFocusStyle = correctStyle;
                            nextAvailableTextField();
                        }

                        if ( grid.getRemaining() == 0 ) {
                            break;
                        }
                    }
                } else {
                    for(int j = word.getjLocation(); j > word.getjLocation() - word.getWord().length(); j--) {
                        grid.revealSquare(word.getiLocation(), j);
                        Square square = grid.web[word.getiLocation()][j];
                        Button button = game[word.getiLocation()][j];
                        button.setText(square.getCorrect() + "");
                        button.setStyle(correctStyle);
                        button.setDisable(true);
                        if ( button == focus ) {
                            oldFocusStyle = correctStyle;
                            nextAvailableTextField();
                        }

                        if ( grid.getRemaining() == 0 ) {
                            break;
                        }
                    }
                }
            }
        }
    }

    private void saveGame(String name) {
        grid.setTime(secondsPassed);
        try {
            SaveHandler.save(grid, name);
        } catch (ClassNotFoundException e1) {
        } catch (IOException e1) {
        }
    }

    private void setSquareAndCheckGame(char c) {
        grid.setSquare(focusI, focusJ,c);
        if(grid.getRemaining() == 0) {
            gameOver();
        }
    }

    private void startTimer() {
        Runnable updating = new Runnable() {
            public int i = secondsPassed;
            @Override
            public void run(){
                timer.setText(convertTime(i));
                i++;
                secondsPassed = i;
            }
        };
        timeTimer = new Timer(updating);
        Thread t = new Thread(timeTimer);
        t.setDaemon(true);
        t.start();
    }

    private String convertTime(int seconds) {
        int hours = seconds / 3600;
        seconds = seconds % 3600;
        int minutes = seconds / 60;
        seconds = seconds % 60;

        String hString = hours < 10? "0" + hours : hours + "";
        String mString = minutes < 10? "0" + minutes : minutes + "";
        String sString = seconds < 10? "0" + seconds : seconds + "";

        if(hours > 0) {
            return hString + ":" + mString + ":" + sString;
        } else {
            return mString + ":" + sString;
        }
    }

    private void addScore(int amount) {
        DataOutputStream output = null;
        score += amount;
        try {
            output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Score")));
            output.writeInt(score);
        } catch ( IOException e ) {

        } finally {
            try {
                if(output != null) {
                    output.close();
                }
            } catch ( IOException e ) {}
        }


    }

    private void loadScore() {
        DataInputStream input = null;
        try {
            input = new DataInputStream(new BufferedInputStream(new FileInputStream("Score")));
            score = input.readInt();
        } catch ( IOException e ) {

        } finally {
            try {
                if(input != null) {
                    input.close();
                }
            } catch ( IOException e ) {}
        }
    }
}
