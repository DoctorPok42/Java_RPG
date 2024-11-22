package Class.DevMode.Text;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

public class Place extends Texts implements TextInterface {
    public Place(String name, String content) {
        super(name, content, KeyCode.T, false);
    }

    @Override
    public void displayText(StackPane gameView) {
        content.setStyle(styles);
        content.setTranslateY(75);
        gameView.getChildren().add(content);
    }

    @Override
    public void updateText(boolean display) {
        content.setText("Edit mod: " + display + " (t)");
        this.display = display;
    }
}
