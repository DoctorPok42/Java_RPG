package Class.Item;

import Class.Character.Player;
import Class.Skill.Skill;
import javafx.scene.image.ImageView;

public class Pc extends Item {
    public Pc(String name, Enum<ItemType> type, float x, float y, int z) {
        super(name, type, x, y, z);

        this.menu.add(new ImageView("file:assets/interact/pc/work.png"));
        this.menu.add(new ImageView("file:assets/interact/test4.png"));
        this.menu.add(new ImageView("file:assets/interact/pc/play.png"));
        this.menu.add(new ImageView("file:assets/interact/test4.png"));
        this.menu.add(new ImageView("file:assets/interact/pc/search.png"));
        this.menu.add(new ImageView("file:assets/interact/test4.png"));

        for (int i = 0; i < this.menu.size(); i++) {
            ImageView img = (ImageView) this.menu.get(i);
            if (i % 2 == 0) {
                img.setFitWidth(83.6);
                img.setFitHeight(32);
            } else {
                img.setFitWidth(91.96);
                img.setFitHeight(37.5);
            }
        }
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
