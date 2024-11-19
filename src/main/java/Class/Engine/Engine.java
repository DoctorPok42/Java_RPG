package Class.Engine;

import Class.Character.Player;
import Class.Character.Pnj;
import Class.Character.PnjTypeAdapter;
import Class.Item.Item;
import Class.Item.ItemTypeAdapter;
import Class.Item.*;
import Class.Map.Map;
import Class.Menu.End;
import Class.Menu.Profile;
import Class.Music.Music;
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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javafx.application.Application;
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
    private final Music music;
    private final Profile profileMenu;
    private final End endMenu;
    private static final boolean isEnd = false;

    //Constructor
    public Engine() {
        this.map = new Map("Map", new ImageView(new Image("file:assets/map/mapavectexture.png")), true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        this.player = new Player("John Doe", new Image("file:assets/perso/animtest1.png"), this.map);
        this.weakness = new Tiredness("Tiredness");
        this.hunger = new Feed("Hunger");
        this.profileMenu = new Profile();
        this.endMenu = new End();
        this.music = new Music("assets/music/ingame.wav", 0.3);
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

    private void checkEndGame(StackPane gameView) {
        if (player.getTimeDays() >= 60) {
            music.pause();
            endMenu.show(gameView, player);
        }

        if (player.getHunger() <= 0 || player.getWeakness() <= 0) {
            music.pause();
            endMenu.show(gameView, player);
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
                if (!endMenu.isLoaded()) {
                    player.updateTime(map);
                    displayTime(gameView);
                    weakness.update(player, gameView);
                    hunger.update(player, gameView);
                }

                if (!endMenu.isLoaded())
                    checkEndGame(gameView);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        gameView.setOnMousePressed((MouseEvent e) -> {
            double clickX = e.getSceneX();
            double clickY = e.getSceneY();
            javafx.geometry.Point2D clickPoint = map.getMapContainer().sceneToLocal(clickX, clickY);
//            Rectangle previewRect = new Rectangle();
            ImageView item = new ImageView(new Image("file:assets/items/distributeur.png"));

            if(e.isPrimaryButtonDown()){
                X1 = clickPoint.getX();
                Y1 = clickPoint.getY();

                item.setLayoutX(X1);
                item.setLayoutY(Y1);
                mapContainer.getChildren().add(item);

//                previewRect.setX(X1);
//                previewRect.setY(Y1);
//                previewRect.setFill(Color.CYAN.deriveColor(0, 1, 1, 0.5));
//                previewRect.setStroke(Color.CYAN);
//                mapContainer.getChildren().add(previewRect);
//                map.getObstacles().add(previewRect);
//
//                Rectangle finalPreviewRect = previewRect;
                gameView.setOnMouseDragged((MouseEvent dragEvent) -> {
                    double dragX = dragEvent.getSceneX();
                    double dragY = dragEvent.getSceneY();
                    javafx.geometry.Point2D dragPoint = map.getMapContainer().sceneToLocal(dragX, dragY);

//                    item.setLayoutX(dragPoint.getX());
//                    item.setLayoutY(dragPoint.getY());

                    // mettre la table au millieu du curseur
                    item.setLayoutX(dragPoint.getX() - item.getImage().getWidth()/2);
                    item.setLayoutY(dragPoint.getY() - item.getImage().getHeight()/2);

                    mapContainer.getChildren().remove(item);
                    mapContainer.getChildren().add(item);

//                    finalPreviewRect.setWidth(width);
//                    finalPreviewRect.setHeight(height);
//
//                    finalPreviewRect.setX(Math.min(X1, dragPoint.getX()));
//                    finalPreviewRect.setY(Math.min(Y1, dragPoint.getY()));
                });

                gameView.setOnMouseReleased((MouseEvent releaseEvent) -> {
                    gameView.setOnMouseDragged(null);
                });
            } else if (e.isSecondaryButtonDown()) {
//                mapContainer.getChildren().remove(previewRect);
//                X2 = clickPoint.getX();
//                Y2 = clickPoint.getY();

                // mettre la table au millieu du curseur

                X2 = clickPoint.getX() - item.getImage().getWidth()/2;
                Y2 = clickPoint.getY() - item.getImage().getHeight()/2;

                double w = X2-X1;
                double h = Y2-Y1;

                if (w < 0) {
                    X1 = X2;
                    w = -w;
                }

                if (h < 0) {
                    Y1 = Y2;
                    h = -h;
                }

                System.out.println(X1+ ", "+ Y1 + ", "+ w + ", "+ h);

                 Gson gson = new GsonBuilder().registerTypeAdapter(Item.class, new ItemTypeAdapter()).setPrettyPrinting().create();

                String filePath = "./data/items.json";

                    try {
                        List<Item> obstaclesList = new ArrayList<>();
                        if (Files.exists(Paths.get(filePath))) {
                            Reader reader = Files.newBufferedReader(Paths.get(filePath));
                            Item[] existingObstacles = gson.fromJson(reader, Item[].class);
                            if (existingObstacles != null) {
                                obstaclesList.addAll(Arrays.asList(existingObstacles));
                            }
                            reader.close();
                        }

                        int lastId = 0;
                        if (!obstaclesList.isEmpty()) {
                            lastId = obstaclesList.getLast().getId();
                        }

                        obstaclesList.add(
                                new Item(
                                        "DISTRIBUTOR",
                                        ItemType.DISTRIBUTOR,
                                        (float) X1,
                                        (float) Y1,
                                        1,
                                        3,
                                        lastId + 1
                                )
                        );

                        Writer writer = Files.newBufferedWriter(Paths.get(filePath));
                        gson.toJson(obstaclesList, writer);
                        writer.close();

                        System.out.println("Item added to json file");

//                        Rectangle rect = new Rectangle(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
//                        rect.setFill(javafx.scene.paint.Color.GREEN.deriveColor(1, 1, 1, 0.5));
//                        map.getObstacles().add(rect);
//                        map.getMapContainer().getChildren().add(rect);

                        // enlever le rectangle de la map

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                X1 = X2 = Y1 = Y2 = 0;
            }
        });
        gameView.setOnKeyPressed(e -> {
            this.itemToInteract = player.getStoreItem();
            if (e.getCode() == KeyCode.ESCAPE) {
                if (!music.isPlaying())
                    music.play();

                this.isInteracting = false;
                this.itemToInteract = null;
                this.canInteract = false;
                mapContainer.getChildren().remove(interactImg);
                gameView.getChildren().remove(gameView.lookup("#dialogBox1"));

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
                endMenu.remove(gameView);
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

            if (e.getCode() == KeyCode.E && this.canInteract && !this.isInteracting && !profileMenu.isLoaded() && !endMenu.isLoaded()) {
                this.isInteracting = true;
                displayInteractiveMenu();
                displayActionSelected();
            }

            if (isMoveKey(e.getCode()) && !profileMenu.isLoaded() && !endMenu.isLoaded()) {
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

            if (e.getCode() == KeyCode.P && !this.isInteracting && !profileMenu.isLoaded() && !endMenu.isLoaded()) {
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
//        music.play();

        Scene scene = new Scene(gameView);
        primaryStage.setScene(scene);
        primaryStage.show();
        this.loadPnj(map);
        this.loadItems(map, 1);
        player.loadSkills("./data/skills.json");
        profileMenu.loadSkills(player);
        endMenu.setName(player.getName());
        weakness.display(gameView);
        hunger.display(gameView);
        this.gameLoop(gameView);
        gameView.requestFocus();
    }
}
