package Class.Character;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

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
