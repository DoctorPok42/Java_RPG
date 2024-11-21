package Class.DevMode.Text;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

public class Interact extends Texts implements TextInterface {
    public Interact(String name, String content) {
        super(name, content, KeyCode.I, false);
    }

    @Override
    public void displayText(StackPane gameView) {
        content.setStyle(styles);
        content.setTranslateY(30);
        gameView.getChildren().add(content);
    }

    @Override
    public void updateText(boolean display) {
        content.setText("Interact hitBox: " + display + " (i)");
        this.display = display;
    }
}
