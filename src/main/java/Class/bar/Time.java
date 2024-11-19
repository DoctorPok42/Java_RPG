package Class.bar;

import Class.Character.Player;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Time extends bar {
    private Text years = new Text();
    private Text hours = new Text();
    private Text days = new Text();
    private final Font font = Font.loadFont("file:assets/font/PressStart2P-Regular.ttf", 13);

    public Time(String name) {
        super(name, new ImageView("file:assets/bar/time.png"));
    }

    @Override
    public void display(StackPane gameView) {
        String styles = "-fx-font-size: 13;" + "-fx-font-family: 'Press Start 2P';";

        if (!gameView.getChildren().contains(texture)) {
            StackPane.setAlignment(texture, Pos.TOP_CENTER);
            gameView.getChildren().add(texture);
        }

        if (!gameView.getChildren().contains(years)) {
            years.setFill(javafx.scene.paint.Color.WHITE);
            years.setStyle(styles);
            StackPane.setAlignment(years, Pos.TOP_CENTER);
            years.setTranslateX(-100);
            years.setTranslateY(15);
            gameView.getChildren().add(years);
        }

        if (!gameView.getChildren().contains(days)) {
            days.setFill(javafx.scene.paint.Color.WHITE);
            days.setStyle(styles);
            StackPane.setAlignment(days, Pos.TOP_CENTER);
            days.setTranslateX(0);
            days.setTranslateY(15);
            gameView.getChildren().add(days);
        }

        if (!gameView.getChildren().contains(hours)) {
            hours.setFill(javafx.scene.paint.Color.WHITE);
            hours.setStyle(styles);
            StackPane.setAlignment(hours, Pos.TOP_CENTER);
            hours.setTranslateX(100);
            hours.setTranslateY(15);
            gameView.getChildren().add(hours);
        }
    }

    public void update(Player player, StackPane gameView) {
        years.setText("Year: "+player.getTimeYears());
        days.setText("Day: "+player.getTimeDays());
        hours.setText("Hour: "+player.getTimeHours());
    }
}
