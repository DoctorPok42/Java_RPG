package Class.Menu;

import java.util.List;

public class Start extends Menu {
    public Start() {
        super("Start", "Start Menu", List.of(
            new Option("newGame", "Start New Game", () -> {
                // System.out.println("New Game");
            }),
            new Option("loadGame", "Load Game", () -> {
                // System.out.println("Load Game");
            }),
            new Option("exit", "Exit", () -> {
                // System.out.println("Exit");
            })
        ), 0);
    }

    public void selectOption() {
        this.options.get(this.selectedOption).select();
    }

    public void up() {
        if (this.selectedOption > 0) {
            this.selectedOption--;
        }
    }

    public void down() {
        if (this.selectedOption < this.options.size() - 1) {
            this.selectedOption++;
        }
    }
}
