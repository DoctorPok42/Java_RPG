package Class.Character;

import java.util.ArrayList;
import java.util.List;

public class Pnj extends Character{
    List<String> message;

    //Constructor
    Pnj(String name, role type, String texture){
        super(name, type, texture);
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
