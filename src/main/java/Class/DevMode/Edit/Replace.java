package Class.DevMode.Edit;

import Class.DevMode.Edit.Utils.ReadItemFile;
import Class.Item.Item;
import Class.Item.ItemTypeAdapter;
import Class.Map.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.paint.Color;

import javafx.geometry.Point2D;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Replace {
    private final List<Item> itemsToReplace = new ArrayList<>();
    private final List<Item> itemsToReplaceStored = new ArrayList<>();
    private final ReadItemFile readItemFile;

    private List<Item> itemField = new ArrayList<>();

    public Replace(String filePath) throws IOException {
        readItemFile = new ReadItemFile(filePath);
        this.itemField = readItemFile.readItems(new GsonBuilder().registerTypeAdapter(Item.class, new ItemTypeAdapter()).create());
    }

    public void move(Map map, Point2D mouse) {
        map.getItems().forEach(item -> {
            if (item.getHitbox().contains(mouse)) {
                item.getHitbox().setStrokeWidth(3);
                item.getHitbox().setStroke(Color.RED);
            } else {
                item.getHitbox().setStrokeWidth(0);
            }
        });
    }

    public void removeItem() {
        itemsToReplace.clear();
    }

    public void selectItem(Map map, Point2D mouse) {
        removeItem();
        map.getItems().forEach(item -> {
            if (item.getHitbox().contains(mouse)) {
                itemsToReplace.add(item);
            }
        });
    }

    public void saveInJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Item.class, new ItemTypeAdapter()).setPrettyPrinting().create();

        try {
            List<Item> itemList = this.itemField;

            itemsToReplaceStored.forEach(item ->
                itemList.forEach(itemStored -> {
                    if (itemStored.getId() == item.getId()) {
                        itemStored.setX((float) item.getItemView().getLayoutX());
                        itemStored.setY((float) item.getItemView().getLayoutY());
                    }
                })
            );

            Writer writer = Files.newBufferedWriter(readItemFile.getItemFilePath());
            gson.toJson(itemList, writer);
            writer.close();

            this.itemField = itemList;

            itemsToReplaceStored.clear();
            itemsToReplace.clear();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void drag(Point2D mouse) {
        itemsToReplace.forEach(item -> {
            item.getItemView().setLayoutX(mouse.getX() - item.getItemView().maxWidth(0) / 2);
            item.getItemView().setLayoutY(mouse.getY() - item.getItemView().maxHeight(0) / 2);

            item.getInteractionHitbox().setX(mouse.getX() - item.getInteractionHitbox().getWidth() / 2);
            item.getInteractionHitbox().setY(mouse.getY() - item.getInteractionHitbox().getHeight() / 2);

            item.getHitbox().setX(mouse.getX() - item.getHitbox().getWidth() / 2);
            item.getHitbox().setY(mouse.getY() - item.getHitbox().getHeight() / 2);

            item.setX((float) item.getItemView().getLayoutX());
            item.setY((float) item.getItemView().getLayoutY());

            if (!itemsToReplaceStored.contains(item))
                itemsToReplaceStored.add(item);
        });
    }

    public void undo() {
        List<Item> itemsCopy = new ArrayList<>(itemsToReplaceStored);

        itemsCopy.forEach(item -> {
            item.getItemView().setLayoutX(item.getX());
            item.getItemView().setLayoutY(item.getY());

            item.getInteractionHitbox().setX(item.getX());
            item.getInteractionHitbox().setY(item.getY());

            item.getHitbox().setX(item.getX());
            item.getHitbox().setY(item.getY());

            item.setX((float) item.getItemView().getLayoutX());
            item.setY((float) item.getItemView().getLayoutY());

            saveInJson();
        });
    }

    public void deleteItem(Map map, Point2D mouse) {
        List<Item> itemsToRemove = new ArrayList<>();

        map.getItems().forEach(item -> {
            if (item.getHitbox().contains(mouse)) {
                itemsToRemove.add(item);
            }
        });

        if (!itemsToRemove.isEmpty()) {
            itemsToRemove.forEach(item -> {
                map.getMapContainer().getChildren().remove(item.getHitbox());
                map.getMapContainer().getChildren().remove(item.getInteractionHitbox());
                map.getItems().remove(item);
                map.getMapContainer().getChildren().remove(item.getItemView());
            });

            Gson gson = new GsonBuilder().registerTypeAdapter(Item.class, new ItemTypeAdapter()).setPrettyPrinting().create();

            try {
                List<Item> itemList = this.itemField;

                itemsToRemove.forEach(item -> {
                    itemList.removeIf(itemStored -> itemStored.getId() == item.getId());
                });

                Writer writer = Files.newBufferedWriter(readItemFile.getItemFilePath());
                gson.toJson(itemList, writer);
                writer.close();

                this.itemField = itemList;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
