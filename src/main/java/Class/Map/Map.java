package Class.Map;

import java.util.ArrayList;
import Class.Item.Item;
import javafx.scene.image.Image;

public class Map {
    private String name;
    private Image texture;
    private  boolean is_loaded;
    private ArrayList<Room> rooms;
    private ArrayList<Item> items;

    public Map(String name, Image texture, boolean is_loaded, ArrayList<Room> rooms, ArrayList<Item> items) {
        this.name = name;
        this.texture = texture;
        this.is_loaded = is_loaded;
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
        return is_loaded;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
