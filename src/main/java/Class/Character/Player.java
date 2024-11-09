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
    private int nbHoursOfSearch;

    private double fun;
    private double weakness;
    private double hunger;

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
        this.nbHoursOfSearch = 0;
        this.fun = 0;
        this.weakness = 0;
        this.hunger = 10;
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
    public int getNbHoursOfSearch(){
        return this.nbHoursOfSearch;
    }
    public double getFun(){
        return this.fun;
    }
    public double getWeakness(){
        return this.weakness;
    }
    public double getHunger(){
        return this.hunger;
    }
    public List<Skill> getSkills(){
        return this.skills;
    }

    public Skill getSkill(String name) {
        for (Skill skill : this.skills) {
            if (skill.getName().equals(name)) {
                return skill;
            }
        }
        return null;
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

    public void addTimeOfSearch(int hours){
        this.nbHoursOfSearch += hours;
    }

    public void updateStats(Map map) {
        double fatigueMultiplier = (this.weakness / 10.0) + 1;
        double hungerMultiplier = (this.hunger / 50.0) + 1;

        if (map.getIsNight()) {
            this.weakness += 3 * fatigueMultiplier;
        } else {
            this.weakness += 1 * fatigueMultiplier;
        }
        this.hunger -= 1 * hungerMultiplier;

        this.weakness = Math.clamp(this.weakness, 0, 60);
        this.hunger = Math.clamp(this.hunger, 0, 10);
    }

    private void timeLogic() {
        if (this.timeHours == 24) {
            this.timeHours = 0;
            this.timeDays++;
        }
        if (this.timeDays == 60) {
            this.timeDays = 0;
            this.timeYears++;
        }
    }

    public void updateTime(Map map) {
        this.timeHours++;
        if (this.timeHours >= 20 || this.timeHours < 6) {
            if (!map.getIsNight())
                map.setIsNight(true);
        } else if (map.getIsNight()) {
            map.setIsNight(false);
        }

        timeLogic();

        updateStats(map);
    }

    public void addTime(int hours) {
        this.timeHours += hours;
        timeLogic();
    }

    public void addSkill(Skill skill) {
        this.skills.add(skill);
    }

    public void setSkills(Skill argSkills) {
        for (Skill skill : this.skills) {
            if (skill.getName().equals(argSkills.getName())) {
                skill.setLevel(argSkills.getLevel());
                return;
            }
        }
    }

    private Timeline checkKey(KeyCode code) {
        if (code == KeyCode.UP || code == KeyCode.Z) {
            return timelineUP;
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            return timelineDOWN;
        } else if (code == KeyCode.LEFT || code == KeyCode.Q) {
            return timelineLEFT;
        } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            return timelineRIGHT;
        }

        return null;
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
            Timeline timeline = checkKey(e.getCode());
            timeline.play();
        });
        gameView.setOnKeyReleased((KeyEvent e) -> {
            Timeline timeline = checkKey(e.getCode());
            timeline.stop();
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
