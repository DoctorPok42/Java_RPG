package Class.Item;

import java.util.List;

public class Dialogues {
    private String namePnj;
    private String text;
    private int id;
    private List<Integer> choices;
    public Dialogues(String name, String text, int lvl, List<Integer> choices) {
        this.text = text;
        this.id = lvl;
        this.namePnj = name;
        this.choices = choices;
    }
    public String getText() {
        return this.text;
    }
    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.namePnj;
    }
    public List<Integer> getChoices() {
        return this.choices;
    }
}
