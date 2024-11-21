package Class.DevMode;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class KeyControler extends Controler {
    private final List<KeyCode> keys = new ArrayList<>();

    public KeyControler(String name, List<KeyCode> keys) {
        super(name);
        this.keys.addAll(keys);
    }

    public KeyCode listenKey(KeyEvent e) {
        KeyCode keyPress = e.getCode();
        if (keys.contains(keyPress)) {
            return keyPress;
        }
        return null;
    }
}
