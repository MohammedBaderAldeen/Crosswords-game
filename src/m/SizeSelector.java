package m;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.Random;

public class SizeSelector
{
    Button fourByFour = new Button("4x4");
    Button fiveByFive = new Button("5x5");
    Button sevenBySeven = new Button("7x7");
    Button back = new Button("Back");

    VBox layout = new VBox();

    Scene scene;

    public SizeSelector() {
        fiveByFive.setMinSize(500,50);
        fourByFour.setMinSize(500,50);
        sevenBySeven.setMinSize(500,50);
        back.setMinSize(500,50);

        layout.getChildren().addAll(fourByFour,fiveByFive,sevenBySeven,back);
        layout.setSpacing(20);
        layout.setAlignment(Pos.CENTER);

        scene = new Scene(layout,700,700);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    }

    public void show(Language language) {

        if(language == Language.PerfectEnglish) {
            fourByFour.setDisable(true);
            fourByFour.setStyle("-fx-opacity: 0.4");
            sevenBySeven.setDisable(true);
            sevenBySeven.setStyle("-fx-opacity: 0.4");
        }

        fourByFour.setOnAction( e -> { start(4,language); } );
        fiveByFive.setOnAction( e -> { start(5,language); } );
        sevenBySeven.setOnAction( e -> { start(7, language); } );
        back.setOnAction(e -> { Main.languageSelector.show(); });

        Main.window.setScene(scene);
    }

    public static void start(int size, Language language) {

        //Algorithm
        Grid grid = GridGenerator.generate(size,language);

        GameController gc = new GameController(grid);

    }
}
