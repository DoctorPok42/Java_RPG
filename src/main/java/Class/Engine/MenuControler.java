package Class.Engine;

import Class.Character.Player;
import Class.Item.*;
import Class.Skill.Skill;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Objects;

public class MenuControler extends Controler implements Menu {
    private final ImageView action = new ImageView("file:assets/interact/alert.png");
    private final Text actionText = new Text();

    public MenuControler(String name) {
        super(name);

        StackPane.setAlignment(action, Pos.TOP_RIGHT);
        action.setTranslateX(-10);
        action.setTranslateY(10);

        Font font = Font.loadFont("file:assets/font/PressStart2P-Regular.ttf", 8);
        actionText.setFont(font);
        actionText.setFill(Color.WHITE);
        StackPane.setAlignment(actionText, Pos.TOP_RIGHT);
        actionText.setTranslateX(-32);
        actionText.setTranslateY(25);
    }

    @Override
    public void displayInteractiveMenu(Item itemToInteract, Pane mapContainer) {
        if (itemToInteract != null && itemToInteract.getType() == ItemType.PC || Objects.requireNonNull(itemToInteract).getType() == ItemType.DISTRIBUTOR || itemToInteract.getType() == ItemType.CANAP|| itemToInteract.getType() == ItemType.PNJ) {
            List<ImageView> images = itemToInteract.getMenu();

            for (int i = 0; i < images.size(); i++) {
                images.get(i).setLayoutX(itemToInteract.getX() + 37);
                images.get(i).setLayoutY(itemToInteract.getY() + (i * 35) - 1);
                if (!mapContainer.getChildren().contains(images.get(i))) {
                    mapContainer.getChildren().add(images.get(i));
                }
            }
        }
    }

    @Override
    public void displayActionSelected(Item itemToInteract, Pane mapContainer) {
        if (itemToInteract != null && itemToInteract.getType() == ItemType.PC || Objects.requireNonNull(itemToInteract).getType() == ItemType.DISTRIBUTOR || itemToInteract.getType() == ItemType.CANAP|| itemToInteract.getType() == ItemType.PNJ) {
            ImageView img = itemToInteract.getSelectedMenu();

            if (currentAction < 10) {
                img.setLayoutX(itemToInteract.getX() + 33.5);
                img.setLayoutY(itemToInteract.getY() + (currentAction * 35) - 4.5);
            } else {
                img.setLayoutX(itemToInteract.getX() + 121);
                img.setLayoutY(itemToInteract.getY() + (currentAction - 10) * 36 - 4.5);
            }

            if (!mapContainer.getChildren().contains(img)) {
                mapContainer.getChildren().add(img);
            } else {
                mapContainer.getChildren().remove(img);
                mapContainer.getChildren().add(img);
            }
        }
    }

    private void moveSelectedRL(KeyEvent e, Item itemToInteract) {
        if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) {
            if (currentAction < 10 && itemToInteract.getSecondMenu().get(currentAction) != null) {
                currentAction += 10;
            }
        } else if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.Q) {
            if (currentAction > 10) {
                currentAction = (currentAction / 10) - 1;
            }  else if (currentAction >= 10) {
                currentAction -= 10;
            }
        }
    }

    private void moveSelectedUpDown(KeyEvent e, List<ImageView> secondMenu) {
        if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.Z) {
            if (currentAction > 10) {
                currentAction -= 1;
            }
        } else if ((e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) && (currentAction % 10) < secondMenu.size() - 1) {
            currentAction += 1;
        }
    }

    @Override
    public void moveSelected(KeyEvent e, Item itemToInteract) {
        List<ImageView> images = itemToInteract.getMenu();

        if (currentAction != -1 && currentAction < 10) {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.Z) {
                if (currentAction > 0) {
                    currentAction -= 1;
                }
            } else if ((e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) && currentAction < images.size() - 1) {
                currentAction += 1;
            }
        } else if (currentAction != -1) {
            List<ImageView> secondMenu = itemToInteract.getSecondMenu().get((currentAction / 10) - 1);

            moveSelectedUpDown(e, secondMenu);
        }

        moveSelectedRL(e, itemToInteract);
    }

    @Override
    public void removeSecondMenu(Item itemToInteract, Pane mapContainer) {
        List<List<ImageView>> images = itemToInteract.getSecondMenu();

        for (List<ImageView> image : images) {
            if (image != null) {
                for (ImageView imageView : image) {
                    mapContainer.getChildren().remove(imageView);
                }
            }
        }
    }

    @Override
    public void displaySecondMenu(Item itemToInteract, Pane mapContainer) {
        if (currentAction < 10) {
            removeSecondMenu(itemToInteract, mapContainer);
            return;
        }

        List<List<ImageView>> images = itemToInteract.getSecondMenu();
        List<ImageView> secondMenu = images.get((currentAction / 10) - 1);

        if (secondMenu == null) return;

        for (int i = 0; i < secondMenu.size(); i++) {
            secondMenu.get(i).setLayoutX(itemToInteract.getX() + 125);
            secondMenu.get(i).setLayoutY(itemToInteract.getY() + (i * 36) - 1);
            if (!mapContainer.getChildren().contains(secondMenu.get(i))) {
                mapContainer.getChildren().add(secondMenu.get(i));
            }
        }
    }

    @Override
    public void doActionOnEnter(Player player, Item itemToInteract, StackPane gameView, Pane mapContainer) {
        List<Skill> skills = player.getSkills();


        if (itemToInteract != null) {
            int firstMenu;
            if (currentAction < 10) {
                firstMenu = currentAction;
            } else {
                firstMenu = (currentAction / 10) -1;
            }

            if (itemToInteract.getType() == ItemType.PC) {
                Pc pc = (Pc) itemToInteract;
                pc.doAction(player, pc.getActions().get(firstMenu), 2, skills.get(currentAction % 10).getName(), gameView);
            } else if (itemToInteract.getType() == ItemType.DISTRIBUTOR) {
                Distributor distributor = (Distributor) itemToInteract;
                distributor.doAction(player, distributor.getActions().get(firstMenu), 1, "module");
            } else if (itemToInteract.getType() == ItemType.CANAP) {
                Canap canap = (Canap) itemToInteract;
                canap.doAction(player, canap.getActions().get(firstMenu), 1, "module");
            } else if ( itemToInteract.getType() == ItemType.PNJ) {
                PnjInteraction pnj = (PnjInteraction) itemToInteract;
                pnj.doAction(player, pnj.getActions().get(firstMenu), pnj.getPnj(), gameView);
            }

            String text = itemToInteract.getListActions().get(firstMenu);
            if (!gameView.getChildren().contains(action)) {
                actionText.setText(text);
                action.setFitWidth(text.length() * 9.5);

                gameView.getChildren().add(action);
                gameView.getChildren().add(actionText);
            } else {
                actionText.setText(text);
            }
        }
    }

    @Override
    public void removeAlert(StackPane gameView) {
        gameView.getChildren().remove(action);
        gameView.getChildren().remove(actionText);
    }
}
