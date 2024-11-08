package Class.Character;

import Class.Skill.Skill;
import Class.Map.Map;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

public class Player extends Character {
    private int money;
    private int timeDays;
    private int timeYears;
    private int timeHours;
    private int fun;
    private int weakness;
    private int hunger;
    private List<Skill> skills;

    //Constructor
    public Player(String name, Image texture) {
        super(name, role.PLAYER, new ImageView(texture));
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

    public void updateTime(Map map) {
        this.timeHours++;
        if (this.timeHours >= 20 || this.timeHours < 6) {
            if (!map.getIsNight())
                map.setIsNight(true);
        } else if (map.getIsNight()) {
            map.setIsNight(false);
        }

        if (this.timeHours == 24){
            this.timeHours = 0;
            this.timeDays++;
        }
        if (this.timeDays == 60){
            this.timeDays = 0;
            this.timeYears++;
        }
    }
}
