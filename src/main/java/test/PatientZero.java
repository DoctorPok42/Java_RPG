package test;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import static javafx.application.Application.launch;

public class PatientZero extends Application {

    private double mapTranslateX = 0;
    private double mapTranslateY = 0;
    private double viewHeight = 400;
    private double viewWidth = 800;
    private double speed = 200;
    private double movestep = 2;
    private Timeline timelineUP, timelineDOWN, timelineLEFT, timelineRIGHT, animationTimeline;

    private Image[] walkimage;
    private int currentImageIndex = 0;
    private ImageView persoView;

    @Override
    public void start(Stage primaryStage) {
        ImageView mapView = new ImageView(new Image("file:assets/map/mapTest.png"));

        walkimage = new Image[]{
                new Image("file:assets/perso/animtest1.png"),
                new Image("file:assets/perso/animtest2.png"),
                new Image("file:assets/perso/animtest3.png"),
                new Image("file:assets/perso/animtest4.png"),
                new Image("file:assets/perso/animtest5.png"),
                new Image("file:assets/perso/animtest6.png")
        };

        StackPane mapContainer = new StackPane(mapView);

        persoView = new ImageView(walkimage[0]);
        persoView.setFitWidth(50);
        persoView.setFitHeight(50);

        Pane mapCont = new Pane(mapView);
        StackPane gameView = new StackPane(mapCont, persoView);
        gameView.setPrefSize(viewWidth, viewHeight);

        persoView.setTranslateX(0);
        persoView.setTranslateY(0);

        timelineUP = createMovementTimeline(0, movestep, mapView);
        timelineDOWN = createMovementTimeline(0, -movestep, mapView);
        timelineLEFT = createMovementTimeline(movestep, 0, mapView);
        timelineRIGHT = createMovementTimeline(-movestep, 0, mapView);

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
        });

        TranslateTransition mapTransition = new TranslateTransition(Duration.seconds(0), mapView);
        //Mouse controls
        gameView.setOnMousePressed((MouseEvent e) -> {
            if (!mapTransition.getDuration().equals(Duration.ZERO)) {
                mapTransition.stop();
            }
            double clickX = e.getX();
            double clickY = e.getY();
            double deltaX = viewWidth / 2 - clickX;
            double deltaY = viewHeight / 2 - clickY;
            mapTranslateX += deltaX;
            mapTranslateY += deltaY;
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double durationSec = distance / speed;
            mapTransition.setDuration(Duration.seconds(durationSec));
            mapTransition.setToX(mapTranslateX);
            mapTransition.setToY(mapTranslateY);
            mapTransition.setCycleCount(1);
            mapTransition.setAutoReverse(false);
            mapTransition.play();
        });

        Scene scene = new Scene(gameView);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Patient Zero");
        primaryStage.show();

        gameView.requestFocus();
    }

    private Timeline createMovementTimeline(double x, double y, ImageView mapView) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(16), event -> {
                    mapTranslateX += x;
                    mapTranslateY += y;
                    mapView.setTranslateX(mapTranslateX);
                    mapView.setTranslateY(mapTranslateY);
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        return timeline;
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
