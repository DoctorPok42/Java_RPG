package Class.Engine;

import Class.Character.Player;
import Class.Character.Pnj;
import Class.Character.PnjTypeAdapter;
import Class.Item.Item;
import Class.Item.ItemTypeAdapter;
import Class.Item.*;
import Class.Map.Map;
import Class.Menu.Profile;
import Class.Skill.Skill;
import Class.bar.Feed;
import Class.bar.Tiredness;
import Class.bar.bar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Engine extends Application {
    private final Player player;
    private final Map map;
    private final Pane mapContainer;
    private final ImageView interactImg;
    private Item itemToInteract;
    private boolean canInteract;
    private boolean isInteracting;
    private int currentAction;
    private final bar weakness;
    private final bar hunger;
    private final Media media = new Media(new File("assets/music/ingame.wav").toURI().toString());
    private final Profile profileMenu;

    //Constructor
    public Engine() {
        this.map = new Map("Map", new ImageView(new Image("file:assets/map/mapavectexture.png")), true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        this.player = new Player("John Doe", new Image("file:assets/perso/animtest1.png"), this.map);
        this.weakness = new Tiredness("Tiredness");
        this.hunger = new Feed("Hunger");
        this.profileMenu = new Profile();
        this.mapContainer = map.getMapContainer();
        this.interactImg = new ImageView(new Image("file:assets/interact/e.png"));
        this.interactImg.setFitWidth(32);
        this.interactImg.setFitHeight(32);
        this.itemToInteract = null;
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
        String styles = "-fx-font: 20 arial;";
        if (!gameView.getChildren().contains(years)) {
            years.setFill(javafx.scene.paint.Color.WHITE);
            years.setStyle(styles);
            StackPane.setAlignment(years, Pos.TOP_LEFT);
            years.setTranslateX(10);
            years.setTranslateY(10);
            gameView.getChildren().add(years);
        }
        years.setText("Year: "+player.getTimeYears());

        if (!gameView.getChildren().contains(days)) {
            days.setFill(javafx.scene.paint.Color.WHITE);
            days.setStyle(styles);
            StackPane.setAlignment(days, Pos.TOP_LEFT);
            days.setTranslateX(100);
            days.setTranslateY(10);
            gameView.getChildren().add(days);
        }
        days.setText("Day: "+player.getTimeDays());

        if (!gameView.getChildren().contains(hours)) {
            hours.setFill(javafx.scene.paint.Color.WHITE);
            hours.setStyle(styles);
            StackPane.setAlignment(hours, Pos.TOP_LEFT);
            hours.setTranslateX(200);
            hours.setTranslateY(10);
            gameView.getChildren().add(hours);
        }
        hours.setText("Hour: "+player.getTimeHours());

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
            for (Pnj pnj : pnjs) {
                map.getPnjs().add(pnj);
                map.getItems().add(new PnjInteraction(pnj.getName(), ItemType.PNJ, (float) pnj.getPosX(), (float) pnj.getPosY(), 1, -1, pnj));
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
            for (Item item : items) {
                // Add items to the map if they are on the current level
                if (item.getZ() == mapLevel) {
                    switch ((ItemType) item.getType()) {
                        case PC:
                            map.getItems().add(new Pc(item.getName(), item.getType(), item.getX(), item.getY(), item.getZ(), item.getSkin()));
                            break;
                        case CANAP:
                            map.getItems().add(new Canap(item.getName(), item.getType(), item.getX(), item.getY(), item.getZ(), item.getSkin()));
                            break;
                        case DISTRIBUTOR:
                            map.getItems().add(new Distributor(item.getName(), item.getType(), item.getX(), item.getY(), item.getZ(), 0, item.getSkin()));
                            break;
                        case PNJ:
                            break;
                        default:
                            map.getItems().add(item);
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


    private void displayInteractiveMenu() {
        if (this.itemToInteract != null && this.itemToInteract.getType() == ItemType.PC || this.itemToInteract.getType() == ItemType.DISTRIBUTOR || this.itemToInteract.getType() == ItemType.CANAP|| this.itemToInteract.getType() == ItemType.PNJ) {
            List<ImageView> images = this.itemToInteract.getMenu();

            for (int i = 0; i < images.size(); i++) {
                images.get(i).setLayoutX(this.itemToInteract.getX() + 37);
                images.get(i).setLayoutY(this.itemToInteract.getY() + (i * 35) - 1);
                if (!mapContainer.getChildren().contains(images.get(i))) {
                    mapContainer.getChildren().add(images.get(i));
                }
            }
        }
    }

    private void displayActionSelected() {
        if (this.itemToInteract != null && this.itemToInteract.getType() == ItemType.PC || Objects.requireNonNull(this.itemToInteract).getType() == ItemType.DISTRIBUTOR || this.itemToInteract.getType() == ItemType.CANAP|| this.itemToInteract.getType() == ItemType.PNJ) {
            ImageView img = this.itemToInteract.getSelectedMenu();

            if (this.currentAction < 10) {
                img.setLayoutX(this.itemToInteract.getX() + 33.5);
                img.setLayoutY(this.itemToInteract.getY() + (this.currentAction * 35) - 4.5);
            } else {
                img.setLayoutX(this.itemToInteract.getX() + 121);
                img.setLayoutY(this.itemToInteract.getY() + (this.currentAction - 10) * 36 - 4.5);
            }

            if (!mapContainer.getChildren().contains(img)) {
                mapContainer.getChildren().add(img);
            } else {
                mapContainer.getChildren().remove(img);
                mapContainer.getChildren().add(img);
            }
        }
    }

    private void moveSelected(KeyEvent e) {
        List<ImageView> images = this.itemToInteract.getMenu();

        if (this.currentAction != -1 && this.currentAction < 10) {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.Z) {
                if (this.currentAction > 0) {
                    this.currentAction -= 1;
                }
            } else if ((e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) && this.currentAction < images.size() - 1) {
                this.currentAction += 1;
            }
        } else if (this.currentAction != -1) {
            List<ImageView> secondMenu = this.itemToInteract.getSecondMenu().get((this.currentAction / 10) - 1);

            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.Z) {
                if (this.currentAction > 10) {
                    this.currentAction -= 1;
                }
            } else if ((e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) && (this.currentAction % 10) < secondMenu.size() - 1) {
                this.currentAction += 1;
            }
        }

        if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) {
            if (this.currentAction < 10 && this.itemToInteract.getSecondMenu().get(this.currentAction) != null) {
                this.currentAction += 10;
            }
        } else if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.Q) {
            if (this.currentAction > 10) {
                this.currentAction = (this.currentAction / 10) - 1;
            }  else if (this.currentAction >= 10) {
                this.currentAction -= 10;
            }
        }
    }

    private void removeSecondMenu() {
        List<List<ImageView>> images = this.itemToInteract.getSecondMenu();

        for (List<ImageView> image : images) {
            if (image != null) {
                for (ImageView imageView : image) {
                    mapContainer.getChildren().remove(imageView);
                }
            }
        }
    }

    private void displaySecondMenu() {
        if (this.currentAction < 10) {
            removeSecondMenu();
            return;
        }

        List<List<ImageView>> images = this.itemToInteract.getSecondMenu();
        List<ImageView> secondMenu = images.get((this.currentAction / 10) - 1);

        if (secondMenu == null) return;

        for (int i = 0; i < secondMenu.size(); i++) {
            secondMenu.get(i).setLayoutX(this.itemToInteract.getX() + 125);
            secondMenu.get(i).setLayoutY(this.itemToInteract.getY() + (i * 36) - 1);
            if (!mapContainer.getChildren().contains(secondMenu.get(i))) {
                mapContainer.getChildren().add(secondMenu.get(i));
            }
        }
    }

    private void doActionOnEnter(StackPane gameView) {
        List<Skill> skills = player.getSkills();

        if (this.itemToInteract != null) {
            int firstMenu;
            if (this.currentAction < 10) {
                firstMenu = this.currentAction;
            } else {
                firstMenu = (this.currentAction / 10) -1;
            }

            if (this.itemToInteract.getType() == ItemType.PC) {
                Pc pc = (Pc) this.itemToInteract;
                pc.doAction(player, pc.getActions().get(firstMenu), 2, skills.get(this.currentAction % 10).getName());
            } else if (this.itemToInteract.getType() == ItemType.DISTRIBUTOR) {
                Distributor distributor = (Distributor) this.itemToInteract;
                distributor.doAction(player, distributor.getActions().get(firstMenu), 1, "module");
            } else if (this.itemToInteract.getType() == ItemType.CANAP) {
                Canap canap = (Canap) this.itemToInteract;
                canap.doAction(player, canap.getActions().get(firstMenu), 1, "module");
            } else if ( this.itemToInteract.getType() == ItemType.PNJ) {
                PnjInteraction pnj = (PnjInteraction) this.itemToInteract;
                pnj.doAction(this.player, pnj.getActions().get(firstMenu), pnj.getPnj(), gameView);
            }
        }
    }
    double X1 = 0;
    double Y1 = 0;
    double X2 = 0;
    double Y2 = 0;
    private void gameLoop(StackPane gameView) {
        displayPnjs(gameView);
        displayItems(gameView);
        displayTime(gameView);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            player.updateTime(map);
            displayTime(gameView);
            weakness.update(player, gameView);
            hunger.update(player, gameView);
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
            this.itemToInteract = player.getStoreItem();
            if (e.getCode() == KeyCode.ESCAPE) {
                this.isInteracting = false;
                this.itemToInteract = null;
                this.canInteract = false;
                mapContainer.getChildren().remove(interactImg);

                if (player.getStoreItem() != null) {
                    for (ImageView img : player.getStoreItem().getMenu()) {
                        mapContainer.getChildren().remove(img);
                    }

                    if (player.getStoreItem().getSecondMenu() != null) {
                        for (List<ImageView> imgs : player.getStoreItem().getSecondMenu()) {
                            if (imgs != null) {
                                for (ImageView img : imgs) {
                                    mapContainer.getChildren().remove(img);
                                }
                            }
                        }
                    }

                    mapContainer.getChildren().remove(player.getStoreItem().getSelectedMenu());
                }

                this.currentAction = 0;
                player.setStoreItem(null);

                profileMenu.remove(gameView);
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

            if (e.getCode() == KeyCode.E && this.canInteract && !this.isInteracting && !profileMenu.isLoaded()) {
                this.isInteracting = true;
                displayInteractiveMenu();
                displayActionSelected();
            }

            if (isMoveKey(e.getCode()) && !profileMenu.isLoaded()) {
                if (this.isInteracting) {
                    moveSelected(e);
                    displaySecondMenu();
                    displayActionSelected();
                } else {
                    player.move(map.getMapView(), gameView, true, e);
                }
            }

            if (e.getCode() == KeyCode.ENTER && this.isInteracting) {
                doActionOnEnter(gameView);
            }

            if (e.getCode() == KeyCode.P && !this.isInteracting && !profileMenu.isLoaded()) {
                profileMenu.show(gameView, player);
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

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.2);

        Scene scene = new Scene(gameView);
        primaryStage.setScene(scene);
        primaryStage.show();
        this.loadPnj(map);
        this.loadItems(map, 1);
        player.loadSkills("./data/skills.json");
        profileMenu.loadSkills(player);
        weakness.display(gameView);
        hunger.display(gameView);
        this.gameLoop(gameView);
        gameView.requestFocus();
    }
}
