package Class.Item;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.skin.TextInputControlSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Snake {

    private StackPane gameView;
    private boolean snaking;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int TILE_SIZE = 20;


    private Direction direction = Direction.RIGHT;


    private final List<Point> ssnake = new ArrayList<>();
    private boolean gameOver = false;


    private Point food;

    private int score = 0;


    private final Random random = new Random();

    GraphicsContext gc;
    Label scoreLabel;

    public Snake(StackPane gameView) {
        snaking = true;
        this.gameView = gameView;
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        this.gc = canvas.getGraphicsContext2D();

        this.scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-font-family:'Press Start 2P';-fx-font-size: 20; -fx-text-fill: white;");
        HBox scoreBox = new HBox(scoreLabel);
        scoreBox.setPrefWidth(WIDTH);
        scoreBox.setMaxWidth(WIDTH);
        scoreBox.setStyle("-fx-background-color: black; -fx-padding: 10px;");
        scoreLabel.setAlignment(Pos.CENTER);

        VBox snakeWindow = new VBox();
        snakeWindow.getChildren().addAll(scoreBox,canvas);
        snakeWindow.setId("snakeWindow");


        ssnake.add(new Point(5, 5));
        ssnake.add(new Point(4, 5));
        ssnake.add(new Point(3, 5));
        spawnFood();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(150), e -> {
            if (!gameOver) {
                update();
                render(gc, scoreLabel);
            } else {
                renderGameOver(gc);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        snakeWindow.setPrefSize(WIDTH, HEIGHT);
        snakeWindow.setMaxSize(WIDTH, HEIGHT);
        StackPane.setAlignment(snakeWindow, Pos.CENTER);
        gameView.getChildren().add(snakeWindow);
    }

    public void play(KeyCode code){
        if (code == KeyCode.UP || code == KeyCode.Z && direction != Direction.DOWN) {
            direction = Direction.UP;
        } else if (code == KeyCode.DOWN || code == KeyCode.S && direction != Direction.UP) {
            direction = Direction.DOWN;
        } else if (code == KeyCode.LEFT || code == KeyCode.Q && direction != Direction.RIGHT) {
            direction = Direction.LEFT;
        } else if (code == KeyCode.RIGHT || code == KeyCode.D && direction != Direction.LEFT) {
            direction = Direction.RIGHT;
        }
    }

    private void update() {
        Point head = ssnake.get(0);
        Point newHead = switch (direction) {
            case UP -> new Point(head.x, head.y - 1);
            case DOWN -> new Point(head.x, head.y + 1);
            case LEFT -> new Point(head.x - 1, head.y);
            case RIGHT -> new Point(head.x + 1, head.y);
        };

        if (newHead.x < 0 || newHead.y < 0 || newHead.x >= WIDTH / TILE_SIZE || newHead.y >= HEIGHT / TILE_SIZE
                || ssnake.contains(newHead)) {
            gameOver = true;
            return;
        }

        ssnake.add(0, newHead);

        if (newHead.equals(food)) {
            spawnFood();
            score++;
        } else {
            ssnake.remove(ssnake.size() - 1);
        }
    }
    private void render(GraphicsContext gc, Label scoreLabel) {
        scoreLabel.setText("Score: " + score);

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        gc.setFill(Color.RED);
        gc.fillOval(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        gc.setFill(Color.GREEN);
        for (Point p : ssnake) {
            gc.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }
    private void renderGameOver(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        gc.setFill(Color.RED);
        gc.fillText("GAME OVER," + "\nScore: "+score+ "\nPress Enter to play again.", WIDTH / 2 - 50, HEIGHT / 2);

    }
    private void spawnFood() {
        int x;
        int y;
        do {
            x = random.nextInt(WIDTH / TILE_SIZE);
            y = random.nextInt(HEIGHT / TILE_SIZE);
        } while (ssnake.contains(new Point(x, y)));

        food = new Point(x, y);
    }

    public void resetGame() {
        ssnake.clear();
        ssnake.add(new Point(5, 5));
        ssnake.add(new Point(4, 5));
        ssnake.add(new Point(3, 5));
        direction = Direction.RIGHT;
        spawnFood();
        score = 0;
        gameOver = false;

        render(this.gc, this.scoreLabel);
    }
    public boolean isGameOver() {
        return gameOver;
    }


    private static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point other) {
            return this.x == other.x && this.y == other.y;
        }
        return false;
        }
    }


    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}

