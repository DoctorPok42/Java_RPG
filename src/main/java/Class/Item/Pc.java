package Class.Item;

import Class.Character.Player;
import Class.Skill.Skill;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import java.util.List;

public class Pc extends Item {
    boolean snaking = false;
    Snake snake;

    public Pc(String name, Enum<ItemType> type, float x, float y, int z, int skin, int id) {
        super(name, type, x, y, z, skin, id);

        this.menu.add(new ImageView("file:assets/interact/pc/work.png"));
        this.menu.add(new ImageView("file:assets/interact/pc/play.png"));
        this.menu.add(new ImageView("file:assets/interact/pc/search.png"));

        List<ImageView> sd_menu = List.of(
                new ImageView("file:assets/skills/web.png"),
                new ImageView("file:assets/skills/java.png"),
                new ImageView("file:assets/skills/devops.png"),
                new ImageView("file:assets/skills/cyber.png"),
                new ImageView("file:assets/skills/ai.png"),
                new ImageView("file:assets/skills/data.png")
        );

        int random = (int) (Math.random() * sd_menu.size());
        this.second_menu.add(List.of(sd_menu.get(random)));

        this.second_menu.add(null);
        this.second_menu.add(null);

        for (ImageView img : this.menu) {
            img.setFitWidth(83.6);
            img.setFitHeight(32);
        }

        for (List<ImageView> secondMenu : this.second_menu) {
            if (secondMenu != null) {
                for (ImageView img : secondMenu) {
                    img.setFitWidth(83.6);
                    img.setFitHeight(32);
                }
            }
        }

        this.actions = List.of(
                "You have just worked for 5 days",
                "You are playing snake",
                "You have just searched for 2 days"
        );
    }

    public void displayMenu() {
        System.out.println("Displaying menu...");
    }

    private boolean work(Player player, int time, String module) {
        if (module == null)
            return false;

        if (player.getSkill(module) != null) {
            player.getSkill(module).setLevel(player.getSkill(module).getLevel() + 1);
        } else {
            player.getSkills().add(new Skill(module));
            player.getSkill(module).setLevel(player.getSkill(module).getLevel() + 1);
        }

        player.addTime(time);
        return true;
    }

    private boolean play(Player player, int time, StackPane gameView) {
        snaking = true;
        snake = new Snake(gameView);
        player.addFun(time);
        return true;
    }

    private boolean search(Player player, int time) {
        player.addTimeOfSearch(time);
        player.addTime(2);
        return true;
    }

    public boolean doAction(Player player, Enum<?> action, int time, String module, StackPane gameView) {
        if (!(action instanceof PcTypeAction))
            return false;
        return switch ((PcTypeAction) action) {
            case WORK -> work(player, time, module);
            case PLAY -> play(player, time, gameView);
            case SEARCH -> search(player, time);
        };
    }
    public boolean isSnaking() {
        return snaking;
    }
    public Snake getSnake() {
        return snake;
    }

    @Override
    public List<PcTypeAction> getActions() {
        return List.of(PcTypeAction.class.getEnumConstants());
    }
}
