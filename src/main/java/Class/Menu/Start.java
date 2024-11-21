package Class.Menu;

import Class.Music.Music;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

public class Start extends Menu {
    private final ImageView titleScene = new ImageView("file:assets/menu/RPG_title.png");
    private final ImageView background = new ImageView("file:assets/menu/background.png");
    private final ImageView cursor = new ImageView("file:assets/menu/cursor.png");
    private final Music music = new Music("assets/music/menu.wav", 0.3);
    private final Font font = Font.loadFont("file:assets/font/PressStart2P-Regular.ttf", 40);
    private final List<ImageView> back = new ArrayList<>();

    public Start() {
        super("Start", "Start Menu",0);

        this.options = List.of(
            new Option("newGame", "Start New Game", System::console),
            new Option("newDevGame", "Dev Mode", System::console),
            new Option("exit", "Exit", () -> System.exit(0))
        );

        StackPane.setAlignment(this.titleScene, Pos.TOP_CENTER);
        this.titleScene.setTranslateY(50);

        this.background.setFitWidth(1280);
        this.background.setFitHeight(720);
        this.selectedOption = 0;

        for (int i = 1; i < 10; i++) {
            ImageView img = new ImageView("file:assets/menu/parallax/menu_" + i + ".png");
            img.setFitWidth(1280);
            img.setFitHeight(720);

            this.back.add(img);
        }
    }

    @Override
    public int selectOption() {
        this.options.get(this.selectedOption).getAction().run();

        if (this.selectedOption == 0) {
            music.stop();
        }

        return this.selectedOption + 1;
    }

    public void up() {
        if (this.selectedOption > 0) {
            this.selectedOption--;
        } else {
            this.selectedOption = this.options.size() - 1;
        }

        this.displayCursor();
    }

    public void down() {
        if (this.selectedOption >= 0 && this.selectedOption < this.options.size() - 1) {
            this.selectedOption++;
        } else {
            this.selectedOption = 0;
        }

        this.displayCursor();
    }

    public void show(StackPane gameView) {
        gameView.getChildren().clear();
        gameView.getChildren().add(background);

        for (ImageView imageView : this.back) {
            gameView.getChildren().add(imageView);
        }

        gameView.getChildren().add(titleScene);

        // play the media
//        music.play();

        // display option
        for (int i = 0; i < this.options.size(); i++) {
            Text text = new Text(this.options.get(i).getName());
            text.setFill(Color.WHITE);
            text.setStyle(
                "-fx-font-size: 40;" +
                "-fx-font-family: 'Press Start 2P';" +
                "-fx-font-weight: bold;"
            );
            StackPane.setAlignment(text, Pos.TOP_CENTER);
            text.setTranslateY(380 + i * 100);
            gameView.getChildren().add(text);
        }

        // display cursor
        this.cursor.setFitWidth(50);
        this.cursor.setFitHeight(50);
        StackPane.setAlignment(cursor, Pos.BASELINE_CENTER);
        cursor.setTranslateX(330);
        cursor.setTranslateY(440 + this.selectedOption * 100);
        gameView.getChildren().add(cursor);
    }

    public void displayCursor() {
        cursor.setTranslateY(440 + this.selectedOption * 100);
    }
}
