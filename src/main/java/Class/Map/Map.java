package Class.Map;

import java.util.ArrayList;
import java.util.List;
import Class.Item.Item;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Map {
    private String name;
    private boolean isLoaded;
    private List<Room> rooms;
    private List<Item> items;
    private ImageView mapView;
    private Pane mapContainer;
    private boolean isNight;
    private List<Rectangle> obstacles;
    private double mapTranslateX = 0;
    private double mapTranslateY = 0;
    private double viewHeight = 720;
    private double viewWidth = 1280;
    private double mapWidth;
    private double mapHeight;

    public Map(String name, ImageView mapView, boolean isLoaded, List<Room> rooms, List<Item> items) {
        this.name = name;
        this.mapView = mapView;
        this.isLoaded = isLoaded;
        this.rooms = rooms;
        this.items = items;
        this.mapContainer = new Pane(mapView);
        this.isNight = false;
        mapWidth = mapView.getImage().getWidth();
        mapHeight = mapView.getImage().getHeight();
        obstacles =new ArrayList<>();
        createObstacles();
        mapContainer.getChildren().addAll(obstacles);
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
    public List<Rectangle> getObstacles() {
        return obstacles;
    }
    public double getMapTranslateX() {
        return mapTranslateX;
    }
    public double getMapTranslateY() {
        return mapTranslateY;
    }
    public double getViewHeight() {
        return viewHeight;
    }
    public double getViewWidth() {
        return viewWidth;
    }
    public double getMapWidth() {
        return mapWidth;
    }
    public double getMapHeight() {
        return mapHeight;
    }

    public void setMapTranslateX(double mapTranslateX) {
        this.mapTranslateX = mapTranslateX;
    }
    public void setMapTranslateY(double mapTranslateY) {
        this.mapTranslateY = mapTranslateY;
    }
    public void setObstacles(List<Rectangle> obstacles) {
        this.obstacles = obstacles;
    }
    public void addMapContainer(Rectangle hitbox) {
        this.mapContainer.getChildren().add(hitbox);
    }
    public void addAllMapContainer(List<Rectangle> hitboxes) {
        this.mapContainer.getChildren().addAll(hitboxes);
    }

    public void setMapView(ImageView mapView) {
        this.mapView = mapView;
    }

    public void setIsNight(boolean isNight) {
        this.isNight = isNight;
    }

    private void createObstacles(){
        Rectangle obstaccle1 = new Rectangle(1548, 965, 200, 80);
        obstaccle1.setFill(Color.RED);
        obstacles.add(obstaccle1);

        Rectangle obstaccle2 = new Rectangle(2027, 958, 350, 90);
        obstaccle2.setFill(Color.BLUE);
        obstacles.add(obstaccle2);
    }
}
