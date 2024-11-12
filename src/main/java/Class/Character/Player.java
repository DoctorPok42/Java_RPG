package Class.Character;

import Class.Skill.Skill;
import Class.Map.Map;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    private Timeline timelineUP;
    private Timeline timelineDOWN;
    private Timeline timelineLEFT;
    private Timeline timelineRIGHT;
    private Timeline animationTimeline;
    private final double movestep = 6;

    protected final ImageView playerView;
    private javafx.geometry.Point2D playerCoord;
    private Rectangle playerHitbox;

    //Constructor
    public Player(String name, Image texture, Map map) {
        super(name, role.PLAYER, new ImageView(texture), map);
        this.playerView = super.characView;
        this.playerView.setLayoutX(map.getViewWidth() / 2 - this.playerView.getFitWidth() / 2);
        this.playerView.setLayoutY(map.getViewHeight() / 2 - this.playerView.getFitHeight() / 2);
        this.playerCoord = map.getMapContainer().sceneToLocal(this.playerView.getLayoutX(), this.playerView.getLayoutY());
        this.setPosX(this.playerCoord.getX());
        this.setPosY(this.playerCoord.getY());

        this.money = 0;
        this.timeDays = 0;
        this.timeYears = 0;
        this.timeHours = 0;
        this.nbHoursOfSearch = 0;
        this.fun = 0;
        this.weakness = 0;
        this.hunger = 10;

        map.getMapContainer().setTranslateX(map.getViewWidth() / 2 - map.getMapWidth() / 2);
        map.getMapContainer().setTranslateY(map.getViewHeight() / 2 - map.getMapHeight() / 2);
        map.setMapTranslateX(map.getViewWidth() / 2 - map.getMapWidth() / 2);
        map.setMapTranslateY(map.getViewHeight() / 2 - map.getMapHeight() / 2);
        this.playerHitbox = new Rectangle(playerCoord.getX(),playerCoord.getY(), this.playerView.getFitWidth(), this.playerView.getFitHeight());
        this.playerHitbox.setFill(Color.TRANSPARENT);
        this.playerHitbox.setStroke(Color.YELLOW);
        this.playerHitbox.setStrokeWidth(2);
        map.addMapContainer(this.playerHitbox);

        timelineUP = createMovementTimeline(0, movestep, map);
        timelineDOWN = createMovementTimeline(0, -movestep, map);
        timelineLEFT = createMovementTimeline(movestep, 0, map);
        timelineRIGHT = createMovementTimeline(-movestep, 0, map);
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
    public ImageView getPlayerView(){
        return this.playerView;
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

    public void addMoney(int money) {
        this.money += money;
    }

    public void addFun(double fun) {
        this.fun += fun;
    }

    public void eat() {
        this.hunger += 1;
    }

    public void sleep(int hours) {
        this.weakness -= hours;
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
    public void move(ImageView mapView, StackPane gameView, boolean keyUp, KeyEvent e) {
        if (keyUp) {
            Timeline timeline = checkKey(e.getCode());
            timeline.play();
        } else {
            Timeline timeline = checkKey(e.getCode());
            timeline.stop();
        }
    }
    //Create movement timeline
    private Timeline createMovementTimeline(double x, double y, Map map) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(16), event -> {
                    javafx.geometry.Point2D targetCoord = map.getMapContainer().sceneToLocal(map.getViewWidth() / 2-this.playerView.getFitWidth() / 2, map.getViewHeight() / 2-this.playerView.getFitHeight() / 2);
                    Rectangle targetHitbox = new Rectangle(targetCoord.getX()-x, targetCoord.getY()-y, this.playerView.getFitWidth(), this.playerView.getFitHeight());
                    map.addMapContainer(targetHitbox);
                    targetHitbox.setFill(Color.TRANSPARENT);
//                    targetHitbox.setStroke(Color.RED);
//                    targetHitbox.setStrokeWidth(2);

                    if (!isCollision(targetHitbox, map)) {
                        map.setMapTranslateX(map.getMapTranslateX() + x);
                        map.setMapTranslateY(map.getMapTranslateY() + y);
                        map.getMapContainer().setTranslateX(map.getMapTranslateX());
                        map.getMapContainer().setTranslateY(map.getMapTranslateY());
                        this.playerCoord = map.getMapContainer().sceneToLocal(map.getViewWidth() / 2-this.playerView.getFitWidth() / 2, map.getViewHeight() / 2-playerView.getFitHeight() / 2);
                        playerHitbox.setX(this.playerCoord.getX());
                        playerHitbox.setY(this.playerCoord.getY());
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        return timeline;
    }
    private boolean isCollision(Rectangle targetHitbox, Map map) {
        for (Rectangle obstacle : map.getObstacles()) {
            if (targetHitbox.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                return true;
            }
        }
        return false;
    }

    public void loadSkills(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            // Read JSON file
            Gson gson = new Gson();

            // Parse JSON file
            String[] skillNames = gson.fromJson(reader, String[].class);
            this.skills = Arrays.stream(skillNames)
                    .map(Skill::new)
                    .collect(Collectors.toList());
        } catch (NullPointerException | IOException | JsonIOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    // getPlayerHitbox
    public Rectangle getPlayerHitbox() {
        return playerHitbox;
    }
}
