package Class.Menu;

import Class.Character.Player;
import Class.Music.Music;
import Class.Skill.Skill;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class End extends Menu {
    private final ImageView background = new ImageView("file:assets/menu/end/menu.png");
    private boolean isLoaded = false;
    private final Rectangle backgroundRect = new Rectangle(1280, 720, Color.rgb(0, 0, 0, 0.5));
    private final Text playerNameText = new Text();
    private final Text endText = new Text();
    private final static String font = "Press Start 2P";
    private List<String> endTexts = new ArrayList<String>();
    private int grade = 0;

    public End() {
        super("End", "End Menu", 2);
        this.selectedOption = 0;

        StackPane.setAlignment(this.background, Pos.CENTER);

        this.endTexts = List.of(
                "Easy Win!",
                "Try to have fun next time!",
                "Try to sleep next time!",
                "Try to eat next time!",
                "Sleep and eat next time!",
                "You lost! But in swag!",
                "What a loser!"
        );
    }

    public void checkEndGame(StackPane gameView, Player player, Music music) {
        if (player.getTimeYears() >= 1 || (player.getHunger() <= 0 || player.getWeakness() <= 0)) {
            music.stop();
            player.stopWalkSound();
            show(gameView, player);
        }
    }

    public void setGrade(Player player) {
        List<Skill> skills = player.getSkills();
        int selectedGrade = 0;
        boolean isHungry = player.getHunger() > 0;
        boolean isWeak = player.getWeakness() > 0;
        double isFun = player.getFun();

        for (Skill skill : skills) {
            if (skill.getLevel() >= 8) {
                selectedGrade++;
            }
        }

        if (selectedGrade == skills.size() && isHungry && isWeak && isFun > 0) {
            this.grade = 0; // Excellent
        } else if (selectedGrade == skills.size() && isHungry && isWeak) {
            this.grade = 1; // Bad
        } else if (selectedGrade == skills.size() && isHungry) {
            this.grade = 2; // Average
        } else if (selectedGrade == skills.size() && isWeak) {
            this.grade = 3; // Good
        } else if (selectedGrade == skills.size()) {
            this.grade = 4; // Average
        } else if (selectedGrade < skills.size() && isFun > 0 && isHungry && isWeak) {
            this.grade = 5; // Average
        } else if (selectedGrade < skills.size() && isHungry) {
            this.grade = 2; // Bad
        } else if (selectedGrade < skills.size() && isWeak) {
            this.grade = 3; // Bad
        } else {
            this.grade = 6; // Bad
        }
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setName(String name) {
        this.playerNameText.setText(name);
        this.playerNameText.setFont(new Font(font, 30));
        this.playerNameText.setFill(Color.BLACK);
        StackPane.setAlignment(this.playerNameText, Pos.CENTER);
        this.playerNameText.setTranslateY(100);
    }

    public void show(StackPane gameView, Player player) {
        this.isLoaded = true;
        gameView.getChildren().add(backgroundRect);
        gameView.getChildren().add(background);
        gameView.getChildren().add(playerNameText);

        setGrade(player);

        endText.setText(endTexts.get(grade));
        endText.setFont(new Font(font, 35));
        endText.setFill(Color.rgb(10, 62, 101));
        StackPane.setAlignment(endText, Pos.CENTER);
        endText.setTranslateY(190);
        gameView.getChildren().add(endText);
    }

    public void remove(StackPane gameView) {
        this.isLoaded = false;
        gameView.getChildren().remove(backgroundRect);
        gameView.getChildren().remove(background);
        gameView.getChildren().remove(playerNameText);
        gameView.getChildren().remove(endText);
    }
}
