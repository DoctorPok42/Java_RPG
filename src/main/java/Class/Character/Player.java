package Class.Character;

import Class.Item.Item;
import Class.Map.Map;
import Class.Music.Music;
import Class.Skill.Skill;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private boolean jobbing;
    private int timeDays;
    private int timeYears;
    private int timeMonths;
    private int secondsCounter = 0;
    private int nbHoursOfSearch;
    private Item storeItem;


    private double fun;
    private double weakness;
    private double hunger;

    private List<Skill> skills;

    private final Timeline timelineUP;
    private final Timeline timelineDOWN;
    private final Timeline timelineLEFT;
    private final Timeline timelineRIGHT;
    private final Timeline staticAnimationTimeline;
    private final Timeline walkDownAnimationTimeline;
    private final Timeline walkUpAnimationTimeline;
    private final Timeline walkLeftAnimationTimeline;
    private final Timeline walkRightAnimationTimeline;
    private final Music walkSound = new Music("assets/music/walk.wav",0.2);

    protected final ImageView playerView;
    protected final Image[] StaticAnim;
    private int currentStaticIndex = 0;
    protected final Image[] walkDownAnim;
    private int currentWalkDownIndex = 0;
    private final Image[] walkUpAnim;
    private int currentWalkUpIndex = 0;
    private final Image[] walkLeftAnim;
    private int currentWalkLeftIndex = 0;
    private final Image[] walkRightAnim;
    private int currentWalkRightIndex = 0;

    private javafx.geometry.Point2D playerCoord;
    private final Rectangle playerHitbox;

    // Dev engine
    private boolean hasToCheckCollision = true;

    //Constructor
    public Player(String name, Image texture, Map map) {
        super(name, Roles.PLAYER, new ImageView(texture), map);
        this.playerView = super.characView;
        this.playerView.setLayoutX(map.getViewWidth() / 2 - this.playerView.getFitWidth() / 2);
        this.playerView.setLayoutY(map.getViewHeight() / 2 - this.playerView.getFitHeight() / 2);
        this.playerCoord = map.getMapContainer().sceneToLocal(this.playerView.getLayoutX(), this.playerView.getLayoutY());
        this.setPosX(this.playerCoord.getX());
        this.setPosY(this.playerCoord.getY());

        this.money = -5000;
        this.jobbing = false;
        this.timeDays = 0;
        this.timeYears = 0;
        this.timeMonths = 0;
        this.nbHoursOfSearch = 0;
        this.fun = 0;
        this.weakness = 115;
        this.hunger = 115;

        this.StaticAnim = new Image[]{
                new Image("file:assets/perso/anim/AnimStatic0.png"),
                new Image("file:assets/perso/anim/AnimStatic1.png"),
                new Image("file:assets/perso/anim/AnimStatic2.png"),
                new Image("file:assets/perso/anim/AnimStatic3.png"),
                new Image("file:assets/perso/anim/AnimStatic4.png"),
                new Image("file:assets/perso/anim/AnimStatic5.png"),
        };
        staticAnimationTimeline = new Timeline(new KeyFrame(Duration.millis(200),e->animateStatic()));
        staticAnimationTimeline.setCycleCount(Animation.INDEFINITE);
        startStaticAnimation();

        this.walkDownAnim = new Image[]{
                new Image("file:assets/perso/anim/AnimWalkDown0.png"),
                new Image("file:assets/perso/anim/AnimWalkDown1.png"),
                new Image("file:assets/perso/anim/AnimWalkDown2.png"),
                new Image("file:assets/perso/anim/AnimWalkDown3.png"),
                new Image("file:assets/perso/anim/AnimWalkDown4.png"),
                new Image("file:assets/perso/anim/AnimWalkDown5.png"),
        };
        walkDownAnimationTimeline = new Timeline(new KeyFrame(Duration.millis(100),e->animateWalkDown()));
        walkDownAnimationTimeline.setCycleCount(Animation.INDEFINITE);

        this.walkUpAnim = new Image[]{
                new Image("file:assets/perso/anim/AnimWalkUp0.png"),
                new Image("file:assets/perso/anim/AnimWalkUp1.png"),
                new Image("file:assets/perso/anim/AnimWalkUp2.png"),
                new Image("file:assets/perso/anim/AnimWalkUp3.png"),
                new Image("file:assets/perso/anim/AnimWalkUp4.png"),
                new Image("file:assets/perso/anim/AnimWalkUp5.png"),
        };
        walkUpAnimationTimeline = new Timeline(new KeyFrame(Duration.millis(100),e->animateWalkUp()));
        walkUpAnimationTimeline.setCycleCount(Animation.INDEFINITE);

        this.walkLeftAnim = new Image[]{
                new Image("file:assets/perso/anim/AnimWalkLeft0.png"),
                new Image("file:assets/perso/anim/AnimWalkLeft1.png"),
                new Image("file:assets/perso/anim/AnimWalkLeft2.png"),
                new Image("file:assets/perso/anim/AnimWalkLeft3.png"),
                new Image("file:assets/perso/anim/AnimWalkLeft4.png"),
                new Image("file:assets/perso/anim/AnimWalkLeft5.png"),
        };
        walkLeftAnimationTimeline = new Timeline(new KeyFrame(Duration.millis(100),e->animateWalkLeft()));
        walkLeftAnimationTimeline.setCycleCount(Animation.INDEFINITE);

        this.walkRightAnim = new Image[]{
                new Image("file:assets/perso/anim/AnimWalkRight0.png"),
                new Image("file:assets/perso/anim/AnimWalkRight1.png"),
                new Image("file:assets/perso/anim/AnimWalkRight2.png"),
                new Image("file:assets/perso/anim/AnimWalkRight3.png"),
                new Image("file:assets/perso/anim/AnimWalkRight4.png"),
                new Image("file:assets/perso/anim/AnimWalkRight5.png"),
        };
        walkRightAnimationTimeline = new Timeline(new KeyFrame(Duration.millis(100),e->animateWalkRight()));
        walkRightAnimationTimeline.setCycleCount(Animation.INDEFINITE);



        map.getMapContainer().setTranslateX(map.getViewWidth() / 2 - map.getMapWidth() / 2);
        map.getMapContainer().setTranslateY(map.getViewHeight() / 2 - map.getMapHeight() / 2);
        map.setMapTranslateX(map.getViewWidth() / 2 - map.getMapWidth() / 2);
        map.setMapTranslateY(map.getViewHeight() / 2 - map.getMapHeight() / 2);
        this.playerHitbox = new Rectangle(playerCoord.getX(),playerCoord.getY(), this.playerView.getFitWidth(), this.playerView.getFitHeight());
        this.playerHitbox.setFill(Color.TRANSPARENT);
        this.playerHitbox.setStroke(Color.TRANSPARENT);
        this.playerHitbox.setStrokeWidth(2);
        map.addMapContainer(this.playerHitbox);

        double movestep = 6;
        timelineUP = createMovementTimeline(0, movestep, map);
        timelineDOWN = createMovementTimeline(0, -movestep, map);
        timelineLEFT = createMovementTimeline(movestep, 0, map);
        timelineRIGHT = createMovementTimeline(-movestep, 0, map);

        try {
            pickName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pickName(){
        try (FileReader reader = new FileReader("./data/playerName.json")) {
            // Read JSON file
            Gson gson = new Gson();
            // Parse JSON file
            String[] names = gson.fromJson(reader, String[].class);
            this.setName(names[new java.util.Random().nextInt(names.length)]);
        } catch (NullPointerException | IOException | JsonIOException | JsonSyntaxException e) {
            e.printStackTrace();
            this.setName("John Doe");
        }
    }

    //Getter
    public int getMoney(){
        return this.money;
    }
    public void setMoney(int money){
        this.money = money;
    }
    public int getTimeDays(){
        return this.timeDays;
    }
    public int getTimeYears(){
        return this.timeYears;
    }
    public int getTimeMonths() {
        return this.timeMonths;
    }
    public int getNbHoursOfSearch(){
        return this.nbHoursOfSearch;
    }
    public double getFun(){
        return this.fun;
    }

    public double getWeakness() {
        return this.weakness;
    }

    public boolean isJobbing() {
        return this.jobbing;
    }
    public void setJobbing(boolean jobbing) {
        this.jobbing = jobbing;
    }

    public double getHunger() {
        return this.hunger;
    }

    public List<Skill> getSkills(){
        return this.skills;
    }
    public Item getStoreItem() {
        return this.storeItem;
    }

    public void setStoreItem(Item item) {
        this.storeItem = item;
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
        this.hunger += 7;
    }

    public void sleep(int hours) {
        this.weakness += (hours * 60);
    }

    public void updateStats() {
        double weaknessPerSecond = 8.0 / 60.0;
        double hungerPerSecond = 5.0 / 60.0;

        this.weakness -= weaknessPerSecond;
        this.hunger -= hungerPerSecond;

        this.weakness = Math.clamp(this.weakness, 0, 115);
        this.hunger = Math.clamp(this.hunger, 0, 115);
    }

    private void timeLogic() {
        if (timeDays >= 20) {
            timeDays = 0;
            timeMonths++;
        }
        if (timeMonths >= 12) {
            timeMonths = 0;
            timeYears++;
        }
    }

    public void updateTime() {
        this.secondsCounter++;
        this.updateStats();

        if (this.secondsCounter >= 60) {
            this.timeDays++;
            this.secondsCounter = 0;
            timeLogic();
        }
    }

    public void addTime(int hours) {
        for (int i = 0; i < (hours * 60); i++) {
            updateTime();
        }
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

    // Dev engine
    public void setIsCollision(boolean isCollision) {
        this.hasToCheckCollision = isCollision;
    }

    private Timeline checkKey(KeyCode code) {
        if (code == KeyCode.UP || code == KeyCode.Z) {
            startWalkUpAnimation();
            walkSound.play();
            return timelineUP;
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            startWalkDownAnimation();
            walkSound.play();
            return timelineDOWN;
        } else if (code == KeyCode.LEFT || code == KeyCode.Q) {
            startWalkLeftAnimation();
            walkSound.play();
            return timelineLEFT;
        } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            startWalkRightAnimation();
            walkSound.play();
            return timelineRIGHT;
        }
        return null;
    }

    //Move method
    @Override
    public void move(ImageView mapView, StackPane gameView, boolean keyUp, KeyEvent e) {
        if (keyUp) {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.Z) {
                timelineDOWN.stop();
                stopWalkDownAnimation();
                timelineLEFT.stop();
                stopWalkLeftAnimation();
                timelineRIGHT.stop();
                stopWalkRightAnimation();
            } else if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) {
                timelineUP.stop();
                stopWalkUpAnimation();
                timelineLEFT.stop();
                stopWalkLeftAnimation();
                timelineRIGHT.stop();
                stopWalkRightAnimation();
            } else if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.Q) {
                timelineDOWN.stop();
                stopWalkDownAnimation();
                timelineUP.stop();
                stopWalkUpAnimation();
                timelineRIGHT.stop();
                stopWalkRightAnimation();
            } else if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) {
                timelineDOWN.stop();
                stopWalkDownAnimation();
                timelineLEFT.stop();
                stopWalkLeftAnimation();
                timelineUP.stop();
                stopWalkUpAnimation();

            }
            stopStaticAnimation();
            Timeline timeline = checkKey(e.getCode());
            timeline.play();
        } else {
            Timeline timeline = checkKey(e.getCode());
            timeline.stop();
            walkSound.stop();
            stopWalkDownAnimation();
            stopWalkLeftAnimation();
            stopWalkRightAnimation();
            stopWalkUpAnimation();
            startStaticAnimation();
        }
    }

    public void stopWalkSound() {
        walkSound.stop();
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

                    this.storeItem = detectInteraction(targetHitbox, map);

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

    //Animate character
    private void animateStatic() {
        currentStaticIndex = (currentStaticIndex + 1) % StaticAnim.length;
        playerView.setImage(StaticAnim[currentStaticIndex]);
    }
    private void startStaticAnimation() {
        if(staticAnimationTimeline.getStatus() == Animation.Status.STOPPED)
            staticAnimationTimeline.play();
    }
    private void stopStaticAnimation() {
        if(staticAnimationTimeline.getStatus() == Animation.Status.RUNNING)
            staticAnimationTimeline.stop();
    }

    private void animateWalkDown() {
        currentWalkDownIndex = (currentWalkDownIndex + 1) % walkDownAnim.length;
        playerView.setImage(walkDownAnim[currentWalkDownIndex]);
    }
    private void startWalkDownAnimation() {
        if(walkDownAnimationTimeline.getStatus() == Animation.Status.STOPPED)
            walkDownAnimationTimeline.play();
    }
    private void stopWalkDownAnimation() {
        if(walkDownAnimationTimeline.getStatus() == Animation.Status.RUNNING)
            walkDownAnimationTimeline.stop();
    }

    private void animateWalkUp() {
        currentWalkUpIndex = (currentWalkUpIndex + 1) % walkUpAnim.length;
        playerView.setImage(walkUpAnim[currentWalkUpIndex]);
    }
    private void startWalkUpAnimation() {
        if(walkUpAnimationTimeline.getStatus() == Animation.Status.STOPPED)
            walkUpAnimationTimeline.play();
    }
    private void stopWalkUpAnimation() {
        if(walkUpAnimationTimeline.getStatus() == Animation.Status.RUNNING)
            walkUpAnimationTimeline.stop();
    }

    private void animateWalkLeft() {
        currentWalkLeftIndex = (currentWalkLeftIndex + 1) % walkLeftAnim.length;
        playerView.setImage(walkLeftAnim[currentWalkLeftIndex]);
    }
    private void startWalkLeftAnimation() {
        if(walkLeftAnimationTimeline.getStatus() == Animation.Status.STOPPED)
            walkLeftAnimationTimeline.play();
    }
    private void stopWalkLeftAnimation() {
        if(walkLeftAnimationTimeline.getStatus() == Animation.Status.RUNNING)
            walkLeftAnimationTimeline.stop();
    }

    private void animateWalkRight() {
        currentWalkRightIndex = (currentWalkRightIndex + 1) % walkRightAnim.length;
        playerView.setImage(walkRightAnim[currentWalkRightIndex]);
    }
    private void startWalkRightAnimation() {
        if(walkRightAnimationTimeline.getStatus() == Animation.Status.STOPPED)
            walkRightAnimationTimeline.play();
    }
    private void stopWalkRightAnimation() {
        if(walkRightAnimationTimeline.getStatus() == Animation.Status.RUNNING)
            walkRightAnimationTimeline.stop();
    }


    private boolean isCollision(Rectangle targetHitbox, Map map) {
        if (!hasToCheckCollision) return false;

        for (Rectangle obstacle : map.getObstacles()) {
            if (targetHitbox.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                return true;
            }
        }

        for (int i = 0; i < map.getItems().size(); i++) {
            Item item = map.getItems().get(i);
            if (targetHitbox.getBoundsInParent().intersects(item.getHitbox().getBoundsInParent())) {
                return true;
            }
        }
        return false;
    }

    private Item detectInteraction(Rectangle targetHitbox, Map map) {
        for (int i = 0; i < map.getItems().size(); i++) {
            Item item = map.getItems().get(i);
            if (targetHitbox.getBoundsInParent().intersects(item.getInteractionHitbox().getBoundsInParent())) {
                this.storeItem = item;
                return item;
            }
        }
        return null;
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
