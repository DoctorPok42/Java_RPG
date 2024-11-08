package Class.Menu;

public class Option {
    protected String value;
    protected String name;
    protected Runnable action;

    public Option(String value, String name, Runnable action) {
        this.value = value;
        this.name = name;
        this.action = action;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public void select() {
        this.action.run();
    }
}
