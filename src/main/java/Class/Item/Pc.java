package Class.Item;

import Class.Character.Player;
import Class.Skill.Skill;
import javafx.scene.image.ImageView;

import java.util.List;

public class Pc extends Item {
    public Pc(String name, Enum<ItemType> type, float x, float y, int z, int skin, int id) {
        super(name, type, x, y, z, skin, id);

        this.menu.add(new ImageView("file:assets/interact/pc/work.png"));
        this.menu.add(new ImageView("file:assets/interact/pc/play.png"));
        this.menu.add(new ImageView("file:assets/interact/pc/search.png"));

        this.second_menu.add(List.of(
                new ImageView("file:assets/skills/web.png"),
                new ImageView("file:assets/skills/java.png"),
                new ImageView("file:assets/skills/devops.png"),
                new ImageView("file:assets/skills/cyber.png"),
                new ImageView("file:assets/skills/ai.png"),
                new ImageView("file:assets/skills/data.png")
        ));
        this.second_menu.add(null);
        this.second_menu.add(null);

        for (int i = 0; i < this.menu.size(); i++) {
            ImageView img = this.menu.get(i);
            img.setFitWidth(83.6);
            img.setFitHeight(32);
        }

        for (int i = 0; i < this.second_menu.size(); i++) {
            if (this.second_menu.get(i) != null) {
                for (int j = 0; j < this.second_menu.get(i).size(); j++) {
                    ImageView img = this.second_menu.get(i).get(j);
                    img.setFitWidth(83.6);
                    img.setFitHeight(32);
                }
            }
        }

        this.actions = List.of(
                "You have just worked for 2 hours",
                "You have just played for 2 hours",
                "You have just searched for 2 hours"
        );
    }

    public void displayMenu() {
        System.out.println("Displaying menu...");
    }

    private boolean work(Player player, int time, String module) {
        if (module == null)
            return false;

        if (time >= 2) {
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
        player.addFun(time);
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

    @Override
    public List<PcTypeAction> getActions() {
        return List.of(PcTypeAction.class.getEnumConstants());
    }
}
