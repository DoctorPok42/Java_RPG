package Class.Engine;

import Class.Character.Player;
import Class.Item.Item;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public interface Menu {
    void displayInteractiveMenu(Item itemToInteract, Pane mapContainer);
    void displayActionSelected(Item itemToInteract, Pane mapContainer);
    void moveSelected(KeyEvent e, Item itemToInteract);
    void removeSecondMenu(Item itemToInteract, Pane mapContainer);
    void displaySecondMenu(Item itemToInteract, Pane mapContainer);
    void doActionOnEnter(Player player, Item itemToInteract, StackPane gameView, Pane mapContainer);
    void removeAlert(StackPane gameView);
}
