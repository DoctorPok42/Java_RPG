package Class.Skill;

public class Skill {
    private String name;
    private int level;

    //Constructor
    public Skill(String name){
        this.name = name;
        this.level = 0;
    }

    //Getter
    public String getName(){
        return this.name;
    }

    public int getLevel(){
        return this.level;
    }

    public void setLevel(int level){
        this.level = level;
    }
}
