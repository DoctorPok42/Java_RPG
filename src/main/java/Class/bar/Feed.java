package Class.bar;

import Class.Character.Player;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Feed extends bar {
    public Feed(String name) {
        super(name, new ImageView("file:assets/bar/health.png"));
        this.texture.setTranslateY(-65);

        this.bar.setFill(Color.web("#cb455e"));
        this.bar.setTranslateY(-88);
    }

    public void update(Player player, StackPane gameView) {
        this.bar.setWidth(player.getHunger());
    }
}
