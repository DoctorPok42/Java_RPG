package Class.Item;

import java.util.ArrayList;

public class Distributor extends Item {
    private int money;
    private ArrayList<String> snacks = new ArrayList<String>();

    public Distributor(String name, Enum<ItemType> type, float x, float y, int z, int money, ArrayList<String> snacks) {
        super(name, type, x, y, z);
        this.money = money;
        this.snacks = snacks;
    }

    public int getMoney() {
        return money;
    }

    public ArrayList<String> getSnacks() {
        return snacks;
    }

    public boolean buySnack(String snack) {
        if (snacks.contains(snack)) {
            snacks.remove(snack);
            money += 1;
            return true;
        }
        return false;
    }

    public boolean hack() {
        System.out.println("Hacking...");
        return true;
    }
}
