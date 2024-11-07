package Class.Map;

import java.util.ArrayList;
import Class.Item.Item;

public class Map {
    private String name;
    private String texture;
    private  boolean is_loaded;
    private ArrayList<Room> rooms;
    private ArrayList<Item> items;
    private double mapTranslateX = 0;
    private double mapTranslateY = 0;


    public Map(String name, String texture, boolean is_loaded, ArrayList<Room> rooms, ArrayList<Item> items) {
        this.name = name;
        this.texture = texture;
        this.is_loaded = is_loaded;
        this.rooms = rooms;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public String getTexture() {
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
