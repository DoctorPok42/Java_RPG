package Class.Engine;

import Class.Character.Player;
import Class.Item.Item;
import Class.Item.ItemTypeAdapter;
import Class.Map.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Engine extends Application {
    private Player player;
    private Map map;

    //Constructor
    public Engine(){
        this.map = new Map("Map", new ImageView(new Image("file:assets/map/vraimenttestmap.png")), true, new ArrayList<>(), new ArrayList<>());
        this.player = new Player("Character", new Image("file:assets/perso/animtest1.png"), this.map);
    }
    private boolean isMoveKey(KeyCode key) {
        return key == KeyCode.UP || key == KeyCode.DOWN || key == KeyCode.LEFT || key == KeyCode.RIGHT || key == KeyCode.Z || key == KeyCode.Q || key == KeyCode.S || key == KeyCode.D;
    }

    private void onClick(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
    }

    private void changeMap(int floor) {
        switch (floor) {
            case 0:
                map.setMapView(new ImageView(new Image("file:assets/map/mapTest.png")));
                break;
            case 1:
                map.setMapView(new ImageView(new Image("file:assets/map/mapTest2.png")));
                break;
            case 2:
                map.setMapView(new ImageView(new Image("file:assets/map/mapTest3.png")));
                break;
            default:
                break;
        }
    }

    private void loadItems(Map map, int mapLevel) {
        Item[] items = null;
        try (FileReader reader = new FileReader("./data/items.json")) {
            // Read JSON file
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Item.class, new ItemTypeAdapter())
                    .create();

            // Parse JSON file
            items = gson.fromJson(reader, Item[].class);
            for (int i = 0; i < items.length; i++) {
                // Add items to the map if they are on the current level
                if (items[i].getZ() == mapLevel) {
                    map.getItems().add(items[i]);
                }
            }
        } catch (NullPointerException | IOException | JsonIOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void displayItems(StackPane gameView) {
        for (int i = 0; i < map.getItems().size(); i++) {
            gameView.getChildren().add(map.getItems().get(i).getItemView());
            map.getItems().get(i).getItemView().toFront();
        }
    }

    private void gameLoop(StackPane gameView) {
        displayItems(gameView);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> player.updateTime(map)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        gameView.setOnKeyPressed(e -> {
            if (isMoveKey(e.getCode())) {
                player.move(map.getMapView(), gameView, true, e);
            }
        });

        gameView.setOnKeyReleased(e -> {
            if (isMoveKey(e.getCode())) {
                player.move(map.getMapView(), gameView, false, e);
            }
        });
    }

    public void start(Stage primaryStage) {
        StackPane gameView = new StackPane(map.getMapContainer(), player.getPlayerView());
        gameView.setPrefSize(this.map.getViewWidth(), this.map.getViewHeight());

        Scene scene = new Scene(gameView);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Epitech Simulator");
        primaryStage.setResizable(false);
        primaryStage.show();
        this.gameLoop(gameView);
        this.loadItems(map, 1);
        player.loadSkills("./data/skills.json");
        gameView.requestFocus();
    }
}
