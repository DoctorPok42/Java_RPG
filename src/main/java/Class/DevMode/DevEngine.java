package Class.DevMode;

import Class.Character.Player;
import Class.DevMode.Edit.ImgMouseControler;
import Class.DevMode.Edit.Replace.Replace;
import Class.DevMode.Text.Collisions;
import Class.DevMode.Text.Interact;
import Class.DevMode.Text.Place;
import Class.DevMode.Text.Texts;
import Class.Map.Map;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class DevEngine {
    private final KeyControler keyControler;
    private Player player;
    private Map map;
    private StackPane gameView;
    private boolean isEditing = false;
    private final Collisions textCollisions;
    private final Interact textInteract;
    private final Place textPlace;
    private final ImgMouseControler imgMouseControler;
    private final Replace replaceAction = new Replace();
    private boolean isAdding = true;
    private final List<KeyCode> keysPressed = new ArrayList<>();

    public DevEngine() {
        textCollisions = new Collisions("Collisions", "Collisions: true (c)");
        textInteract = new Interact("Interact", "Interact hitBox: false (i)");
        textPlace = new Place("Place", "Edit mod: false (t)");
        List<KeyCode> keys = Texts.getAllKeyListeners();
        keys.add(KeyCode.CONTROL);
        keys.add(KeyCode.Z);
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
        if (keyPress == null) {
            return;
        }

        if (keyPress == KeyCode.CONTROL) {
            keysPressed.add(KeyCode.CONTROL);
        }

        if (keyPress == KeyCode.Z && keysPressed.contains(KeyCode.CONTROL)) {
            keysPressed.clear();
            replaceAction.undo();
        }

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
            imgMouseControler.displayImg(e.getX(), e.getY(), gameView, map, replaceAction);
        } else {
            imgMouseControler.removeImg(gameView);
        }
    }

    public void listenMouseClick(MouseEvent e, Point2D clickPoint) {
        boolean isLeftClick = e.getButton() == MouseButton.PRIMARY;
        if (isEditing) {
            if (isAdding) {
                imgMouseControler.putItem(map, clickPoint);
            } else {
                if (isLeftClick) {
                    replaceAction.selectItem(map, clickPoint);
                } else {
                    replaceAction.saveInJson();
                }
            }
        }
    }

    public void listenScroll(ScrollEvent e, double deltaY, boolean isAltPressed) {
        if (!isAltPressed) {
            imgMouseControler.setMode((imgMouseControler.getMode() + 1) % 2, gameView);

            this.isAdding = imgMouseControler.getMode() == 0;
        } else if (isEditing && isAdding) {
            if (deltaY > 0) {
                imgMouseControler.changeSelected((imgMouseControler.getSelected() - 1 + imgMouseControler.getAllImgItem().size()) % imgMouseControler.getAllImgItem().size());
            } else {
                imgMouseControler.changeSelected((imgMouseControler.getSelected() + 1) % imgMouseControler.getAllImgItem().size());
            }
            imgMouseControler.displayItemSelected(e.getX(), e.getY(), gameView);
        }
    }

    public void listenMouseDrag(MouseEvent e) {
        if (isEditing && !isAdding) {
            double dragX = e.getSceneX();
            double dragY = e.getSceneY();
            javafx.geometry.Point2D dragPoint = map.getMapContainer().sceneToLocal(dragX, dragY);
            replaceAction.drag(dragPoint);
        }
    }
}
