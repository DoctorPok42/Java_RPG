package Class.bar;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import java.util.Objects;

public class bar {
    protected String name;
    protected ImageView texture;
    protected Rectangle bar;

    public bar(String name, ImageView texture) {
        this.name = name;
        this.texture = texture;

        if (!Objects.equals(name, "Time")) {
            this.texture.setFitWidth(187.6);
            this.texture.setFitHeight(60.4);
            StackPane.setAlignment(texture, Pos.BOTTOM_LEFT);
            this.texture.setTranslateX(5);
            this.bar = new Rectangle(0, -30, 115, 14.5);

            StackPane.setAlignment(bar, Pos.BOTTOM_LEFT);
            this.bar.setTranslateX(60);
        }
    }

    public String getName() {
        return name;
    }

    public void display(StackPane gameView) {
        gameView.getChildren().add(texture);
        gameView.getChildren().add(bar);
    }
}
