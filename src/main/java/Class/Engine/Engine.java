package Class.Engine;

import Class.Character.Player;
import Class.Character.Pnj;
import Class.Character.PnjTypeAdapter;
import Class.Item.Item;
import Class.Item.ItemTypeAdapter;
import Class.Item.*;
import Class.Map.Map;
import Class.bar.Tiredness;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javafx.application.Application;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Engine extends Application {
    private Player player;
    private Map map;
    private Pane mapContainer;
    private ImageView interactImg;
    private Item itemToInteract;
    private Item storeItem;
    private boolean canInteract;
    private boolean isInteracting;
    private int currentAction;

    //Constructor
    public Engine() {
        this.map = new Map("Map", new ImageView(new Image("file:assets/map/mapavectexture.png")), true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        this.player = new Player("Character", new Image("file:assets/perso/animtest1.png"), this.map);
        this.mapContainer = map.getMapContainer();
        this.interactImg = new ImageView(new Image("file:assets/interact/e.png"));
        this.interactImg.setFitWidth(32);
        this.interactImg.setFitHeight(32);
        this.itemToInteract = null;
        this.storeItem = null;
        this.canInteract = false;
        this.isInteracting = false;
        this.currentAction = 0;

    }
    private boolean isMoveKey(KeyCode key) {
        return key == KeyCode.UP || key == KeyCode.DOWN || key == KeyCode.LEFT || key == KeyCode.RIGHT || key == KeyCode.Z || key == KeyCode.Q || key == KeyCode.S || key == KeyCode.D;
    }

    Text hours = new Text();
    Text days = new Text();
    Text years = new Text();
    private void displayTime(StackPane gameView) {
        if (!gameView.getChildren().contains(years)) {
            years.setFill(javafx.scene.paint.Color.WHITE);
            years.setStyle("-fx-font: 20 arial;");
            StackPane.setAlignment(years, Pos.TOP_LEFT);
            years.setTranslateX(10);
            years.setTranslateY(10);
            gameView.getChildren().add(years);
        }
        years.setText("Year: "+player.getTimeYears());

        if (!gameView.getChildren().contains(days)) {
            days.setFill(javafx.scene.paint.Color.WHITE);
            days.setStyle("-fx-font: 20 arial;");
            StackPane.setAlignment(days, Pos.TOP_LEFT);
            days.setTranslateX(100);
            days.setTranslateY(10);
            gameView.getChildren().add(days);
        }
        days.setText("Day: "+player.getTimeDays());

        if (!gameView.getChildren().contains(hours)) {
            hours.setFill(javafx.scene.paint.Color.WHITE);
            hours.setStyle("-fx-font: 20 arial;");
            StackPane.setAlignment(hours, Pos.TOP_LEFT);
            hours.setTranslateX(200);
            hours.setTranslateY(10);
            gameView.getChildren().add(hours);
        }
        hours.setText("Hour: "+player.getTimeHours());

    }

    private void displayTiredness(StackPane gameview){
        Tiredness tiredness = new Tiredness("Tiredness", this.player.getWeakness());
        ImageView tirednessBar = new ImageView(tiredness.getTexture());
        double valueBar = tiredness.getCurrentCapacity()/tiredness.getMaxCapacity();
        if(!gameview.getChildren().contains(tirednessBar)){
            tirednessBar.setFitWidth(200);
            tirednessBar.setFitHeight(25);
            StackPane.setAlignment(tirednessBar, Pos.TOP_RIGHT);
            tirednessBar.setTranslateX(-10);
            tirednessBar.setTranslateY(10);
            Rectangle2D viewportRect = new Rectangle2D(0, 0, 68, 35);
            tirednessBar.setViewport(viewportRect);
            gameview.getChildren().add(tirednessBar);
        }
        tirednessBar.setFitWidth(200*valueBar);
        tirednessBar.setTranslateX(-10-200*(1-valueBar));
        tirednessBar.setViewport(new Rectangle2D(0, 0, 68*valueBar, 35));


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
    private void loadPnj(Map map) {
        Pnj[] pnjs = null;
        try (FileReader reader = new FileReader("./data/pnjs.json")) {
            // Read JSON file
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Pnj.class, new PnjTypeAdapter())
                    .create();

            // Parse JSON file
            pnjs = gson.fromJson(reader, Pnj[].class);
            for (int i = 0; i < pnjs.length; i++) {
                map.getPnjs().add(pnjs[i]);
            }
        } catch (NullPointerException | IOException | JsonIOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
    private void displayPnjs(StackPane gameView) {
        for (int i = 0; i < map.getPnjs().size(); i++) {
            gameView.getChildren().add(map.getPnjs().get(i).getCharacView());
            map.getPnjs().get(i).getCharacView().toFront();
        }
        for (Pnj pnj : this.map.getPnjs()) {
            this.map.getObstacles().add(pnj.getPnjHitbox());
            pnj.getCharacView().setLayoutX(pnj.getPosX());
            pnj.getCharacView().setLayoutY(pnj.getPosY());
            this.map.getMapContainer().getChildren().add(pnj.getCharacView());
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
            gameView.getChildren().add(map.getItems().get(i).getItemView());
            map.getItems().get(i).getItemView().toFront();

            Item item = map.getItems().get(i);
            mapContainer.getChildren().add(item.getInteractionHitbox());
            mapContainer.getChildren().add(item.getHitbox());

            item.getItemView().setLayoutX(item.getX());
            item.getItemView().setLayoutY(item.getY());
            mapContainer.getChildren().add(item.getItemView());
        }
    }

    private boolean detectColision() {
        for (int i = 0; i < map.getItems().size(); i++) {
            Item item = map.getItems().get(i);
            if (player.getPlayerHitbox().getBoundsInParent().intersects(item.getHitbox().getBoundsInParent())) {
                return true;
            }
        }
        return false;
    }

    private Item detectInteraction() {
        for (int i = 0; i < map.getItems().size(); i++) {
            Item item = map.getItems().get(i);
            if (player.getPlayerHitbox().getBoundsInParent().intersects(item.getInteractionHitbox().getBoundsInParent())) {
                this.storeItem = item;
                return item;
            }
        }
        return null;
    }

    private void displayInteractiveMenu() {
        if (this.itemToInteract != null && this.itemToInteract.getType() == ItemType.PC || this.itemToInteract.getType() == ItemType.DISTRIBUTOR || this.itemToInteract.getType() == ItemType.CANAP) {
            List<ImageView> images = this.itemToInteract.getMenu();

            for (int i = 0; i < images.size(); i += 2) {
                images.get(i).setLayoutX(this.itemToInteract.getX() + 40);
                images.get(i).setLayoutY(this.itemToInteract.getY() + (i * 18) + 1.5);
                if (!mapContainer.getChildren().contains(images.get(i))) {
                    mapContainer.getChildren().add(images.get(i));
                }
            }
        }
    }

    private void displayActionSelected() {
        if (this.itemToInteract != null && this.itemToInteract.getType() == ItemType.PC || this.itemToInteract.getType() == ItemType.DISTRIBUTOR || this.itemToInteract.getType() == ItemType.CANAP) {
            ImageView img = this.itemToInteract.getMenu().get(this.currentAction + 1);

            img.setLayoutX(this.itemToInteract.getX() + 36);
            img.setLayoutY(this.itemToInteract.getY() + (this.currentAction * 18) - 1.5);
            if (!mapContainer.getChildren().contains(img)) {
                mapContainer.getChildren().add(img);
            }

            for (int i = 0; i < this.itemToInteract.getMenu().size(); i += 2) {
                if (i != this.currentAction) {
                    ImageView img2 = this.itemToInteract.getMenu().get(i + 1);
                    if (mapContainer.getChildren().contains(img2)) {
                        mapContainer.getChildren().remove(img2);
                    }
                }
            }
        }
    }

    private void moveSelected(KeyEvent e) {
        List<ImageView> images = this.itemToInteract.getMenu();

        if (this.currentAction != -1) {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.Z) {
                if (this.currentAction > 0) {
                    this.currentAction -= 2;
                }
            } else if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) {
                if (this.currentAction < images.size() - 2) {
                    this.currentAction += 2;
                }
            }
        }
        for (Item item : this.map.getItems()) {
            item.getItemView().setLayoutX(item.getX());
            item.getItemView().setLayoutY(item.getY());
            this.map.getMapContainer().getChildren().add(item.getItemView());
        }
    }
    double X1 = 0;
    double Y1 = 0;
    double X2 = 0;
    double Y2 = 0;
    private void gameLoop(StackPane gameView) {
        displayPnjs(gameView);
        displayItems(gameView);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            player.updateTime(map);
            displayTime(gameView);
            displayTiredness(gameView);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        gameView.setOnMousePressed((MouseEvent e) ->{
            double clickX = e.getSceneX();
            double clickY = e.getSceneY();
            javafx.geometry.Point2D clickPoint = map.getMapContainer().sceneToLocal(clickX, clickY);

            if(e.isPrimaryButtonDown()){
                X1 = clickPoint.getX();
                Y1 = clickPoint.getY();
                System.out.println("X: " + clickPoint.getX());
                System.out.println("Y: " + clickPoint.getY());
            } else if (e.isSecondaryButtonDown()) {
                X2 = clickPoint.getX();
                Y2 = clickPoint.getY();
                double w = X2-X1;
                double h = Y2-Y1;
                System.out.println("Width "+w);
                System.out.println("Height "+h);
                System.out.println(X1+ ", "+ Y1 + ", "+ w + ", "+ h);
            }
        });
        gameView.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                this.isInteracting = false;
                this.itemToInteract = null;
                this.canInteract = false;
                mapContainer.getChildren().remove(interactImg);

                if (this.storeItem != null) {
                    for (ImageView img : this.storeItem.getMenu()) {
                        mapContainer.getChildren().remove(img);
                    }
                }

                this.currentAction = 0;
                this.storeItem = null;
            }

            if (this.itemToInteract != null) {
                this.canInteract = true;
                if (!mapContainer.getChildren().contains(interactImg)) {
                    interactImg.setLayoutX((double) this.itemToInteract.getWidth() / 2 + this.itemToInteract.getX() - 16);
                    interactImg.setLayoutY((double) this.itemToInteract.getHeight() / 2 + this.itemToInteract.getY() - 16);
                    mapContainer.getChildren().add(interactImg);
                }
            } else {
                mapContainer.getChildren().remove(interactImg);
            }

            if (e.getCode() == KeyCode.E && this.canInteract) {
                this.isInteracting = true;
                displayInteractiveMenu();
                displayActionSelected();
            }

            if (isMoveKey(e.getCode())) {
                if (detectColision()) {
                    System.out.println("Collision detected");
                }

                if (this.isInteracting) {
                    moveSelected(e);
                    displayActionSelected();

                } else {
                    player.move(map.getMapView(), gameView, true, e);
                }

                this.itemToInteract = detectInteraction();
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
        this.loadPnj(map);
        this.loadItems(map, 1);
        player.loadSkills("./data/skills.json");
        this.gameLoop(gameView);
        gameView.requestFocus();
    }
}
