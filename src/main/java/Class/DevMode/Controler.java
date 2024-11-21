package Class.DevMode;

public abstract class Controler {
    protected String name;

    protected Controler(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
