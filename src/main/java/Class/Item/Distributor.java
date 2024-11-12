package Class.Item;

import Class.Character.Player;
import java.util.ArrayList;

public class Distributor extends Item {
    private int money;
    private ArrayList<String> snacks = new ArrayList<String>();

    public Distributor(String name, Enum<ItemType> type, float x, float y, int z, int money) {
        super(name, type, x, y, z);

        this.money = money;
//        this.snacks = snacks;
    }

    public int getMoney() {
        return money;
    }

    public ArrayList<String> getSnacks() {
        return snacks;
    }

    private boolean buySnack(Player player, String snack) {
        if (snacks.contains(snack)) {
            snacks.remove(snack);
            money += 1;

            player.eat();
            return true;
        }
        return false;
    }

    private boolean hack(Player player) {
        player.addMoney(money);
        money = 0;
        return true;
    }

    public boolean doAction(Player player, Enum<?> action, int time, String snack) {
        if (!(action instanceof DistributorTypeAction))
            return false;

        return switch ((DistributorTypeAction) action) {
            case BUY -> buySnack(player, snack);
            case HACK -> hack(player);
        };
    }
}
