package Class.Character;

public abstract class Character {
    private String name;
    private String texture;
    private int posX;
    private int posY;
    private int posZ;
    private role type ;

    //Constructor
    protected Character(String name, role type, String texture){
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
