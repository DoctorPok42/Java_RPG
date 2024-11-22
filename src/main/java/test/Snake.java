package test;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Snake extends Application {

    // Dimensions de la fenêtre
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int TILE_SIZE = 20;

    // Direction initiale
    private Direction direction = Direction.RIGHT;

    // Corps du serpent
    private final List<Point> snake = new ArrayList<>();
    private boolean gameOver = false;

    // Position de la pomme
    private Point food;

    private int score = 0;

    // Random pour positionner la pomme
    private final Random random = new Random();

    @Override
    public void start(Stage primaryStage) {
        // Création de la fenêtre
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Label pour afficher le score
        Label scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-font-size: 20; -fx-text-fill: white;");
        HBox scoreBox = new HBox(scoreLabel);
        scoreBox.setStyle("-fx-background-color: black; -fx-padding: 10px;");
        scoreLabel.setAlignment(Pos.CENTER);

        VBox root = new VBox();
        root.getChildren().addAll(scoreBox,canvas);

        // Initialisation du serpent
        snake.add(new Point(5, 5));
        snake.add(new Point(4, 5));
        snake.add(new Point(3, 5));
        spawnFood();

        // Animation
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(150), e -> {
            if (!gameOver) {
                update();
                render(gc, scoreLabel);
            } else {
                renderGameOver(gc, scoreLabel);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Contrôles clavier
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.Z && direction != Direction.DOWN) {
                direction = Direction.UP;
            } else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S && direction != Direction.UP) {
                direction = Direction.DOWN;
            } else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.Q && direction != Direction.RIGHT) {
                direction = Direction.LEFT;
            } else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D && direction != Direction.LEFT) {
                direction = Direction.RIGHT;
            }
        });

        // Configuration de la fenêtre
        primaryStage.setScene(scene);
        primaryStage.setTitle("Snake Game");
        primaryStage.show();
    }

    private void update() {
        // Déplacement du serpent
        Point head = snake.get(0);
        Point newHead = switch (direction) {
            case UP -> new Point(head.x, head.y - 1);
            case DOWN -> new Point(head.x, head.y + 1);
            case LEFT -> new Point(head.x - 1, head.y);
            case RIGHT -> new Point(head.x + 1, head.y);
        };

        // Vérifier les collisions
        if (newHead.x < 0 || newHead.y < 0 || newHead.x >= WIDTH / TILE_SIZE || newHead.y >= HEIGHT / TILE_SIZE
                || snake.contains(newHead)) {
            gameOver = true;
            return;
        }

        // Ajouter la nouvelle tête
        snake.add(0, newHead);

        // Vérifier si le serpent a mangé la pomme
        if (newHead.equals(food)) {
            spawnFood();
            score++;
        } else {
            snake.remove(snake.size() - 1); // Supprimer la queue si pas de pomme mangée
        }
    }

    private void render(GraphicsContext gc, Label scoreLabel) {
        scoreLabel.setText("Score: " + score);

        // Effacer l'écran
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Dessiner la pomme
        gc.setFill(Color.RED);
        gc.fillOval(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Dessiner le serpent
        gc.setFill(Color.GREEN);
        for (Point p : snake) {
            gc.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    private void renderGameOver(GraphicsContext gc, Label scoreLabel) {
        scoreLabel.setText("Score: " + score);
        scoreLabel.setAlignment(Pos.CENTER);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        gc.setFill(Color.RED);
        gc.fillText("GAME OVER", WIDTH / 2 - 50, HEIGHT / 2);
    }

    private void spawnFood() {
        int x, y;
        do {
            x = random.nextInt(WIDTH / TILE_SIZE);
            y = random.nextInt(HEIGHT / TILE_SIZE);
        } while (snake.contains(new Point(x, y))); // Ne pas placer la pomme sur le serpent

        food = new Point(x, y);
    }

    // Point pour représenter les positions
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

    // Enum pour représenter les directions
    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public static void main(String[] args) {
        launch(args);
    }
}
