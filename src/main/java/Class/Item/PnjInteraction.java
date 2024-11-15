package Class.Item;

import Class.Character.Player;
import Class.Character.Pnj;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.List;

public class PnjInteraction extends Item{
    Pnj pnj;
    public PnjInteraction (String name, Enum<ItemType> type, float x, float y, int z, int skin, Pnj pnj) {
        super(name, type, x, y, z, skin);
        this.pnj = pnj;
        this.menu.add(new ImageView("file:assets/interact/pnj/speak.png"));

        for (ImageView img : this.menu) {
            img.setFitWidth(83.6);
            img.setFitHeight(32);
        }
        this.second_menu.add(null);
    }
    public Pnj getPnj() {
        return this.pnj;
    }
    private void displayDiaglogBox(StackPane gameView) {
        ImageView dlg = new ImageView(new Image("file:assets/Pnjs/dialogbox2.png"));
        StackPane.setAlignment(dlg, Pos.BOTTOM_CENTER);
        gameView.getChildren().add(dlg);
        System.out.println("Pnj: Hello!");
    }
    public boolean speak(Player player , Pnj pnj, StackPane gameView) {
        displayDiaglogBox(gameView);
        return  true;
        }
    public boolean doAction(Player player, Enum<?> action, Pnj pnj, StackPane gameView) {
        if(!(action instanceof PnjTypeAction))
            return false;
        return switch ((PnjTypeAction) action) {
            case SPEAK -> speak(player, pnj, gameView);
        };
    }
    @Override
    public List<PnjTypeAction> getActions() {
        return List.of(PnjTypeAction.class.getEnumConstants());
    }
}
