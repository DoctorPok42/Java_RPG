package Class.bar;

import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class barTest {

    @Test
    public void testGetNameWithValidName() {
        // Création d'un objet bar avec un nom "Health"
        bar healthBar = new bar("Health", new ImageView());

        // Vérification que getName renvoie le bon nom
        assertEquals("Health", healthBar.getName(), "getName should return the correct name");
    }

    @Test
    public void testGetNameWithDifferentName() {
        // Création d'un objet bar avec un autre nom "Mana"
        bar manaBar = new bar("Mana", new ImageView());

        // Vérification que getName renvoie le bon nom
        assertEquals("Mana", manaBar.getName(), "getName should return the correct name");
    }

    @Test
    public void testGetNameWithEmptyName() {
        // Création d'un objet bar avec un nom vide
        bar emptyBar = new bar("", new ImageView());

        // Vérification que getName renvoie une chaîne vide
        assertEquals("", emptyBar.getName(), "getName should return an empty string if name is empty");
    }
}
