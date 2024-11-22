package Class.Character;

import Class.Item.Item;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Pnj extends Character {
    private List<String> message;
    private Rectangle pnjHitbox;
    private int id;
    private Image[] pnjAnim;
    private int currentIndexAnim=0;
    private Timeline staticAnimationTimeline;
    private int skin;

    //Constructor
    public Pnj(String name, Enum<Roles> type, int skin, double x, double y, int id){
        super(name, type);

        this.message = new ArrayList<>();
        this.setPosX(x);
        this.setPosY(y);
        this.id = id;
        pnjHitbox = new Rectangle(x,y,32, 46);
        pnjHitbox.setFill(Color.TRANSPARENT);
        pnjHitbox.setStroke(Color.RED);
        pnjHitbox.setStrokeWidth(2);
        this.skin = skin;

        switch (skin){
            case 1:
                super.characView = new ImageView(new Image("file:assets/Pnjs/skin1/AnimSkin1-0.png"));
                pnjAnim = new Image[]{
                        new Image("file:assets/Pnjs/skin1/AnimSkin1-0.png"),
                        new Image("file:assets/Pnjs/skin1/AnimSkin1-1.png"),
                        new Image("file:assets/Pnjs/skin1/AnimSkin1-2.png"),
                        new Image("file:assets/Pnjs/skin1/AnimSkin1-3.png"),
                        new Image("file:assets/Pnjs/skin1/AnimSkin1-4.png"),
                        new Image("file:assets/Pnjs/skin1/AnimSkin1-5.png")
                };
                break;
            case 2:
                super.characView = new ImageView(new Image("file:assets/Pnjs/skin2/AnimSkin2-0.png"));
                pnjAnim = new Image[]{
                        new Image("file:assets/Pnjs/skin2/AnimSkin2-0.png"),
                        new Image("file:assets/Pnjs/skin2/AnimSkin2-1.png"),
                        new Image("file:assets/Pnjs/skin2/AnimSkin2-2.png"),
                        new Image("file:assets/Pnjs/skin2/AnimSkin2-3.png"),
                        new Image("file:assets/Pnjs/skin2/AnimSkin2-4.png"),
                        new Image("file:assets/Pnjs/skin2/AnimSkin2-5.png")
                };
                break;
            case 3:
                super.characView = new ImageView(new Image("file:assets/Pnjs/skin3/AnimSkin3-0.png"));
                pnjAnim = new Image[]{
                        new Image("file:assets/Pnjs/skin3/AnimSkin3-0.png"),
                        new Image("file:assets/Pnjs/skin3/AnimSkin3-1.png"),
                        new Image("file:assets/Pnjs/skin3/AnimSkin3-2.png"),
                        new Image("file:assets/Pnjs/skin3/AnimSkin3-3.png"),
                        new Image("file:assets/Pnjs/skin3/AnimSkin3-4.png"),
                        new Image("file:assets/Pnjs/skin3/AnimSkin3-5.png")
                };
                break;

        }
        staticAnimationTimeline = new Timeline(new KeyFrame(Duration.millis(100), e->animateStatic()));
        staticAnimationTimeline.setCycleCount(Animation.INDEFINITE);
        startStaticAnimation();
    }

    //Getter
    public List<String> getMessage(){
        return this.message;
    }
    public Rectangle getPnjHitbox(){
        return this.pnjHitbox;
    }

    //Method
    private void animateStatic() {
        currentIndexAnim = (currentIndexAnim + 1) % pnjAnim.length;
        super.characView.setImage(pnjAnim[currentIndexAnim]);
    }
    private void startStaticAnimation() {
        if(staticAnimationTimeline.getStatus() == Animation.Status.STOPPED)
            staticAnimationTimeline.play();
    }
    private void stopStaticAnimation() {
        if(staticAnimationTimeline.getStatus() == Animation.Status.RUNNING)
            staticAnimationTimeline.stop();
    }

    public int getId() {
        return this.id;
    }
}
