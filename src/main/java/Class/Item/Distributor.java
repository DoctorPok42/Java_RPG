package Class.Item;

import Class.Character.Player;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Distributor extends Item {
    private int money;

    public Distributor(String name, Enum<ItemType> type, float x, float y, int z, int money, int skin) {
        super(name, type, x, y, z, skin);

        this.money = money;

        this.menu.add(new ImageView("file:assets/interact/distributor/buy.png"));

        this.second_menu.add(null);

        for (int i = 0; i < this.menu.size(); i++) {
            ImageView img = this.menu.get(i);
            img.setFitWidth(83.6);
            img.setFitHeight(32);
        }
    }

    public int getMoney() {
        return money;
    }

    private boolean buySnack(Player player) {
        money += 1;
        player.eat();
        return true;
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
            case BUY -> buySnack(player);
            case HACK -> hack(player);
        };
    }

    public List<DistributorTypeAction> getActions() {
        return List.of(DistributorTypeAction.class.getEnumConstants());
    }
}
