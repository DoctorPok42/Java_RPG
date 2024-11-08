package Class.Character;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Stack;

public abstract class Character {
    private String name;
    private final ImageView persoView;
    private int posX;
    private int posY;
    private int posZ;
    private role type;

    //Constructor
    public Character(String name, role type, ImageView persoView) {
        this.name = name;
        this.type = type;
        this.persoView = persoView;
    }

    //Getter
    public String getName() {
        return this.name;
    }

    public ImageView getPersoView() {
        return this.persoView;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public int getPosZ() {
        return this.posZ;
    }

    public role getType() {
        return this.type;
    }

    //Method
    public void move(ImageView mapView, StackPane gameView) {}
}
