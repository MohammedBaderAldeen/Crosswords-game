package m;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {

    public static Stage window;
    public static Scene scene;
    public static LanguageSelector languageSelector ;
    public static SizeSelector sizeSelector ;
    public static LoadController loadController;

    public static void main(String[] args) throws Exception {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Crossword Puzzles");


        Button playButton = new Button("Play");
        Button loadButton = new Button("Load");
        Button prebuiltButton = new Button("Prebuilt");
        Button exitButton = new Button("Exit");


        playButton.setMinSize(500,50);
        loadButton.setMinSize(500,50);
        prebuiltButton.setMinSize(500,50);
        exitButton.setMinSize(500,50);


        playButton.setOnAction(e -> { languageSelector = new LanguageSelector();
            sizeSelector = new SizeSelector();
            languageSelector.show(); } );
        loadButton.setOnAction(e -> {
            loadController = new LoadController("Saved");
            loadController.show();
        });
        prebuiltButton.setOnAction(e -> {
            loadController = new LoadController("Prebuilt");
            loadController.show();
        });
        exitButton.setOnAction(e -> window.close());

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        vbox.getChildren().addAll(playButton,loadButton,prebuiltButton,exitButton);
        scene = new Scene(vbox,700,700);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        window.setResizable(false);
        window.setScene(scene);
        window.show();
    }

    public static void show() { window.setScene(scene); }

}
