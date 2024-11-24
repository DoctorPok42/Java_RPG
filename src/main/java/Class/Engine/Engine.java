package Class.Engine;

import Class.Character.Player;
import Class.Character.Pnj;
import Class.Character.PnjTypeAdapter;
import Class.DevMode.DevEngine;
import Class.Item.*;
import Class.Map.Fog;
import Class.Map.Map;
import Class.Menu.End;
import Class.Menu.Profile;
import Class.Music.Music;
import Class.bar.Feed;
import Class.bar.Money;
import Class.bar.Time;
import Class.bar.Tiredness;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Engine {
    private final Player player;
    private final Map map;
    private final Pane mapContainer;
    private final ImageView interactImg;
    private Item itemToInteract;
    private boolean canInteract;
    private boolean isInteracting;
    private boolean snaking = false;
    private Snake snake;
    private final Tiredness weakness;
    private final Feed hunger;
    private final Time time;
    private final Money money;
    private final Music music;
    private final Profile profileMenu;
    private final End endMenu;
    private final MenuControler menuControler;
    private boolean isDevMode = false;
    private final DevEngine devEngine;
    private boolean isAltPressed = false;
    private boolean isCtrlPressed = false;
    private final Text pnjName = new Text();

    //Constructor
    public Engine() throws IOException {
        this.map = new Map("Map", new ImageView(new Image("file:assets/map/mapfinale.png")), true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        this.player = new Player("John Doe", new Image("file:assets/perso/animtest1.png"), this.map);
        this.weakness = new Tiredness("Tiredness");
        this.hunger = new Feed("Hunger");
        this.time = new Time("Time");
        this.money = new Money("Money");
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

        this.pnjName.setStyle("-fx-font-size: 13;" + "-fx-font-family: 'Press Start 2P';");
        this.pnjName.setFill(javafx.scene.paint.Color.WHITE);
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
                map.getItems().add(new PnjInteraction(pnj.getName(), ItemType.PNJ, (float) pnj.getPosX(), (float) pnj.getPosY(), 1, -1, pnj, pnj.getId()));
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
                            map.getItems().add(new Pc(item.getName(), item.getType(), item.getX(), item.getY(), item.getZ(), item.getSkin(), item.getId()));
                            break;
                        case CANAP:
                            map.getItems().add(new Canap(item.getName(), item.getType(), item.getX(), item.getY(), item.getZ(), item.getSkin(), item.getId()));
                            break;
                        case DISTRIBUTOR:
                            map.getItems().add(new Distributor(item.getName(), item.getType(), item.getX(), item.getY(), item.getZ(), 0, item.getSkin(), item.getId()));
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

    public Point2D getClickPoint(MouseEvent e) {
        double clickX = e.getSceneX();
        double clickY = e.getSceneY();
        return map.getMapContainer().sceneToLocal(clickX, clickY);
    }

    @NotNull
    private Timeline getTimeline(StackPane gameView) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (!endMenu.isLoaded()) {
                player.updateTime();
                time.update(player);
                money.update(player);
                weakness.update(player, gameView);
                hunger.update(player, gameView);
            }
            if (!endMenu.isLoaded() && !isDevMode)
                endMenu.checkEndGame(gameView, player, music);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        return timeline;
    }

    private void gameLoop(StackPane gameView) {
        displayPnjs(gameView);
        displayItems(gameView);
        time.update(player);
        Timeline timeline = getTimeline(gameView);
        timeline.play();

        if (isDevMode) {
            gameView.setOnMouseMoved(devEngine::listenMouse);

            gameView.setOnScroll(e -> {
                devEngine.listenScroll(e, e.getDeltaY(), isAltPressed);
            });

            gameView.setOnMouseDragged(e -> {
                devEngine.listenMouse(e);
                devEngine.listenMouseDrag(e);
            });

            gameView.setOnMousePressed((MouseEvent e) -> {
                if ((e.isPrimaryButtonDown() || e.isSecondaryButtonDown())) {
                    try {
                        devEngine.listenMouseClick(e, getClickPoint(e));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }

        gameView.setOnKeyPressed(e -> {
            if (isDevMode)
                devEngine.listenKey(e);

            this.itemToInteract = player.getStoreItem();
            if (e.getCode() == KeyCode.ESCAPE) {
                if (!music.isPlaying())
                    music.play();

                this.isInteracting = false;
                this.itemToInteract = null;
                this.canInteract = false;
                mapContainer.getChildren().remove(interactImg);
                gameView.getChildren().remove(gameView.lookup("#dialogBox"));
                gameView.requestFocus();
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
                menuControler.removeAlert(gameView);
                player.setStoreItem(null);
                profileMenu.remove(gameView);


                snaking = false;
                gameView.getChildren().remove(gameView.lookup("#snakeWindow"));
            }

            if (e.getCode() == KeyCode.ALT) isAltPressed = true;
            if (e.getCode() == KeyCode.CONTROL) isCtrlPressed = true;

            if (this.itemToInteract != null) {
                this.canInteract = true;
                if (!mapContainer.getChildren().contains(interactImg)) {
                    interactImg.setLayoutX((double) this.itemToInteract.getWidth() / 2 + this.itemToInteract.getX() - 16);
                    interactImg.setLayoutY((double) this.itemToInteract.getHeight() / 2 + this.itemToInteract.getY() - 16);
                    mapContainer.getChildren().add(interactImg);
                }

                if (this.itemToInteract.getType() == ItemType.PNJ) {
                    PnjInteraction pnj = (PnjInteraction) this.itemToInteract;
                    pnjName.setText(pnj.getName());
                    pnjName.setLayoutX(pnj.getItemView().getLayoutX() - (pnj.getName().length() * 3.5));
                    pnjName.setLayoutY(pnj.getItemView().getLayoutY() - 10);

                    if (!mapContainer.getChildren().contains(pnjName))
                        mapContainer.getChildren().add(pnjName);
                }
            } else {
                mapContainer.getChildren().remove(interactImg);
                mapContainer.getChildren().remove(pnjName);
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
                } else if (!this.isCtrlPressed) {
                    player.move(map.getMapView(), gameView, true, e);
                }
            }

            if (e.getCode() == KeyCode.ENTER && this.isInteracting) {
                menuControler.doActionOnEnter(player, this.itemToInteract, gameView, mapContainer);
                if (this.itemToInteract.getType() == ItemType.PC) {
                    Pc pc =(Pc) this.itemToInteract;
                    snaking = pc.isSnaking();
                    this.snake = pc.getSnake();
                }
            }
            if (snaking){
                snake.play(e.getCode());
                if (snake.isGameOver()&&e.getCode()==KeyCode.ENTER){
                    snake.resetGame();
                }
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
        music.play();

//        new Fog(gameView, player);

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
            money.display(gameView);
        }
        this.gameLoop(gameView);
        gameView.requestFocus();
    }
}
