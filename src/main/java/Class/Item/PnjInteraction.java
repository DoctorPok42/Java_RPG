package Class.Item;

import Class.Character.Player;
import Class.Character.Pnj;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PnjInteraction extends Item{
    Pnj pnj;
    private final Font font = Font.loadFont("file:assets/font/PressStart2P-Regular.ttf", 20);
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
        StackPane dialogBox = new StackPane(dlg);
        dialogBox.setId("dialogBox");

        Dialogues[] dialogues = null;
        List<Dialogues> pnjDialogues = null;
        try (FileReader reader = new FileReader("./data/Dialogues.json")) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Dialogues.class, new DialoguesTypeApadter()).create();
             dialogues = gson.fromJson(reader, Dialogues[].class);
             pnjDialogues = new ArrayList<>();
             for (Dialogues d : dialogues) {
                 if (d.getName().equals(this.pnj.getName())) {
                     pnjDialogues.add(d);
                     break;
                 }
             }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Text text = new Text();
        Text resp1 = new Text();
        Text resp2 = new Text();
        for (Dialogues d : pnjDialogues) {
            if (d.getId() == 0) {
                text.setText(d.getText());
            }
            List<Dialogues> responses = new ArrayList<>();
            for (int i : d.getChoices()) {
                for (Dialogues d2 : dialogues) {
                    if (d2.getId() == i) {
                        responses.add(d2);
                    }
                }
            }
            resp1.setText(responses.get(0).getText());
            resp2.setText(responses.get(1).getText());
        }
        resp1.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 10;");
        resp1.setFill(javafx.scene.paint.Color.RED);
        StackPane.setAlignment(resp1, Pos.BOTTOM_CENTER);
        resp1.setTranslateX(100);
        resp1.setTranslateY(-100);
        dialogBox.getChildren().add(resp1);
        resp2.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 10;");
        resp2.setFill(javafx.scene.paint.Color.RED);
        StackPane.setAlignment(resp2, Pos.BOTTOM_CENTER);
        resp2.setTranslateX(-100);
        resp2.setTranslateY(-100);
        dialogBox.getChildren().add(resp2);
        text.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 10;");
        text.setFill(javafx.scene.paint.Color.WHITE);
        StackPane.setAlignment(text, Pos.BOTTOM_CENTER);
        text.setTranslateY(-50);
        dialogBox.getChildren().add(text);
        gameView.getChildren().add(dialogBox);
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
