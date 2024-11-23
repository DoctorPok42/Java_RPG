package Class.bar;

import Class.Character.Player;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Time extends bar {
    private final Text years = new Text();
    private final Text months = new Text();
    private final Text days = new Text();
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
            years.setTranslateX(-110);
            years.setTranslateY(15);
            gameView.getChildren().add(years);
        }

        if (!gameView.getChildren().contains(months)) {
            months.setFill(javafx.scene.paint.Color.WHITE);
            months.setStyle(styles);
            StackPane.setAlignment(months, Pos.TOP_CENTER);
            months.setTranslateX(2.5);
            months.setTranslateY(15);
            gameView.getChildren().add(months);
        }

        if (!gameView.getChildren().contains(days)) {
            days.setFill(javafx.scene.paint.Color.WHITE);
            days.setStyle(styles);
            StackPane.setAlignment(days, Pos.TOP_CENTER);
            days.setTranslateX(115);
            days.setTranslateY(15);
            gameView.getChildren().add(days);
        }
    }

    public void update(Player player) {
        years.setText(player.getTimeYears() > 1 ? "Years: "+player.getTimeYears() : "Year: "+player.getTimeYears());
        months.setText(player.getTimeMonths() > 1 ? "Months: "+player.getTimeMonths() : "Month: "+player.getTimeMonths());
        days.setText(player.getTimeDays() > 1 ? "Days: "+player.getTimeDays() : "Day: "+player.getTimeDays());
    }
}
