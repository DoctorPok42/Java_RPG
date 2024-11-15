package Class.bar;

import Class.Character.Player;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Tiredness extends bar {
    public Tiredness(String name) {
        super(name, new ImageView("file:assets/bar/energy.png"));
        this.texture.setTranslateY(-5);

        this.bar.setFill(Color.web("#ffba08"));
        this.bar.setTranslateY(-28);
    }

    public void update(Player player, StackPane gameView) {
        this.bar.setWidth(player.getWeakness());
    }
}
