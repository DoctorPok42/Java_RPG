package Class.Item;

import Class.Character.Player;

public class Canap extends Item {
    public Canap(String name, Enum<ItemType> type, float x, float y, int z, int nbPlaces) {
        super(name, type, x, y, z);
    }

    private boolean sleep(Player player, int time) {
        player.addTime(time);
        player.sleep(time);
        return true;
    }

    @Override
    public boolean doAction(Player player, Enum<?> action, int time, String module) {
        if (!(action instanceof CanapTypeAction))
            return false;
        return switch ((CanapTypeAction) action) {
            case SLEEP -> sleep(player, time);
        };
    }
}
