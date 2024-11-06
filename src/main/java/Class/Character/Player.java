package Class.Character;

import Class.Skill.Skill;
import java.util.List;

public class Player extends Character{
    int money;
    int timeDays;
    int timeYears;
    int timeHours;
    int fun;
    int weakness;
    int hunger;
    List<Skill> skills;

    //Constructor
    Player(String name,String texture){
        super(name, role.PLAYER, texture);
        this.money = 0;
        this.timeDays = 0;
        this.timeYears = 0;
        this.timeHours = 0;
        this.fun = 0;
        this.weakness = 0;
        this.hunger = 0;
    }

    //Getter
    public int getMoney(){
        return this.money;
    }
    public int getTimeDays(){
        return this.timeDays;
    }
    public int getTimeYears(){
        return this.timeYears;
    }
    public int getTimeHours(){
        return this.timeHours;
    }
    public int getFun(){
        return this.fun;
    }
    public int getWeakness(){
        return this.weakness;
    }
    public int getHunger(){
        return this.hunger;
    }

    //Method
    void interact(){
        //interact with...
    }
    void profile(){
        //display profile
    }
    void menu(){
        //display menu
    }
}
