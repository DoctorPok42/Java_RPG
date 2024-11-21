package Class.DevMode.Text;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

public class Collisions extends Texts implements TextInterface {
    public Collisions(String name, String content) {
        super(name, content, KeyCode.C);
    }

    @Override
    public void displayText(StackPane gameView) {
        content.setStyle(styles);
        content.setTranslateY(30);
        gameView.getChildren().add(content);
    }

    @Override
    public void updateText(boolean display) {
        content.setText("Collisions: " + display + " (c)");
        this.display = display;
    }
}
