package Class.Engine;

import Class.Character.Player;
import Class.Character.Pnj;
import Class.Character.PnjTypeAdapter;
import Class.DevMode.DevEngine;
import Class.Item.Item;
import Class.Item.ItemTypeAdapter;
import Class.Item.*;
import Class.Map.Map;
import Class.Menu.End;
import Class.Menu.Profile;
import Class.Music.Music;
import Class.bar.Feed;
import Class.bar.Time;
import Class.bar.Tiredness;
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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Engine {
    private final Player player;
    private final Map map;
    private final Pane mapContainer;
    private final ImageView interactImg;
    private Item itemToInteract;
    private boolean canInteract;
    private boolean isInteracting;
    private final Tiredness weakness;
    private final Feed hunger;
    private final Time time;
    private final Music music;
    private final Profile profileMenu;
    private final End endMenu;
    private final MenuControler menuControler;
    private boolean isDevMode = false;
    private final DevEngine devEngine;
    private boolean isAltPressed = false;

    //Constructor
    public Engine() {
        this.map = new Map("Map", new ImageView(new Image("file:assets/map/mapavectexture.png")), true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        this.player = new Player("John Doe", new Image("file:assets/perso/animtest1.png"), this.map);
        this.weakness = new Tiredness("Tiredness");
        this.hunger = new Feed("Hunger");
        this.time = new Time("Time");
        this.profileMenu = new Profile();
        this.endMenu = new End();
        this.menuControler = new MenuControler("MenuControler");
        this.music = new Music("assets/music/ingame.wav", 0.3);
        this.mapContainer = map.getMapContainer();
        this.interactImg = new ImageView(new Image("file:assets/interact/e.png"));
        this.interactImg.setFitWidth(32);
        this.interactImg.setFitHeight(32);
        this.itemToInteract = null;
        this.canInteract = false;
        this.isInteracting = false;
        this.devEngine = new DevEngine();
    }
    private boolean isMoveKey(KeyCode key) {
        return key == KeyCode.UP || key == KeyCode.DOWN || key == KeyCode.LEFT || key == KeyCode.RIGHT || key == KeyCode.Z || key == KeyCode.Q || key == KeyCode.S || key == KeyCode.D;
    }

    private void loadPnj(Map map) {
        Pnj[] pnjs;
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
        Item[] items;
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

    double X1 = 0;
    double Y1 = 0;
    double X2 = 0;
    double Y2 = 0;
    private void gameLoop(StackPane gameView) {
        displayPnjs(gameView);
        displayItems(gameView);
        time.update(player);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                if (!endMenu.isLoaded()) {
                    player.updateTime(map);
                    time.update(player);
                    weakness.update(player, gameView);
                    hunger.update(player, gameView);
                }

                if (!endMenu.isLoaded() && !isDevMode)
                    endMenu.checkEndGame(gameView, player, music);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        gameView.setOnMouseMoved(e -> {
            if (isDevMode) {
                devEngine.listenMouse(e);
            }
        });

        // on scroll
        gameView.setOnScroll(e -> {
            if (isDevMode && !isAltPressed) {
                devEngine.imgMouseControler.setMode((devEngine.imgMouseControler.getMode() + 1) % 2, gameView);
            } else if (isDevMode) {
                if (e.getDeltaY() > 0) {
                    devEngine.imgMouseControler.changeSelected((devEngine.imgMouseControler.getSelected() - 1 + devEngine.imgMouseControler.getAllImgItem().size()) % devEngine.imgMouseControler.getAllImgItem().size());
                } else {
                    devEngine.imgMouseControler.changeSelected((devEngine.imgMouseControler.getSelected() + 1) % devEngine.imgMouseControler.getAllImgItem().size());
                }
            }
        });

        gameView.setOnMousePressed((MouseEvent e) -> {
            double clickX = e.getSceneX();
            double clickY = e.getSceneY();
            javafx.geometry.Point2D clickPoint = map.getMapContainer().sceneToLocal(clickX, clickY);
//            Rectangle previewRect = new Rectangle();
            ImageView item = new ImageView(new Image("file:assets/items/distributeur.png"));

            if(e.isPrimaryButtonDown()){
                X1 = clickPoint.getX();
                Y1 = clickPoint.getY();

                System.out.println(X1+ ", "+ Y1);

                item.setLayoutX(X1 - item.getImage().getWidth()/2);
                item.setLayoutY(Y1 - item.getImage().getHeight()/2);
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
                    mapContainer.getChildren().remove(item);
                });
            } else if (e.isSecondaryButtonDown()) {
//                mapContainer.getChildren().remove(previewRect);
//                X2 = clickPoint.getX();
//                Y2 = clickPoint.getY();

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

                        // Ajouter le rectangle à la map
                        item.setLayoutX(X1);
                        item.setLayoutY(Y1);
                        map.getItems().add(obstaclesList.getLast());
                        mapContainer.getChildren().add(item);


                        // enlever le rectangle de la map

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                X1 = X2 = Y1 = Y2 = 0;
            }
        });
        gameView.setOnKeyPressed(e -> {
            devEngine.listenKey(e);

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

                menuControler.setCurrentAction(0);
                player.setStoreItem(null);
                profileMenu.remove(gameView);
            }

            if (e.getCode() == KeyCode.ALT) {
                isAltPressed = true;
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
                menuControler.displayInteractiveMenu(this.itemToInteract, mapContainer);
                menuControler.displayActionSelected(this.itemToInteract, mapContainer);
            }

            if (isMoveKey(e.getCode()) && !profileMenu.isLoaded() && !endMenu.isLoaded()) {
                if (this.isInteracting) {
                    menuControler.moveSelected(e, this.itemToInteract);
                    menuControler.displaySecondMenu(this.itemToInteract, mapContainer);
                    menuControler.displayActionSelected(this.itemToInteract, mapContainer);
                } else {
                    player.move(map.getMapView(), gameView, true, e);
                }
            }

            if (e.getCode() == KeyCode.ENTER && this.isInteracting) {
                menuControler.doActionOnEnter(player, this.itemToInteract, gameView);
            }

            if (e.getCode() == KeyCode.P && !this.isInteracting && !profileMenu.isLoaded() && !endMenu.isLoaded()) {
                profileMenu.show(gameView, player);
            }
        });

        gameView.setOnKeyReleased(e -> {
            if (isMoveKey(e.getCode())) {
                player.move(map.getMapView(), gameView, false, e);
            }

            if (e.getCode() == KeyCode.ALT) {
                isAltPressed = false;
            }
        });

    }

    public void start(Stage primaryStage, boolean devMode) {
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

        if (devMode) {
            this.isDevMode = true;
            devEngine.displayDevMode(gameView, player, map);
        } else {
            weakness.display(gameView);
            hunger.display(gameView);
            time.display(gameView);
        }
        this.gameLoop(gameView);
        gameView.requestFocus();
    }
}
