package Class.bar;

import Class.Character.Player;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.Random;

public class Money extends bar {
    private final Text money = new Text();
    private final Text job = new Text();
    public Money(String name){
        super(name, new ImageView("file:assets/bar/money.png"));
    }

    @Override
    public void display(StackPane gameView) {
        String styles = "-fx-font-size: 10;" + "-fx-font-family: 'Press Start 2P';"+ "-fx-fill: white;";

        if (!gameView.getChildren().contains(texture)) {
            StackPane.setAlignment(texture, Pos.TOP_LEFT);
            texture.setTranslateX(5);
            gameView.getChildren().add(texture);
        }
        if (!gameView.getChildren().contains(money)) {
            money.setStyle(styles);
            StackPane.setAlignment(money, Pos.TOP_LEFT);
            money.setTranslateX(20);
            money.setTranslateY(17);
            gameView.getChildren().add(money);
        }
        if (!gameView.getChildren().contains(job)) {
            job.setStyle(styles);
            StackPane.setAlignment(job, Pos.TOP_LEFT);
            job.setTranslateX(20);
            job.setTranslateY(32);
            gameView.getChildren().add(job);
        }
    }

    public void update(Player player) {
        money.setText(player.getMoney() + " $");
        Random random = new Random();
        double randomValue = random.nextDouble();
        double prob = Math.min(1.0, player.getNbHoursOfSearch()/1000.0);
        if (randomValue < prob) {
            player.setJobbing(true);
        }
        if (player.isJobbing()) {
            job.setText("You have a job!");
            player.setMoney(player.getMoney() + 10);
        } else {
            job.setText("You don't have a job!");
        }
    }
}
