package Class.Menu;

import java.util.List;

public class Menu {
    protected String name;
    protected String title;
    protected List<Option> options;
    protected int selectedOption;
    protected int id;

    public Menu(String name, String title, List<Option> options, int id) {
        this.name = name;
        this.title = title;
        this.options = options;
        this.selectedOption = 0;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public String getTitle() {
        return this.title;
    }

    public List<Option> getOptions() {
        return this.options;
    }

    public int getSelectedOption() {
        return this.selectedOption;
    }

    public int getId() {
        return this.id;
    }

    public void up() {
    }

    public void down() {
    }
}
