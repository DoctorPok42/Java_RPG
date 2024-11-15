package Class.bar;

import Class.Character.Player;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Feed extends bar {
    public Feed(String name) {
        super(name);
        this.texture = new ImageView("file:assets/bar/health.png");
        this.texture.setFitWidth(187.6);
        this.texture.setFitHeight(60.4);
        StackPane.setAlignment(texture, Pos.BOTTOM_LEFT);
        this.texture.setTranslateX(5);
        this.texture.setTranslateY(-65);

        this.bar = new Rectangle(0, -30, 115, 14.5);
        this.bar.setFill(Color.web("#cb455e"));
        StackPane.setAlignment(bar, Pos.BOTTOM_LEFT);
        this.bar.setTranslateX(60);
        this.bar.setTranslateY(-88);
    }

    public void display(StackPane gameView) {
        super.display(gameView);
    }

    public void update(Player player, StackPane gameView) {
        this.bar.setWidth(player.getHunger());
    }
}
