package Class.Character;

public abstract class Character {
    String name;
    String texture;
    int posX;
    int posY;
    int posZ;
    role type ;

    //Constructor
    Character(String name, role type, String texture){
        this.name = name;
        this.type = type;
        this.texture = texture;
    }

    //Getter
    public String getName(){
        return this.name;
    }
    public String getTexture(){
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
    void move(){
        //move character
    }
}
