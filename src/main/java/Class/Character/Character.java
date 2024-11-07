package Class.Character;

import javafx.scene.image.Image;

public abstract class Character {
    private String name;
    private Image texture;
    private int posX;
    private int posY;
    private int posZ;
    private role type ;

    //Constructor
    protected Character(String name, role type, Image texture) {
        this.name = name;
        this.type = type;
        this.texture = texture;
    }

    //Getter
    public String getName(){
        return this.name;
    }
    public Image getTexture(){
        return this.texture;
    }
    public int getPosX(){
        return this.posX;
    }
    public int getPosY(){
        return this.posY;
    }
    public int getPosZ(){
        return this.posZ;
    }
    public role getType(){
        return this.type;
    }

    //Method
    void move(float x, float y) {
        
    }
}
