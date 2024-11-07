package Game;

import Class.Engine.Engine;
import javafx.application.Application;
import javafx.stage.Stage;

public class Game extends Application {
    private Engine engine;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        engine = new Engine();
        engine.start(primaryStage);
    }
}
