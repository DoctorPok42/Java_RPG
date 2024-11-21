package Class.DevMode.Edit;

import Class.DevMode.Edit.Utils.ReadItemFile;
import Class.Item.*;
import Class.Map.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.Writer;
import java.nio.file.Files;
import java.util.List;

public class ImgMouseControler {
    private final List<ImageView> imgMode = List.of(
            new ImageView("file:assets/interact/mouse/add.png"),
            new ImageView("file:assets/interact/mouse/move.png")
    );
    private int mode = 0;
    private final ImageView bar = new ImageView("file:assets/interact/mouse/bar.png");
    private final List<EditItems> allImgItem = List.of(
        new EditItems(new ImageView("file:assets/items/pc.png"), ItemType.PC, "PC", 3),
        new EditItems(new ImageView("file:assets/items/canapplusgros.png"), ItemType.CANAP, "CANAP", 1),
        new EditItems(new ImageView("file:assets/items/canapplusgros2.png"), ItemType.CANAP, "CANAP", 2),
        new EditItems(new ImageView("file:assets/items/distributeur.png"), ItemType.DISTRIBUTOR, "DISTRIBUTOR", 0),
        new EditItems(new ImageView("file:assets/items/table3.png"), ItemType.TABLE, "Table", 3),
        new EditItems(new ImageView("file:assets/items/table5.png"), ItemType.TABLE, "Table", 5)
    );
    private final ImageView imgSelected = new ImageView("file:assets/interact/mouse/selected.png");
    private int selected = 0;
    private final ImageView itemToDisplay = new ImageView();
    private final ReadItemFile readItemFile;

    public ImgMouseControler(String filePath) {
        for (ImageView img : imgMode) {
            img.setFitWidth(20);
            img.setFitHeight(20);
        }

        StackPane.setAlignment(bar, Pos.BOTTOM_LEFT);

        for (ImageView img : EditItems.getAllItemsView()) {
            img.setFitWidth(40);
            img.setFitHeight(40);

            StackPane.setAlignment(img, Pos.BOTTOM_CENTER);
            img.setTranslateX(-allImgItem.size() * 20 + (40 * allImgItem.indexOf(allImgItem.get(EditItems.getAllItemsView().indexOf(img))) * 2));
            img.setTranslateY(-20);
        }

        StackPane.setAlignment(imgSelected, Pos.BOTTOM_CENTER);
        imgSelected.setTranslateX(-allImgItem.size() * 20);
        imgSelected.setTranslateY(-9);

        readItemFile = new ReadItemFile(filePath);
    }

    public int getMode() {
        return mode;
    }

    public int getSelected() {
        return selected;
    }

    public List<ImageView> getAllImgItem() {
        return EditItems.getAllItemsView();
    }

    public void changeSelected(int selected) {
        this.selected = selected;

        imgSelected.setTranslateX(-allImgItem.size() * 20 + (40 * selected * 2));
    }

    public void setMode(int mode, StackPane gameView) {
        this.mode = mode;

        for (int i = 0; i < imgMode.size(); i++) {
            if (i != mode) {
                gameView.getChildren().remove(imgMode.get(i));
                gameView.getChildren().remove(bar);

                for (ImageView img : EditItems.getAllItemsView()) {
                    gameView.getChildren().remove(img);
                }

                gameView.getChildren().remove(imgSelected);

                gameView.getChildren().remove(itemToDisplay);
            }
        }
    }

    public void displayItemSelected(double x, double y, StackPane gameView) {
        double fx = x - gameView.getWidth() / 2;
        double fy = y - gameView.getHeight() / 2;

        gameView.getChildren().remove(itemToDisplay);

        itemToDisplay.setImage(allImgItem.get(selected).getImg().getImage());
        itemToDisplay.setTranslateX(fx);
        itemToDisplay.setTranslateY(fy);

        itemToDisplay.setOpacity(0.5);

        if (!gameView.getChildren().contains(itemToDisplay))
            gameView.getChildren().add(itemToDisplay);
    }

    public void putItem(Map map, Point2D clickPoint) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Item.class, new ItemTypeAdapter()).setPrettyPrinting().create();

        try {
            List<Item> itemList = readItemFile.readItems(gson);

            int lastId = 0;
            if (!itemList.isEmpty()) {
                lastId = itemList.getLast().getId();
            }

            double itemX = clickPoint.getX() - itemToDisplay.getImage().getWidth() / 2;
            double itemY = clickPoint.getY() - itemToDisplay.getImage().getHeight() / 2;

            Item item = new Item(allImgItem.get(selected).getName(), allImgItem.get(selected).getType(), (float) itemX, (float) itemY, 1, allImgItem.get(selected).getSkin(), lastId + 1);
            itemList.add(item);

            Writer writer = Files.newBufferedWriter(readItemFile.getItemFilePath());
            gson.toJson(itemList, writer);
            writer.close();

            Item items =
                    switch ((ItemType) item.getType()) {
                        case PC -> new Pc(item.getName(), item.getType(), item.getX(), item.getY(), item.getZ(), item.getSkin(), lastId + 1);
                        case CANAP -> new Canap(item.getName(), item.getType(), item.getX(), item.getY(), item.getZ(), item.getSkin(), lastId + 1);
                        case DISTRIBUTOR -> new Distributor(item.getName(), item.getType(), item.getX(), item.getY(), item.getZ(), 0, item.getSkin(), lastId + 1);
                        case TABLE -> new Item(item.getName(), item.getType(), item.getX(), item.getY(), item.getZ(), item.getSkin(), lastId + 1);
                        default -> null;
                    };

            map.getItems().add(items);
            map.getItems().getLast().getItemView().toFront();

            assert items != null;
            map.getMapContainer().getChildren().add(items.getInteractionHitbox());
            map.getMapContainer().getChildren().add(items.getHitbox());

            items.getItemView().setLayoutX(itemX);
            items.getItemView().setLayoutY(itemY);
            map.getMapContainer().getChildren().add(items.getItemView());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void actionOnAdd(double x, double y, StackPane gameView) {
        if (!gameView.getChildren().contains(bar))
            gameView.getChildren().add(bar);

        for (ImageView img : EditItems.getAllItemsView()) {
            if (!gameView.getChildren().contains(img))
                gameView.getChildren().add(img);
        }

        if (!gameView.getChildren().contains(imgSelected))
            gameView.getChildren().add(imgSelected);

        displayItemSelected(x, y, gameView);
    }

    public void displayImg(double x, double y, StackPane gameView, Map map, Replace replace) {
        double fx = x - gameView.getWidth() / 2;
        double fy = y - gameView.getHeight() / 2;
        imgMode.get(mode).setTranslateX(fx + 10);
        imgMode.get(mode).setTranslateY(fy -10);

        Point2D mouse = map.getMapContainer().sceneToLocal(x, y);

        if (!gameView.getChildren().contains(imgMode.get(mode)))
            gameView.getChildren().add(imgMode.get(mode));

        if (mode == 0) {
            actionOnAdd(x, y, gameView);
        } else if (mode == 1) {
            replace.move(map, mouse);
        }
    }

    public void removeImg(StackPane gameView) {
        gameView.getChildren().remove(imgMode.get(mode));
        gameView.getChildren().remove(bar);

        for (ImageView img : EditItems.getAllItemsView()) {
            gameView.getChildren().remove(img);
        }

        gameView.getChildren().remove(imgSelected);

        gameView.getChildren().remove(itemToDisplay);
    }
}
