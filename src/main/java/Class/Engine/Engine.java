package Class.Engine;

import Class.Character.Player;
import Class.Map.Map;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Engine extends Application {
    private Player player;
    private Map map;

    //Constructor
    public Engine(){
        this.player = new Player("Character", new Image("file:assets/perso/animtest1.png"));
        this.map = new Map("Map", new ImageView(new Image("file:assets/map/mapTest.png")), true, new ArrayList<>(), new ArrayList<>());

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

    private void gameLoop(StackPane gameView) {
        player.move(map.getMapView(), gameView);
//        gameView.setOnMouseClicked((MouseEvent e) -> onClick(e));
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> player.updateTime(map)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void start(Stage primaryStage) {
        StackPane gameView = new StackPane(map.getMapContainer(), player.getPersoView());
        gameView.setPrefSize(1080, 720);

        Scene scene = new Scene(gameView);
        this.gameLoop(gameView);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Epitech Simulator");
        primaryStage.show();
        gameView.requestFocus();

    }
}
