package Class.Item;

import Class.Character.Player;
import Class.Skill.Skill;

public class Pc extends Item {
    public Pc(String name, Enum<ItemType> type, float x, float y, int z) {
        super(name, type, x, y, z);
    }

    public void displayMenu() {
        System.out.println("Displaying menu...");
    }

    private boolean work(Player player, int time, String module) {
        if (module == null)
            return false;

        if (time > 2) {
            if (player.getSkill(module) != null) {
                player.getSkill(module).setLevel(player.getSkill(module).getLevel() + 1);
            } else {
                player.getSkills().add(new Skill(module));
                player.getSkill(module).setLevel(player.getSkill(module).getLevel() + 1);
            }
        }

        player.addTime(time);
        return true;
    }

    private boolean play(Player player, int time) {
        player.addTime(time);
        return true;
    }

    private boolean search(Player player, int time) {
        player.addTimeOfSearch(time);
        return true;
    }

    @Override
    public boolean doAction(Player player, Enum<?> action, int time, String module) {
        if (!(action instanceof PcTypeAction))
            return false;
        return switch ((PcTypeAction) action) {
            case WORK -> work(player, time, module);
            case PLAY -> play(player, time);
            case SEARCH -> search(player, time);
        };
    }
}
