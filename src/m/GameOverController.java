package m;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.*;

public class GameOverController {
    int size;
    String time;
    Language language;
    Label header;
    Button playAgainButton = new Button("Play Again");
    Button exitButton = new Button("Exit");

    VBox layout = new VBox();
    Scene scene;

    public GameOverController(int size, String time, Language language) {
        this.size = size;
        this.time = time;
        this.language = language;

        header = new Label("YOU WON" + "\n" + "You won a " + size + "x" + size + " game in " + time);

        playAgainButton.setMinSize(500,50);
        exitButton.setMinSize(500,50);

        playAgainButton.setOnAction(e -> { SizeSelector.start(this.size,this.language); } );
        exitButton.setOnAction( e ->  Main.show() );

        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(20);
        layout.getChildren().addAll(header, playAgainButton, exitButton);

        scene = new Scene(layout,700,700);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        this.show();
    }

    public void show() {
        Main.window.setFullScreen(false);
        Main.window.setScene(scene);
    }


}
