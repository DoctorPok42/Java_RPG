package Class.Map;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import Class.Character.Pnj;
import Class.Item.Item;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    private boolean isNight;
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

    public List<Pnj> getPnjs() {
        return pnjs;
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
//        Rectangle obstaccle1 = new Rectangle(1548, 965, 200, 80);
//        obstaccle1.setFill(Color.RED);
//        obstacles.add(obstaccle1);
//
//        Rectangle obstaccle2 = new Rectangle(2027, 958, 350, 90);
//        obstaccle2.setFill(Color.BLUE);
//        obstacles.add(obstaccle2);

//        Rectangle mur00 = new Rectangle(1892.800048828125, 1209.5999755859375, 56.0, 24.800048828125);
//        mur00.setFill(Color.TRANSPARENT);
//        obstacles.add(mur00);
//
//        Rectangle mur01 = new Rectangle(1927.199951171875, 1119.199951171875, 23.2000732421875, 114.4000244140625);
//        mur01.setFill(Color.TRANSPARENT);
//        obstacles.add(mur01);
//
//        Rectangle mur02 = new Rectangle(1717.5999755859375, 1205.5999755859375, 41.5999755859375, 27.2000732421875);
//        mur02.setFill(Color.TRANSPARENT);
//        obstacles.add(mur02);
//
//        Rectangle mur03 = new Rectangle(1717.5999755859375, 1120.800048828125, 20.800048828125, 107.199951171875);
//        mur03.setFill(Color.TRANSPARENT);
//        obstacles.add(mur03);
//
//        Rectangle mur04 = new Rectangle(1374.0, 1118.800048828125, 361.5999755859375, 23.199951171875);
//        mur04.setFill(Color.TRANSPARENT);
//        obstacles.add(mur04);
//
//        Rectangle mur05 = new Rectangle(1928.800048828125, 1119.5999755859375, 273.599853515625, 20.800048828125);
//        mur05.setFill(Color.TRANSPARENT);
//        obstacles.add(mur05);
//
//        Rectangle mur06 = new Rectangle(1933.199951171875, 1026.4000244140625, 10.4000244140625, 92.0);
//        mur06.setFill(Color.TRANSPARENT);
//        obstacles.add(mur06);
    }
}
