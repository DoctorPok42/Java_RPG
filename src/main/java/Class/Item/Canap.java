package Class.Item;

import Class.Character.Player;

import java.util.List;
import javafx.scene.image.ImageView;

public class Canap extends Item {
    public Canap(String name, Enum<ItemType> type, float x, float y, int z, int skin, int id) {
        super(name, type, x, y, z, skin, id);

        this.menu.add(new ImageView("file:assets/interact/canap/sleep.png"));

        for (ImageView img : this.menu) {
            img.setFitWidth(83.6);
            img.setFitHeight(32);
        }
        this.second_menu.add(null);

        this.actions = List.of(
            "You have just slept for 2 hours"
        );
    }

    private boolean sleep(Player player, int time) {
        player.addTime(time);
        player.sleep(time);
        return true;
    }

    public boolean doAction(Player player, Enum<?> action, int time, String module) {
        if (!(action instanceof CanapTypeAction))
            return false;
        return switch ((CanapTypeAction) action) {
            case SLEEP -> sleep(player, time);
        };
    }

    public List<CanapTypeAction> getActions() {
        return List.of(CanapTypeAction.class.getEnumConstants());
    }
}
