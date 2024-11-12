package Class.Engine;

import Class.Character.Player;
import Class.Item.*;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Engine extends Application {
    private Player player;
    private Map map;
    private Pane mapContainer;
    private ImageView interactImg;
    private Item itemToInteract;
    private boolean canInteract;

    //Constructor
    public Engine() {
        this.map = new Map("Map", new ImageView(new Image("file:assets/map/vraimenttestmap.png")), true, new ArrayList<>(), new ArrayList<>());
        this.player = new Player("Character", new Image("file:assets/perso/animtest1.png"), this.map);
        this.mapContainer = map.getMapContainer();
        this.interactImg = new ImageView(new Image("file:assets/interact/e.png"));
        this.interactImg.setFitWidth(32);
        this.interactImg.setFitHeight(32);
        this.itemToInteract = null;
        this.canInteract = false;
    }
    private boolean isMoveKey(KeyCode key) {
        return key == KeyCode.UP || key == KeyCode.DOWN || key == KeyCode.LEFT || key == KeyCode.RIGHT || key == KeyCode.Z || key == KeyCode.Q || key == KeyCode.S || key == KeyCode.D;
    }

    private void changeMap(int floor) {
        switch (floor) {
            case 0:
                map.setMapView(new ImageView(new Image("file:assets/map/mapTest.png")));
                break;
            case 1:
                map.setMapView(new ImageView(new Image("file:assets/map/vraimenttestmap.png")));
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
//                    map.getItems().add(items[i]);
                    switch ((ItemType) items[i].getType()) {
                        case PC:
                            map.getItems().add(new Pc(items[i].getName(), items[i].getType(), items[i].getX(), items[i].getY(), items[i].getZ()));
                            break;
                        case CANAP:
                            map.getItems().add(new Canap(items[i].getName(), items[i].getType(), items[i].getX(), items[i].getY(), items[i].getZ()));
                            break;
                        case DISTRIBUTOR:
                            map.getItems().add(new Distributor(items[i].getName(), items[i].getType(), items[i].getX(), items[i].getY(), items[i].getZ(), 0));
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (NullPointerException | IOException | JsonIOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void displayItems(StackPane gameView) {
        for (int i = 0; i < map.getItems().size(); i++) {
            Item item = map.getItems().get(i);
            mapContainer.getChildren().add(item.getInteractionHitbox());
            mapContainer.getChildren().add(item.getHitbox());

            item.getItemView().setLayoutX(item.getX());
            item.getItemView().setLayoutY(item.getY());
            mapContainer.getChildren().add(item.getItemView());
        }
    }

    private void detectColision() {
        for (int i = 0; i < map.getItems().size(); i++) {
            Item item = map.getItems().get(i);
            if (player.getPlayerHitbox().getBoundsInParent().intersects(item.getHitbox().getBoundsInParent())) {
                System.out.println("Collision with " + item.getName());
            }
        }
    }

    private Item detectInteraction() {
        for (int i = 0; i < map.getItems().size(); i++) {
            Item item = map.getItems().get(i);
            if (player.getPlayerHitbox().getBoundsInParent().intersects(item.getInteractionHitbox().getBoundsInParent())) {
                System.out.println("Interaction with " + item.getName());
                return item;
            }
        }
        return null;
    }

    private void displayInteractiveMenu() {
        // switch sur ItemType
        switch ((ItemType) this.itemToInteract.getType()) {
            case PC:
                System.out.println("PC");
                break;
            case CANAP:
                List<ImageView> canapImages = this.itemToInteract.getMenu();

                for (int i = 0; i < canapImages.size(); i++) {
                    canapImages.get(i).setLayoutX(this.itemToInteract.getX() + 60);
                    canapImages.get(i).setLayoutY(this.itemToInteract.getY() + (i * 50) + 1.5);
                    if (!mapContainer.getChildren().contains(canapImages.get(i))) {
                        mapContainer.getChildren().add(canapImages.get(i));
                    }
                }
                break;
            case DISTRIBUTOR:
                System.out.println("DISTRIBUTOR");
                break;
        }
    }

    private void gameLoop(StackPane gameView) {
        displayItems(gameView);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> player.updateTime(map)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        gameView.setOnKeyPressed(e -> {
            if (this.itemToInteract != null) {
                this.canInteract = true;
                if (!mapContainer.getChildren().contains(interactImg)) {
                    interactImg.setLayoutX((double) this.itemToInteract.getWidth() / 2 + this.itemToInteract.getX() - 16);
                    interactImg.setLayoutY((double) this.itemToInteract.getHeight() / 2 + this.itemToInteract.getY() - 16);
                    mapContainer.getChildren().add(interactImg);
                }
            } else if (mapContainer.getChildren().contains(interactImg)) {
                this.canInteract = false;
                mapContainer.getChildren().remove(interactImg);

                if (this.itemToInteract != null && new HashSet<>(mapContainer.getChildren()).containsAll(this.itemToInteract.getMenu())) {
                    mapContainer.getChildren().removeAll(this.itemToInteract.getMenu());
                }
            } else {
                this.canInteract = false;

                if (this.itemToInteract != null && new HashSet<>(mapContainer.getChildren()).containsAll(this.itemToInteract.getMenu())) {
                    mapContainer.getChildren().removeAll(this.itemToInteract.getMenu());
                }
            }

            if (e.getCode() == KeyCode.E && this.canInteract) {
                System.out.println("Interacting with " + this.itemToInteract.getName());
                displayInteractiveMenu();
            }

            if (isMoveKey(e.getCode())) {
                player.move(map.getMapView(), gameView, true, e);
                this.itemToInteract = detectInteraction();
                detectColision();
            }
        });

        gameView.setOnKeyReleased(e -> {
            if (isMoveKey(e.getCode())) {
                player.move(map.getMapView(), gameView, false, e);
            }
        });

        gameView.setOnMousePressed((MouseEvent e) ->{
            double clickX = e.getSceneX();
            double clickY = e.getSceneY();
            javafx.geometry.Point2D clickPoint = this.map.getMapContainer().sceneToLocal(clickX, clickY);

            System.out.println("X: " + clickPoint.getX());
            System.out.println("Y: " + clickPoint.getY());
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
        this.loadItems(map, 1);
        this.gameLoop(gameView);
        player.loadSkills("./data/skills.json");
        gameView.requestFocus();
    }
}
