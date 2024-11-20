package Class.DevMode.Edit;

import Class.Item.Item;
import Class.Map.Map;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.List;

public class ImgMouseControler {
    private final List<ImageView> imgMode = List.of(
            new ImageView("file:assets/interact/mouse/add.png"),
            new ImageView("file:assets/interact/mouse/move.png")
    );
    private int mode = 0;
    private final ImageView bar = new ImageView("file:assets/interact/mouse/bar.png");
    private final List<ImageView> allImgItem = List.of(
            new ImageView("file:assets/items/pc.png"),
            new ImageView("file:assets/items/canapplusgros.png"),
            new ImageView("file:assets/items/canapplusgros2.png"),
            new ImageView("file:assets/items/distributeur.png"),
            new ImageView("file:assets/items/table1.png"),
            new ImageView("file:assets/items/table5.png")
    );
    private final ImageView imgSelected = new ImageView("file:assets/interact/mouse/selected.png");
    private int selected = 0;

    public ImgMouseControler() {
        for (ImageView img : imgMode) {
            img.setFitWidth(20);
            img.setFitHeight(20);
        }

        StackPane.setAlignment(bar, Pos.BOTTOM_LEFT);

        for (ImageView img : allImgItem) {
            img.setFitWidth(40);
            img.setFitHeight(40);

            StackPane.setAlignment(img, Pos.BOTTOM_CENTER);
            img.setTranslateX(-allImgItem.size() * 20 + (40 * allImgItem.indexOf(img) * 2));
            img.setTranslateY(-20);
        }

        StackPane.setAlignment(imgSelected, Pos.BOTTOM_CENTER);
        imgSelected.setTranslateX(-allImgItem.size() * 20);
        imgSelected.setTranslateY(-10);
    }

    public int getMode() {
        return mode;
    }

    public int getSelected() {
        return selected;
    }

    public List<ImageView> getAllImgItem() {
        return allImgItem;
    }

    public void changeSelected(int selected) {
        this.selected = selected;

        imgSelected.setTranslateX(-allImgItem.size() * 20 + (40 * selected * 2));
    }

    public void setMode(int mode, StackPane gameView) {
        this.mode = mode;

        for (int i = 0; i < imgMode.size(); i++) {
            if (i == mode)
                continue;
            else {
                gameView.getChildren().remove(imgMode.get(i));
                gameView.getChildren().remove(bar);

                for (ImageView img : allImgItem) {
                    gameView.getChildren().remove(img);
                }

                gameView.getChildren().remove(imgSelected);
            }
        }
    }

    private void actionOnAdd(double x, double y, StackPane gameView, Map map) {
        double fx = x - gameView.getWidth() / 2;
        double fy = y - gameView.getHeight() / 2;
        javafx.geometry.Point2D mouse = map.getMapContainer().sceneToLocal(x, y);

        if (!gameView.getChildren().contains(bar))
            gameView.getChildren().add(bar);

        for (ImageView img : allImgItem) {
            if (!gameView.getChildren().contains(img)) {
                gameView.getChildren().add(img);
            }
        }

        if (!gameView.getChildren().contains(imgSelected))
            gameView.getChildren().add(imgSelected);
    }

    public void displayImg(double x, double y, StackPane gameView, Map map) {
        double fx = x - gameView.getWidth() / 2;
        double fy = y - gameView.getHeight() / 2;
        imgMode.get(mode).setTranslateX(fx + 10);
        imgMode.get(mode).setTranslateY(fy -10);

        javafx.geometry.Point2D mouse = map.getMapContainer().sceneToLocal(x, y);

        if (mode == 1) {
            map.getItems().forEach(item -> {
                if (item.getHitbox().contains(mouse)) {
                    item.getHitbox().setStrokeWidth(2);
                    item.getHitbox().setStroke(javafx.scene.paint.Color.RED);
                } else {
                    item.getHitbox().setStrokeWidth(0);
                }
            });
        } else if (mode == 0) {
            actionOnAdd(x, y, gameView, map);
        }

        if (!gameView.getChildren().contains(imgMode.get(mode)))
            gameView.getChildren().add(imgMode.get(mode));
    }

    public void removeImg(StackPane gameView) {
        gameView.getChildren().remove(imgMode.get(mode));
        gameView.getChildren().remove(bar);

        for (ImageView img : allImgItem) {
            gameView.getChildren().remove(img);
        }

        gameView.getChildren().remove(imgSelected);
    }
}
