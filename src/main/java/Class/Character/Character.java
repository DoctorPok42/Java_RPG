package Class.Character;

import Class.Map.Map;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public abstract class Character {
    private String name;
    protected final ImageView characView;
    private double posX;
    private double posY;
    private int posZ;
    private role type;
    protected javafx.geometry.Point2D characCoord;
    private Rectangle characHitbox;

    //Constructor
    public Character(String name, role type, ImageView characView, Map map) {
        this.name = name;
        this.type = type;
        this.characView = characView;
        this.characView.setFitWidth(50);
        this.characView.setFitHeight(50);
        characCoord = map.getMapContainer().sceneToLocal(this.characView.getLayoutX(), this.characView.getLayoutY());
        this.posX = characCoord.getX();
        this.posY = characCoord.getY();
    }

    //Getter
    public String getName() {
        return this.name;
    }

    public ImageView getCharacView() {
        return this.characView;
    }

    public double getPosX() {
        return this.posX;
    }

    public double getPosY() {
        return this.posY;
    }

    public int getPosZ() {
        return this.posZ;
    }

    public role getType() {
        return this.type;
    }

    //setter
    public void setPosX(double posX) {
        this.posX = posX;
    }
    public void setPosY(double posY) {
        this.posY = posY;
    }

    //Method
    public void move(Map map, StackPane gameView) {}
}
