package test;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import java.awt.*;

import static javafx.application.Application.launch;


public class PatientZero extends Application {

    private double mapTranslateX = 0;
    private double mapTranslateY = 0;
    private double viewHeight = 720;
    private double viewWidth = 1280;
    private double speed = 200;
    private double movestep = 4;
    private Timeline timelineUP, timelineDOWN, timelineLEFT, timelineRIGHT, animationTimeline;
    private List<Rectangle> obstacles;
    private Pane mapContainer ;
    private javafx.geometry.Point2D persoCoord;
    private Rectangle persoHitbox;

    private Image[] walkimage;
    private int currentImageIndex = 0;
    private ImageView persoView;

    @Override
    public void start(Stage primaryStage) {
        ImageView mapView = new ImageView(new Image("file:assets/map/vraimenttestmap.png"));
        double mapWidth = mapView.getImage().getWidth();
        double mapHeight = mapView.getImage().getHeight();

        walkimage = new Image[]{
                new Image("file:assets/perso/soper.png"),
                new Image("file:assets/perso/animtest2.png"),
                new Image("file:assets/perso/animtest3.png"),
                new Image("file:assets/perso/animtest4.png"),
                new Image("file:assets/perso/animtest5.png"),
                new Image("file:assets/perso/animtest6.png")
        };


        persoView = new ImageView(walkimage[0]);
        persoView.setFitWidth(37.5);
        persoView.setFitHeight(50);

        persoView.setLayoutX(viewWidth / 2 - persoView.getFitWidth() / 2);
        persoView.setLayoutY(viewHeight / 2 - persoView.getFitHeight() / 2);

        mapContainer = new Pane(mapView);

        mapContainer.setTranslateX(viewWidth / 2 - mapWidth / 2);
        mapContainer.setTranslateY(viewHeight / 2 - mapHeight / 2);
        mapTranslateX = viewWidth / 2 - mapWidth / 2;
        mapTranslateY = viewHeight / 2 - mapHeight / 2;

        StackPane gameView = new StackPane(mapContainer, persoView);
        gameView.setPrefSize(viewWidth, viewHeight);
//        StackPane root = new StackPane(gameView);

        persoCoord = mapContainer.sceneToLocal(persoView.getLayoutX(), persoView.getLayoutY());

        obstacles =new ArrayList<>();
        createObstacles();
        mapContainer.getChildren().addAll(obstacles);

        //hitbox

        persoHitbox = new Rectangle(persoCoord.getX(), persoCoord.getY(), persoView.getFitWidth(), persoView.getFitHeight());
        persoHitbox.setFill(Color.TRANSPARENT);
        persoHitbox.setStroke(Color.YELLOW);
        persoHitbox.setStrokeWidth(2);
        mapContainer.getChildren().add(persoHitbox);
//        persoView.setTranslateX(0);
//        persoView.setTranslateY(0);

        timelineUP = createMovementTimeline(0, movestep, mapContainer);
        timelineDOWN = createMovementTimeline(0, -movestep, mapContainer);
        timelineLEFT = createMovementTimeline(movestep, 0, mapContainer);
        timelineRIGHT = createMovementTimeline(-movestep, 0, mapContainer);

        animationTimeline = new Timeline(new KeyFrame(Duration.millis(200),e->animateCharacter()));
        animationTimeline.setCycleCount(Animation.INDEFINITE);

        gameView.setOnKeyPressed((KeyEvent e) -> {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.Z) {
                timelineUP.play();
                startCharacterAnimation();
            } else if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) {
                timelineDOWN.play();
                startCharacterAnimation();
            } else if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.Q) {
                timelineLEFT.play();
                startCharacterAnimation();
            } else if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) {
                timelineRIGHT.play();
                startCharacterAnimation();
            }
        });
        gameView.setOnKeyReleased((KeyEvent e) -> {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.Z) {
                timelineUP.stop();
            } else if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) {
                timelineDOWN.stop();
            } else if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.Q) {
                timelineLEFT.stop();
            } else if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) {
                timelineRIGHT.stop();
            }
            stopCharacterAnimation();
            System.out.println("X: " + persoCoord.getX());
            System.out.println("Y: " + persoCoord.getY());
        });

        TranslateTransition mapTransition = new TranslateTransition(Duration.seconds(0), mapView);
        //Mouse controls
//        gameView.setOnMousePressed((MouseEvent e) -> {
//            if (!mapTransition.getDuration().equals(Duration.ZERO)) {
//                mapTransition.stop();
//            }
//            double clickX = e.getX();
//            System.out.println("X: " + clickX);
//            double clickY = e.getY();
//            System.out.println("Y: " + clickY);
//            double deltaX = viewWidth / 2 - clickX;
//            double deltaY = viewHeight / 2 - clickY;
//            mapTranslateX += deltaX;
//            mapTranslateY += deltaY;
//            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
//            double durationSec = distance / speed;
//            mapTransition.setDuration(Duration.seconds(durationSec));
//            mapTransition.setToX(mapTranslateX);
//            mapTransition.setToY(mapTranslateY);
//            mapTransition.setCycleCount(1);
//            mapTransition.setAutoReverse(false);
//            mapTransition.play();
//        });
        gameView.setOnMousePressed((MouseEvent e) ->{
            double clickX = e.getSceneX();
            double clickY = e.getSceneY();
            javafx.geometry.Point2D clickPoint = mapContainer.sceneToLocal(clickX, clickY);

            System.out.println("X: " + clickPoint.getX());
            System.out.println("Y: " + clickPoint.getY());
                });

        Scene scene = new Scene(gameView, viewWidth, viewHeight);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Patient Zero");
        primaryStage.setResizable(false);
        primaryStage.show();

        gameView.requestFocus();
    }

    private Timeline createMovementTimeline(double x, double y, Pane mapContainer) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(16), event -> {
//                    double newTranslateX = mapTranslateX + x;
//                    double newTranslateY = mapTranslateY + y;
                    javafx.geometry.Point2D targetCoord = mapContainer.sceneToLocal(viewWidth / 2-persoView.getFitWidth() / 2, viewHeight / 2-persoView.getFitHeight() / 2);
                    Rectangle targetHitbox = new Rectangle(targetCoord.getX()-x, targetCoord.getY()-y, persoView.getFitWidth(), persoView.getFitHeight());
                    mapContainer.getChildren().add(targetHitbox);
                    targetHitbox.setFill(Color.TRANSPARENT);
//                    targetHitbox.setStroke(Color.RED);
//                    targetHitbox.setStrokeWidth(2);
                    if (!isCollision(targetHitbox)) {
                        mapTranslateX = mapTranslateX + x;
                        mapTranslateY = mapTranslateY + y;
                        mapContainer.setTranslateX(mapTranslateX);
                        mapContainer.setTranslateY(mapTranslateY);
                        persoCoord = mapContainer.sceneToLocal(viewWidth / 2-persoView.getFitWidth() / 2, viewHeight / 2-persoView.getFitHeight() / 2);
                        persoHitbox.setX(persoCoord.getX());
                        persoHitbox.setY(persoCoord.getY());
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        return timeline;
    }

    private boolean isCollision(Rectangle targetHitbox) {
        for (Rectangle obstacle : obstacles) {
            if (targetHitbox.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                return true;
            }
        }
        return false;
    }

    private void createObstacles(){
        Rectangle obstaccle1 = new Rectangle(1548, 965, 92, 48);
        obstaccle1.setFill(new ImagePattern(new Image("file:assets/items/canap.png")));
        obstacles.add(obstaccle1);

        Rectangle obstaccle2 = new Rectangle(2027, 958, 350, 90);
        obstaccle2.setFill(Color.BLUE);
        obstacles.add(obstaccle2);
    }

    private void animateCharacter() {
        currentImageIndex = (currentImageIndex + 1) % walkimage.length;
        persoView.setImage(walkimage[currentImageIndex]);
    }

    private void startCharacterAnimation() {
        if(animationTimeline.getStatus() == Animation.Status.STOPPED)
            animationTimeline.play();
    }

    private void stopCharacterAnimation() {
        if(animationTimeline.getStatus() == Animation.Status.RUNNING)
            animationTimeline.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
