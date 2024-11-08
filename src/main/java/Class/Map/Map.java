package Class.Map;

import java.util.List;
import Class.Item.Item;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Map {
    private String name;
    private  boolean isLoaded;
    private List<Room> rooms;
    private List<Item> items;
    private ImageView mapView;
    private Pane mapContainer;
    private boolean isNight;

    public Map(String name, ImageView mapView, boolean isLoaded, List<Room> rooms, List<Item> items) {
        this.name = name;
        this.mapView = mapView;
        this.isLoaded = isLoaded;
        this.rooms = rooms;
        this.items = items;
        this.mapContainer = new Pane(mapView);
        this.isNight = false;
    }

    public String getName() {
        return name;
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

    public ImageView getMapView() {
        return mapView;
    }

    public Pane getMapContainer() {
        return mapContainer;
    }

    public boolean getIsNight() {
        return isNight;
    }

    public void setMapView(ImageView mapView) {
        this.mapView = mapView;
    }

    public void setIsNight(boolean isNight) {
        this.isNight = isNight;
    }
}
