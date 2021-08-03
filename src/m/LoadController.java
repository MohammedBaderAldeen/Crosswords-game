package m;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class LoadController {

    Label header = new Label("Select the game you wish to load:");
    ArrayList<Button> gameNamesButtons = new ArrayList<>();
    ArrayList<String> gameNames = new ArrayList<>();
    Button backButton = new Button("Back");

    VBox vbox = new VBox();
    ScrollPane scrollPane = new ScrollPane(vbox);
    Scene scene;

    public LoadController(String type) {

        SaveHandler.setFilename(type);

        backButton.setMinSize(500,50);
        backButton.setMaxSize(500,50);
        backButton.setOnAction( e ->{ Main.window.setScene(Main.scene); } );


        try {
            gameNames = SaveHandler.getGameNames();
        } catch (ClassNotFoundException e) {
            header.setText("File Corrupted.");
        } catch (IOException e) {
            header.setText("Error while reading file.");
        }

        if(gameNames.size() == 0) {
            header.setText("You didn't save any game.");
            vbox.getChildren().addAll(header,backButton);
        } else {
            vbox.getChildren().add(header);
            for(int i = 0; i < gameNames.size(); i++) {
                Button button = new Button(gameNames.get(i));
                button.setMinSize(500,50);
                button.setMaxSize(500,50);
                int x = i;
                button.setOnAction(e-> loadGame(x));
                gameNamesButtons.add(button);
                vbox.getChildren().add(button);
            }
            vbox.getChildren().add(backButton);
        }

        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        scene = new Scene(scrollPane,700,700);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

    }

    public void show() {
        Main.window.setScene(scene);
    }

    private void loadGame(int i) {
        try {
            Grid game = SaveHandler.getGame(gameNames.get(i));
            GameController gc = new GameController(game);
        } catch (IOException e) {
            header.setText("Error while reading file.");
            errorLoading();
        } catch (ClassNotFoundException e) {
            header.setText("Corrupted file.");
            errorLoading();
        }

    }

    private void errorLoading() {
        vbox.getChildren().removeAll(gameNamesButtons);
    }
}
