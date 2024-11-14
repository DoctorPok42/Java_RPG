package Class.Character;

import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Pnj extends Character {
    private List<String> message;
    private Rectangle pnjHitbox;

    //Constructor
    public Pnj(String name, Enum<Roles> type, Image texture, double x, double y) {
        super(name, type, new ImageView(texture));
        this.message = new ArrayList<>();
        this.setPosX(x);
        this.setPosY(y);
        pnjHitbox = new Rectangle(x,y,32, 46);
        pnjHitbox.setFill(Color.TRANSPARENT);
        pnjHitbox.setStroke(Color.RED);
        pnjHitbox.setStrokeWidth(2);
        System.out.println("Pnj named " + name + " created with texture : " + texture.getUrl());
    }

    //Getter
    public List<String> getMessage(){
        return this.message;
    }
    public Rectangle getPnjHitbox(){
        return this.pnjHitbox;
    }

    //Method
    void addMessage(String message){
        this.message.add(message);
    }
    void Speak(){
        //display dialogue
    }
}
