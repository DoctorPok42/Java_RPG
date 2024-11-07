package Class.Character;

import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.ImageView;

public class Pnj extends Character {
    private List<String> message;

    //Constructor
    public Pnj(String name, role type, Image texture) {
        super(name, type, new ImageView(texture));
        this.message = new ArrayList<>();
    }

    //Getter
    public List<String> getMessage(){
        return this.message;
    }

    //Method
    void addMessage(String message){
        this.message.add(message);
    }
    void Speak(){
        //display dialogue
    }
}
