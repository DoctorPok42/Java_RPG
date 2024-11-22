package Class.Map;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import Class.Character.Pnj;
import Class.Item.Item;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Map {
    private String name;
    private boolean isLoaded;
    private List<Room> rooms;
    private List<Item> items;
    private List<Pnj> pnjs;
    private ImageView mapView;
    private Pane mapContainer;
    private List<Rectangle> obstacles;
    private double mapTranslateX = 0;
    private double mapTranslateY = 0;
    private double viewHeight = 720;
    private double viewWidth = 1280;
    private double mapWidth;
    private double mapHeight;

    public Map(String name, ImageView mapView, boolean isLoaded, List<Room> rooms, List<Item> items, List<Pnj> pnjs) {
        this.name = name;
        this.mapView = mapView;
        mapView.setScaleX(1.5);
        mapView.setScaleY(1.5);
        this.isLoaded = isLoaded;
        this.rooms = rooms;
        this.items = items;
        this.pnjs = pnjs;
        this.mapContainer = new Pane(mapView);
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

    public List<Pnj> getPnjs() {
        return pnjs;
    }

    public ImageView getMapView() {
        return mapView;
    }

    public Pane getMapContainer() {
        return mapContainer;
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

    private void createObstacles(){
        Obstacles[] obstacles = null;
        try (FileReader reader = new FileReader("./data/obstacles.json")){
            Gson gson = new GsonBuilder().registerTypeAdapter(Obstacles.class, new obstaclesTypeAdaptater()).create();
            obstacles = gson.fromJson(reader, Obstacles[].class);
            for (Obstacles obstacle : obstacles) {
                Rectangle rect = new Rectangle(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
                rect.setFill(Color.TRANSPARENT);
                this.obstacles.add(rect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
