package Class.Skill;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SkillTest {

    @Test
    public void testGetNameWithValidName() {
        // Création d'un objet Skill avec un nom "Fireball"
        Skill fireball = new Skill("WEB");

        // Vérification que getName renvoie le bon nom
        assertEquals("WEB", fireball.getName(), "getName should return the correct name");
    }

    @Test
    public void testGetNameWithDifferentName() {
        // Création d'un objet Skill avec un autre nom "Heal"
        Skill heal = new Skill("Heal");

        // Vérification que getName renvoie le bon nom
        assertEquals("Heal", heal.getName(), "getName should return the correct name");
    }

    @Test
    public void testGetNameWithEmptyName() {
        // Création d'un objet Skill avec un nom vide
        Skill emptySkill = new Skill("");

        // Vérification que getName renvoie une chaîne vide
        assertEquals("", emptySkill.getName(), "getName should return an empty string if name is empty");
    }

    @Test
    public void testGetNameWithNullName() {
        // Création d'un objet Skill avec un nom null
        Skill nullSkill = new Skill(null);

        // Vérification que getName renvoie null
        assertEquals(null, nullSkill.getName(), "getName should return null if name is null");
    }

    @Test
    public void testGetLevelWithValidLevel() {
        // Création d'un objet Skill avec un niveau 5
        Skill skill = new Skill("WEB");

        // Vérification que getLevel renvoie le bon niveau
        assertEquals(0, skill.getLevel(), "getLevel should return the correct level");
    }

    @Test
    public void testSetLevelWithValidLevel() {
        // Création d'un objet Skill avec un niveau 5
        Skill skill = new Skill("WEB");

        // Modification du niveau à 10
        skill.setLevel(10);

        // Vérification que getLevel renvoie le bon niveau
        assertEquals(10, skill.getLevel(), "getLevel should return the correct level");
    }

    @Test
    public void testSetLevelWithNegativeLevel() {
        // Création d'un objet Skill avec un niveau 5
        Skill skill = new Skill("WEB");

        // Modification du niveau à -5
        skill.setLevel(-5);

        // Vérification que getLevel renvoie le bon niveau
        assertEquals(-5, skill.getLevel(), "getLevel should return the correct level");
    }
}
