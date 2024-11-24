package Class.DevMode.Text;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

public class Cords extends Texts{
    public Cords(String name, String content) {
        super(name, content, KeyCode.T, false);
    }

    public void displayText(StackPane gameView) {
        content.setStyle(styles);
        content.setTranslateY(100);
        gameView.getChildren().add(content);
    }

    public void updateText(String display) {
        content.setText("Cords: " + display);
    }

    public void removeText(StackPane gameView) {
        gameView.getChildren().remove(content);
    }
}
