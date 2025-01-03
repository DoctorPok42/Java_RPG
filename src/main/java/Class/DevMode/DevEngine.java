package Class.DevMode;

import Class.Character.Player;
import Class.DevMode.Edit.ImgMouseControler;
import Class.DevMode.Edit.Replace;
import Class.DevMode.Edit.Utils.ReadItemFile;
import Class.DevMode.Text.*;
import Class.Item.Item;
import Class.Item.ItemTypeAdapter;
import Class.Map.Map;
import com.google.gson.GsonBuilder;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;

import java.io.IOException;
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
    private final Cords textCords;
    private final ImgMouseControler imgMouseControler;
    private final Replace replaceAction;
    private boolean isAdding = true;
    private final List<KeyCode> keysPressed = new ArrayList<>();
    private Point2D mousePoint = new Point2D(0, 0);
    private final ImageView textBlock = new ImageView("file:assets/menu/creative/block.png");
    private final ReadItemFile readItemFile;

    public DevEngine() throws IOException {
        textCollisions = new Collisions("Collisions", "Collisions: true (c)");
        textInteract = new Interact("Interact", "Interact hitBox: false (i)");
        textPlace = new Place("Place", "Edit mod: false (t)");
        textCords = new Cords("Cords", "Cords: 0, 0");
        String filePath = "./data/items.json";
        readItemFile = new ReadItemFile(filePath);
        List<KeyCode> keys = Texts.getAllKeyListeners();
        keys.add(KeyCode.CONTROL);
        keys.add(KeyCode.Z);
        keys.add(KeyCode.DELETE);
        keyControler = new KeyControler("KeyControler", keys);
        imgMouseControler = new ImgMouseControler(filePath);
        replaceAction = new Replace(filePath);

        StackPane.setAlignment(textBlock, Pos.TOP_LEFT);
        textBlock.setTranslateX(10);
        textBlock.setTranslateY(10);
    }

    public void displayDevMode(StackPane gameView, Player player, Map map) {
        this.player = player;
        this.map = map;
        this.gameView = gameView;
        gameView.getChildren().add(textBlock);
        textCollisions.displayText(gameView);
        textInteract.displayText(gameView);
        textPlace.displayText(gameView);
    }

    public void listenKey(KeyEvent e) {
        KeyCode keyPress = keyControler.listenKey(e);
        if (keyPress == null) {
            return;
        }

        if (keyPress == KeyCode.DELETE && isEditing && !isAdding) {
            replaceAction.deleteItem(map, mousePoint);
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
                textCords.removeText(gameView);
                textBlock.setFitHeight(90);
            } else {
                textCords.displayText(gameView);
                textBlock.setFitHeight(120);
            }
        }
    }

    public void listenMouse(MouseEvent e) {
        this.mousePoint = map.getMapContainer().sceneToLocal(e.getSceneX(), e.getSceneY());

        if (isEditing) {
            imgMouseControler.displayImg(e.getX(), e.getY(), gameView, map, replaceAction);

            textCords.updateText((int) mousePoint.getX() + ", " + (int) mousePoint.getY());
        } else {
            imgMouseControler.removeImg(gameView);
        }
    }

    public void listenMouseClick(MouseEvent e, Point2D clickPoint) throws IOException {
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

            List<Item> items = readItemFile.readItems(new GsonBuilder().registerTypeAdapter(Item.class, new ItemTypeAdapter()).create());
            imgMouseControler.setItemField(items);
            replaceAction.setItemField(items);
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
        imgMouseControler.displayImg(e.getX(), e.getY(), gameView, map, replaceAction);
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
