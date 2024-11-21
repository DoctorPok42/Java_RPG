package Class.DevMode.Edit.Replace;

import Class.Item.Item;
import Class.Item.ItemTypeAdapter;
import Class.Map.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.paint.Color;

import javafx.geometry.Point2D;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Replace {
    private final List<Item> itemsToReplace = new ArrayList<>();
    private final List<Item> itemsToReplaceStored = new ArrayList<>();

    public Replace() {
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
        String filePath = "./data/items.json";

        try {
            List<Item> itemList = new ArrayList<>();
            Path itemFilePath = Paths.get(filePath);
            if (Files.exists(itemFilePath)) {
                Reader reader = Files.newBufferedReader(itemFilePath);
                Item[] existingObstacles = gson.fromJson(reader, Item[].class);
                if (existingObstacles != null) {
                    itemList.addAll(Arrays.asList(existingObstacles));
                }
                reader.close();
            }

            itemsToReplaceStored.forEach(item ->
                itemList.forEach(itemStored -> {
                    if (itemStored.getId() == item.getId()) {
                        itemStored.setX((float) item.getItemView().getLayoutX());
                        itemStored.setY((float) item.getItemView().getLayoutY());
                    }
                })
            );

            Writer writer = Files.newBufferedWriter(itemFilePath);
            gson.toJson(itemList, writer);
            writer.close();

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

        System.out.println("itemsCopy: " + itemsCopy);

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
}
