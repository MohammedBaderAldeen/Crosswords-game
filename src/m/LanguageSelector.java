package m;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class LanguageSelector
{
    private Button arabic = new Button("Arabic");
    private Button english = new Button("English");
    private Button perfect = new Button("Perfect");
    private Button back = new Button("Back");

    private VBox layout = new VBox();

    private Scene scene;

    public LanguageSelector() {
        arabic.setMinSize(500,50);
        english.setMinSize(500,50);
        perfect.setMinSize(500,50);
        back.setMinSize(500,50);

        layout.getChildren().addAll(arabic,english,perfect,back);
        layout.setSpacing(20);
        layout.setAlignment(Pos.CENTER);

        arabic.setOnAction( e -> { Main.sizeSelector.show(Language.Arabic); } );
        english.setOnAction( e -> { Main.sizeSelector.show(Language.English); } );
        perfect.setOnAction( e -> { Main.sizeSelector.show(Language.PerfectEnglish); });
        back.setOnAction( e ->{ Main.show(); } );

        scene = new Scene(layout,700,700);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    }

    public void show() { Main.window.setScene(scene); }

}
