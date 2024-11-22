package Class.bar;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class barTest {

    private bar testBar;
    private StackPane gameView;

    @BeforeEach
    public void setUp() {
        // Initialiser un bar avec un nom et une texture factice
        ImageView mockTexture = new ImageView();
        testBar = new bar("Health", mockTexture);

        // Initialiser un StackPane vide pour simuler la scène de jeu
        gameView = new StackPane();
    }

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

    @Test
    public void testDisplayAddsTextureAndBar() {
        // Appeler la méthode display
        testBar.display(gameView);

        // Vérifier que deux enfants ont été ajoutés
        assertEquals(2, gameView.getChildren().size(), "The StackPane should contain 2 children after display is called");

        // Vérifier que le premier enfant est la texture
        assertSame(testBar.texture, gameView.getChildren().get(0), "The first child should be the texture");

        // Vérifier que le second enfant est la barre
        assertSame(testBar.bar, gameView.getChildren().get(1), "The second child should be the bar");
    }

    @Test
    public void testDisplayWithEmptyStackPane() {
        // Vérifier que le StackPane est initialement vide
        assertTrue(gameView.getChildren().isEmpty(), "The StackPane should be empty before display is called");

        // Appeler la méthode display
        testBar.display(gameView);

        // Vérifier que les éléments sont ajoutés
        assertFalse(gameView.getChildren().isEmpty(), "The StackPane should not be empty after display is called");
    }
}
