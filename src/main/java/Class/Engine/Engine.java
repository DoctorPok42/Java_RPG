package Class.Engine;

import Class.Character.Player;
import Class.Map.Map;
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

public class Engine extends Application {
    private Player player;
    private Map map;

    private boolean isMoveKey(KeyCode key) {
        return key == KeyCode.UP || key == KeyCode.DOWN || key == KeyCode.LEFT || key == KeyCode.RIGHT || key == KeyCode.Z || key == KeyCode.Q || key == KeyCode.S || key == KeyCode.D;
    }

    private void onClick(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
    }

    private void gameLoop(StackPane gameView) {
        gameView.setOnKeyPressed((KeyEvent e) -> {
            if (isMoveKey(e.getCode())) {
                System.out.println("key: " + e.getCode());
                player.move(e, map.getMapView());
            }
        });
        gameView.setOnMouseClicked((MouseEvent e) -> onClick(e, logger));
        gameView.requestFocus();
    }

    public void start(Stage primaryStage) {
        this.player = new Player("Character", new Image("file:assets/perso/animtest1.png"));
        this.map = new Map("Map", new ImageView(new Image("file:assets/map/mapTest.png")), true, new ArrayList<>(), new ArrayList<>());
        StackPane gameView = new StackPane(map.getMapContainer(), player.getPersoView());
        gameView.setPrefSize(1080, 720);

        Scene scene = new Scene(gameView);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Epitech Simulator");
        primaryStage.show();

        this.gameLoop(gameView);
    }
}
