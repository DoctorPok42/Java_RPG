package Class.Item;

import Class.Character.Player;
import Class.Skill.Skill;

public class Pc extends Item {
    private Enum<PcTypeAction> typeAction = PcTypeAction.WORK;

    public Pc(String name, Enum<ItemType> type, float x, float y, int z, Enum<PcTypeAction> typeAction) {
        super(name, type, x, y, z);
        this.typeAction = typeAction;
    }

    public Enum<PcTypeAction> getTypeAction() {
        return typeAction;
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

    public boolean doAction(Player player, Enum<PcTypeAction> action, int time, String module) {
        return switch (action) {
            case PcTypeAction.WORK -> work(player, time, module);
            case PcTypeAction.PLAY -> play(player, time);
            case PcTypeAction.SEARCH -> search(player, time);
            default -> false;
        };
    }
}
