package Class.bar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TirednessTest {

    @Test
    public void testGetNameWithValidName() {
        // Création d'un objet bar avec un nom "Health"
        bar healthBar = new Tiredness("Health");

        // Vérification que getName renvoie le bon nom
        assertEquals("Health", healthBar.getName(), "getName should return the correct name");
    }

    @Test
    public void testGetNameWithDifferentName() {
        // Création d'un objet bar avec un autre nom "Mana"
        bar manaBar = new Tiredness("Mana");

        // Vérification que getName renvoie le bon nom
        assertEquals("Mana", manaBar.getName(), "getName should return the correct name");
    }

    @Test
    public void testGetNameWithEmptyName() {
        // Création d'un objet bar avec un nom vide
        bar emptyBar = new Tiredness("");

        // Vérification que getName renvoie une chaîne vide
        assertEquals("", emptyBar.getName(), "getName should return an empty string if name is empty");
    }

    @Test
    public void testGetNameWithNullName() {
        // Création d'un objet bar avec un nom null
        bar nullBar = new Tiredness(null);

        // Vérification que getName renvoie null
        assertNull(nullBar.getName(), "getName should return null if name is null");
    }
}
