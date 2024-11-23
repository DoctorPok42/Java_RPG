package Game;

import Class.Engine.Engine;
import Class.Menu.Start;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Game extends Application {
    private final Start startMenu = new Start();
    private int currentMenu = 0;

    public static void main(String[] args) {
        launch(args);
    }

    public void startGame(Stage primaryStage, StackPane root) throws InterruptedException, IOException {
        root.getChildren().clear();
        Engine engine = new Engine();
        engine.start(primaryStage, currentMenu == 2);
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        primaryStage.setTitle("Epitech Simulator");
        primaryStage.getIcons().add(new Image("file:assets/logo.png"));
        primaryStage.setResizable(false);
        root.setPrefSize(1280, 720);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        root.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case Z:
                case UP:
                    this.startMenu.up();
                    break;
                case S:
                case DOWN:
                    this.startMenu.down();
                    break;
                case ENTER:
                    this.currentMenu = this.startMenu.selectOption();
                    break;
            }

            if (this.currentMenu == 1 || this.currentMenu == 2) {
                try {
                    startGame(primaryStage, root);
                } catch (InterruptedException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        this.startMenu.show(root);
        root.requestFocus();
    }
}
