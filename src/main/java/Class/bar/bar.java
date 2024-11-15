package Class.bar;

import Class.Character.Player;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class bar {
    protected String name;
    protected ImageView texture;
    protected Rectangle bar;

    public bar(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void display(StackPane gameView) {
        gameView.getChildren().add(texture);
        gameView.getChildren().add(bar);
    }

    public void update(Player player, StackPane gameView) {
    }
}
