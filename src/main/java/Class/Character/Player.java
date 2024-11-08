package Class.Character;

import Class.Skill.Skill;
import Class.Map.Map;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.List;

public class Player extends Character {
    private int money;
    private int timeDays;
    private int timeYears;
    private int timeHours;

    private int fun;
    private int weakness;
    private int hunger;

    private List<Skill> skills;
    double mapTranslateX = 0;
    double mapTranslateY = 0;

    private Timeline timelineUP;
    private Timeline timelineDOWN;
    private Timeline timelineLEFT;
    private Timeline timelineRIGHT;
    private Timeline animationTimeline;
    private final double movestep = 2;

    //Constructor
    public Player(String name, Image texture) {
        super(name, role.PLAYER, new ImageView(texture));
        this.money = 0;
        this.timeDays = 0;
        this.timeYears = 0;
        this.timeHours = 0;
        this.fun = 0;
        this.weakness = 0;
        this.hunger = 0;
    }

    //Getter
    public int getMoney(){
        return this.money;
    }
    public int getTimeDays(){
        return this.timeDays;
    }
    public int getTimeYears(){
        return this.timeYears;
    }
    public int getTimeHours(){
        return this.timeHours;
    }
    public int getFun(){
        return this.fun;
    }
    public int getWeakness(){
        return this.weakness;
    }
    public int getHunger(){
        return this.hunger;
    }

    //Method
    void interact(){
        //interact with...
    }
    void profile(){
        //display profile
    }
    void menu(){
        //display menu
    }

    public void updateTime(Map map) {
        this.timeHours++;
        if (this.timeHours >= 20 || this.timeHours < 6) {
            if (!map.getIsNight())
                map.setIsNight(true);
        } else if (map.getIsNight()) {
            map.setIsNight(false);
        }

        if (this.timeHours == 24) {
            this.timeHours = 0;
            this.timeDays++;
        }
        if (this.timeDays == 60) {
            this.timeDays = 0;
            this.timeYears++;
        }
    }
    //Move method
    @Override
    public void move(ImageView mapView, StackPane gameView) {
        //def timeline
        timelineUP = createMovementTimeline(0, movestep, mapView);
        timelineDOWN = createMovementTimeline(0, -movestep, mapView);
        timelineLEFT = createMovementTimeline(movestep, 0, mapView);
        timelineRIGHT = createMovementTimeline(-movestep, 0, mapView);
        //move
        gameView.setOnKeyPressed((KeyEvent e) -> {
        if (e.getCode() == KeyCode.UP|| e.getCode() == KeyCode.Z) {
            timelineUP.play();
        } else if (e.getCode() == KeyCode.DOWN|| e.getCode() == KeyCode.S) {
            timelineDOWN.play();
        } else if (e.getCode() == KeyCode.LEFT|| e.getCode() == KeyCode.Q) {
            timelineLEFT.play();
        } else if (e.getCode() == KeyCode.RIGHT|| e.getCode() == KeyCode.D) {
            timelineRIGHT.play();
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
    });
    }
    //Create movement timeline
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


}
