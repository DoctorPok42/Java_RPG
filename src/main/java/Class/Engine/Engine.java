package Class.Engine;

import Class.Character.Character;
import Class.Character.Player;
import Class.Character.role;
import Class.Map.Map;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.application.Application;
import javafx.stage.Stage;

public class Engine extends Application {
    private List<KeyCode> keys;
    private Player player;
    private Map map;

    private boolean isMoveKey(KeyCode key) {
        return key == KeyCode.UP || key == KeyCode.DOWN || key == KeyCode.LEFT || key == KeyCode.RIGHT || key == KeyCode.Z || key == KeyCode.Q || key == KeyCode.S || key == KeyCode.D;
    }

    private void gameLoop(StackPane gameView) {
        while (true) {
            gameView.setOnKeyPressed((KeyEvent e) -> {
                if (isMoveKey(e.getCode())) {
                    player.move(e, map.getMapView());
                }
            });

            gameView.setOnMouseClicked((MouseEvent e) -> {
                double x = e.getX();
                double y = e.getY();
                System.out.println("Mouse clicked at: " + x + ", " + y);
            });
        }
    }

    public void start(Stage primaryStage) {
        this.player = new Player("Character", new Image("file:assets/perso/animtest1.png"));
        this.keys = new ArrayList<>();
        this.map = new Map("Map", new ImageView(new Image("file:assets/map/mapTest.png")), true, new ArrayList<>(), new ArrayList<>());
        StackPane gameView = new StackPane(map.getMapContainer(), player.getPersoView());
        gameView.setPrefSize(800, 400);

        Scene scene = new Scene(gameView);
        primaryStage.setScene(scene);
        primaryStage.setTitle("fyghkjkl");
        primaryStage.show();

        this.gameLoop(gameView);
    }
}
