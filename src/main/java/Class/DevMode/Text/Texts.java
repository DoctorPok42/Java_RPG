package Class.DevMode.Text;

import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Texts {
    protected String name;
    protected Text content;
    protected final Font font = Font.loadFont("file:assets/font/PressStart2P-Regular.ttf", 13);
    protected static final String styles = "-fx-font-size: 13;" + "-fx-font-family: 'Press Start 2P';" + "-fx-font-weight: bold;";
    protected final KeyCode keyListener;
    protected boolean display = true;
    private static final List<Texts> allTexts = new ArrayList<>();

    public Texts(String name, String content, KeyCode keyListener, boolean... display) {
        this.name = name;
        this.content = new Text(content);
        this.keyListener = keyListener;
        allTexts.add(this);
        if (display.length > 0) {
            this.display = display[0];
        }

        StackPane.setAlignment(this.content, Pos.TOP_LEFT);
        this.content.setTranslateX(20);
        this.content.setFill(Color.rgb(243, 159, 24));
    }

    public String getName() {
        return name;
    }

    public Text getContent() {
        return content;
    }

    public void setContent(Text content) {
        this.content = content;
    }

    public KeyCode getKeyListener() {
        return keyListener;
    }

    public static List<KeyCode> getAllKeyListeners() {
        List<KeyCode> keyListeners = new ArrayList<>();
        for (Texts text : allTexts) {
            keyListeners.add(text.getKeyListener());
        }
        return keyListeners;
    }

    public boolean getDisplay() {
        return display;
    }
}
