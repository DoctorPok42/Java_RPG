package Class.Menu;

import Class.Character.Player;
import Class.Skill.Skill;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Profile extends Menu {
    private final ImageView background = new ImageView("file:assets/menu/profile/carte.png");
    private final Rectangle backgroundRect = new Rectangle(1280, 720, Color.rgb(0, 0, 0, 0.5));
    private List<Skill> skills;
    private final List<Text> texts = new ArrayList<>();
    private final Text firstName = new Text("");
    private final Text lastName = new Text("");
    private final Rectangle bar = new Rectangle(0, -30, 525, 24);
    private boolean isLoaded = false;
    private String font = "Press Start 2P";

    public Profile() {
        super("Profile", "Profile Menu", 1);
        this.selectedOption = 0;

        StackPane.setAlignment(this.background, Pos.CENTER);

        StackPane.setAlignment(this.bar, Pos.BOTTOM_LEFT);
        this.bar.setTranslateX(377
        );
        this.bar.setTranslateY(-97.5);
        this.bar.setFill(Color.rgb(10, 62, 101));
        this.bar.setWidth(0);
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void loadSkills(Player player) {
        this.skills = player.getSkills();

        for (int i = 0; i < this.skills.size(); i++) {
            Text text = new Text(this.skills.get(i).getName() + ": " + this.skills.get(i).getLevel());
            text.setFont(new Font(font, 20));
            text.setFill(Color.GREY);
            StackPane.setAlignment(text, Pos.BOTTOM_LEFT);
            text.setTranslateX(
                150 + (i % 4) * 250
            );
            text.setTranslateY(
                i <= 3 ? -230 : -190
            );
            this.texts.add(text);
        }

        this.firstName.setText(player.getName().split(" ")[0]);
        this.lastName.setText(player.getName().split(" ")[1]);

        this.lastName.setFont(new Font(font, 20));
        this.lastName.setFill(Color.GREY);
        StackPane.setAlignment(this.lastName, Pos.TOP_LEFT);
        this.lastName.setTranslateX(320);
        this.lastName.setTranslateY(309);

        this.firstName.setFont(new Font(font, 20));
        this.firstName.setFill(Color.GREY);
        StackPane.setAlignment(this.firstName, Pos.TOP_LEFT);
        this.firstName.setTranslateX(320);
        this.firstName.setTranslateY(340);
    }

    public void show(StackPane gameView, Player player) {
        this.isLoaded = true;
        gameView.getChildren().add(backgroundRect);
        gameView.getChildren().add(background);

        skills = player.getSkills();
        int total = 0;

        // Display name of the player
        gameView.getChildren().add(firstName);
        gameView.getChildren().add(lastName);

        for (Text text : texts) {
            // update text with player's skills
            text.setText(skills.get(texts.indexOf(text)).getName() + ": " + skills.get(texts.indexOf(text)).getLevel());
            total += skills.get(texts.indexOf(text)).getLevel();

            gameView.getChildren().add(text);
        }

        // to have a full bar, we need each skill to be at level 8
        bar.setWidth(Math.clamp((total * 65.625) / skills.size(), 0, 525));
        gameView.getChildren().add(bar);
    }

    public void remove(StackPane gameView) {
        this.isLoaded = false;
        gameView.getChildren().remove(backgroundRect);
        gameView.getChildren().remove(background);
        gameView.getChildren().remove(firstName);
        gameView.getChildren().remove(lastName);

        for (int i = 0; i < skills.size(); i++) {
            gameView.getChildren().remove(texts.get(i));
        }

        gameView.getChildren().remove(bar);
    }
}
