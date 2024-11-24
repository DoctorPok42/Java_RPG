package Class.Map;

import Class.Character.Player;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Fog {
    private final Pane mapContainer;
    private final Player player;

    public Fog(Pane mapContainer, Player player) {
        this.mapContainer = mapContainer;
        this.player = player;

        initializeFogEffect();
    }

    private void initializeFogEffect() {
        double mapWidth = 1280;
        double mapHeight = 720;
        double visibilityRadius = 300;

        Canvas fogCanvas = new Canvas(mapWidth, mapHeight);
        GraphicsContext gc = fogCanvas.getGraphicsContext2D();

        drawFog(gc, mapWidth, mapHeight, player.getPlayerView().getLayoutX(), player.getPlayerView().getLayoutY(), visibilityRadius);

        mapContainer.getChildren().add(fogCanvas);

        player.getPlayerView().layoutXProperty().addListener((obs, oldX, newX) ->
                drawFog(gc, mapWidth, mapHeight, newX.doubleValue(), player.getPlayerView().getLayoutY(), visibilityRadius));

        player.getPlayerView().layoutYProperty().addListener((obs, oldY, newY) ->
                drawFog(gc, mapWidth, mapHeight, player.getPlayerView().getLayoutX(), newY.doubleValue(), visibilityRadius));
    }

    private void drawFog(GraphicsContext gc, double width, double height, double playerX, double playerY, double radius) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        gc.clearRect(playerX - radius, playerY - radius, radius * 2, radius * 2);
    }
}
