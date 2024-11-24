package Class.Item;

import Class.Character.Player;
import Class.Character.Pnj;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PnjInteraction extends Item{
    Pnj pnj;
    private final Font font = Font.loadFont("file:assets/font/PressStart2P-Regular.ttf", 20);
    private int id;

    public PnjInteraction (String name, Enum<ItemType> type, float x, float y, int z, int skin, Pnj pnj, int id) {
        super(name, type, x, y, z, skin, id);
        this.pnj = pnj;
        this.id = id;
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

        Dialogues[] dialogues = null;
        List<Dialogues> pnjDialogues = null;
        try (FileReader reader = new FileReader("./data/Dialogues.json")) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Dialogues.class, new DialoguesTypeApadter()).create();
             dialogues = gson.fromJson(reader, Dialogues[].class);
             pnjDialogues = new ArrayList<>();
             for (Dialogues d : dialogues) {
                 if (d.getName().equals(this.pnj.getName())) {
                     pnjDialogues.add(d);
                 }
             }
        } catch (IOException e) {
            e.printStackTrace();
        }

        VBox dialogBox = new VBox();
        int idDialogue = 0;
        dialogBox.setAlignment(Pos.BOTTOM_CENTER);
        dialogBox.setId("dialogBox");
        dialogBox.setSpacing(10);
        Label dialog = new Label(pnjDialogues.get(idDialogue).getText());
        dialog.setStyle("-fx-font-size: 10;" +
                "-fx-font-fill: white ;" +
                "-fx-font-family: 'Press Start 2P';"
        );
        dialogBox.setStyle("-fx-font-family: 'Press Start 2P';" +
                "-fx-background-color: rgba(0, 0, 0, 0.2);"

        );
        dialog.setWrapText(true);
        dialog.setAlignment(Pos.CENTER);

        int nbLines = dialog.getText().split("\n").length;
        dialog.setLineSpacing(5);

        HBox pnjdialog = new HBox();
        Rectangle rect = new Rectangle(520, 101);
        rect.setFill(new ImagePattern(new Image(getClass().getResource("/assets/pnj/dialogbox5.png").toExternalForm())));
        StackPane background = new StackPane(rect);
        pnjdialog.getChildren().add(background);
        pnjdialog.setAlignment(Pos.CENTER);
        pnjdialog.setSpacing(10);
        background.getChildren().add(dialog);

        HBox responses = new HBox();
        responses.setSpacing(20);
        responses.setAlignment(Pos.CENTER);
        Button resp1 = new Button(pnjDialogues.get(pnjDialogues.get(0).getChoices().get(0)).getText());
        Rectangle resp1rect = new Rectangle(250, 48);
        resp1rect.setFill(new ImagePattern(new Image(getClass().getResource("/assets/pnj/dialogbox3.png").toExternalForm())));
        StackPane resp1background = new StackPane(resp1rect);
        resp1.setStyle("-fx-font-family: 'Press Start 2P';-fx-font-fill: white; -fx-font-size: 10;-fx-background-color: transparent;");
        resp1background.getChildren().add(resp1);

        Button resp2 = new Button(pnjDialogues.get(pnjDialogues.get(idDialogue).getChoices().get(1)).getText());
        Rectangle resp2rect = new Rectangle(250, 48);
        resp2rect.setFill(new ImagePattern(new Image(getClass().getResource("/assets/pnj/dialogbox3.png").toExternalForm())));
        StackPane resp2background = new StackPane(resp2rect);
        resp2.setStyle("-fx-font-family: 'Press Start 2P';-fx-font-fill: white; -fx-font-size: 10;-fx-background-color: transparent;");
        resp2background.getChildren().add(resp2);

        responses.getChildren().addAll(resp1background, resp2background);

        updateDialogBox(pnjDialogues.get(idDialogue), dialog, resp1, resp2, pnjDialogues, gameView, rect);
        dialogBox.getChildren().addAll(pnjdialog, responses);
        gameView.getChildren().add(dialogBox);
    }

    private void updateDialogBox(Dialogues currentDialogue, Label pnjDialogue, Button resp1, Button resp2, List<Dialogues> pnjDialogues, StackPane gameView, Rectangle rect) {
        int nbLines = pnjDialogue.getText().split("\n").length;
        pnjDialogue.setLineSpacing(5 - (nbLines - 3));

        resp1.setOnAction(e -> {
            if (pnjDialogues.get(currentDialogue.getChoices().get(0)).getChoices().size() != 0){
                pnjDialogue.setText(pnjDialogues.get(pnjDialogues.get(currentDialogue.getChoices().get(0)).getChoices().get(0)).getText());
                resp1.setText(pnjDialogues.get(pnjDialogues.get(pnjDialogues.get(currentDialogue.getChoices().get(0)).getChoices().get(0)).getChoices().get(0)).getText());
                resp2.setText(pnjDialogues.get(pnjDialogues.get(pnjDialogues.get(currentDialogue.getChoices().get(0)).getChoices().get(0)).getChoices().get(1)).getText());
                updateDialogBox(pnjDialogues.get(pnjDialogues.get(currentDialogue.getChoices().get(0)).getChoices().get(0)), pnjDialogue, resp1, resp2, pnjDialogues, gameView, rect);
            }else{
                gameView.getOnKeyPressed().handle(new KeyEvent(
                        KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE, false, false, false, false
                ));
            }
        });

        resp2.setOnAction(e -> {
            if (pnjDialogues.get(currentDialogue.getChoices().get(1)).getChoices().size() != 0){
                pnjDialogue.setText(pnjDialogues.get(pnjDialogues.get(currentDialogue.getChoices().get(1)).getChoices().get(0)).getText());
                resp1.setText(pnjDialogues.get(pnjDialogues.get(pnjDialogues.get(currentDialogue.getChoices().get(1)).getChoices().get(0)).getChoices().get(0)).getText());
                resp2.setText(pnjDialogues.get(pnjDialogues.get(pnjDialogues.get(currentDialogue.getChoices().get(1)).getChoices().get(0)).getChoices().get(1)).getText());
                updateDialogBox(pnjDialogues.get(pnjDialogues.get(currentDialogue.getChoices().get(1)).getChoices().get(0)), pnjDialogue, resp1, resp2, pnjDialogues, gameView, rect);
            }else{
                gameView.getOnKeyPressed().handle(new KeyEvent(
                        KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE, false, false, false, false
                ));
            }
        });

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
