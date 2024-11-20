package Class.DevMode;

import Class.Character.Player;
import Class.DevMode.Edit.ImgMouseControler;
import Class.DevMode.Text.Collisions;
import Class.DevMode.Text.Interact;
import Class.DevMode.Text.Place;
import Class.DevMode.Text.Texts;
import Class.Map.Map;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;

public class DevEngine {
    private final KeyControler keyControler;
    private Player player;
    private Map map;
    private StackPane gameView;
    private final List<KeyCode> keys;
    private boolean isEditing = false;
    private final Collisions textCollisions;
    private final Interact textInteract;
    private final Place textPlace;
    public final ImgMouseControler imgMouseControler;

    public DevEngine() {
        textCollisions = new Collisions("Collisions", "Collisions: true (c)");
        textInteract = new Interact("Interact", "Interact hitBox: false (i)");
        textPlace = new Place("Place", "Edit mod: false (t)");
        keys = Texts.getAllKeyListeners();
        keyControler = new KeyControler("KeyControler", keys);
        imgMouseControler = new ImgMouseControler();
    }

    public void displayDevMode(StackPane gameView, Player player, Map map) {
        this.player = player;
        this.map = map;
        this.gameView = gameView;
        textCollisions.displayText(gameView);
        textInteract.displayText(gameView);
        textPlace.displayText(gameView);
    }

    public void listenKey(KeyEvent e) {
        KeyCode keyPress = keyControler.listenKey(e);
        if (keyPress == KeyCode.C) {
            textCollisions.updateText(!textCollisions.getDisplay());
            player.setIsCollision(textCollisions.getDisplay());
        }

        if (keyPress == KeyCode.I) {
            textInteract.updateText(!textInteract.getDisplay());
            map.getItems().forEach(item ->
                    item.getInteractionHitbox().setStroke(textInteract.getDisplay() ? Color.RED : Color.TRANSPARENT)
            );
        }

        if (keyPress == KeyCode.T) {
            textPlace.updateText(!textPlace.getDisplay());
            isEditing = textPlace.getDisplay();

            if (!isEditing) {
                imgMouseControler.removeImg(gameView);
            }
        }
    }

    public void listenMouse(MouseEvent e) {
        if (isEditing) {
            imgMouseControler.displayImg(e.getX(), e.getY(), gameView, map);
        } else {
            imgMouseControler.removeImg(gameView);
        }
    }
}
