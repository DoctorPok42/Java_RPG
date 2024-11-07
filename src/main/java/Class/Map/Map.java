package Class.Map;

import java.util.List;
import Class.Item.Item;
import javafx.scene.image.Image;

public class Map {
    private String name;
    private Image texture;
    private  boolean isLoaded;
    private List<Room> rooms;
    private List<Item> items;

    public Map(String name, Image texture, boolean isLoaded, List<Room> rooms, List<Item> items) {
        this.name = name;
        this.texture = texture;
        this.isLoaded = isLoaded;
        this.rooms = rooms;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public Image getTexture() {
        return texture;
    }

    public boolean getIsLoaded() {
        return isLoaded;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Item> getItems() {
        return items;
    }
}
