package Class.Engine;

public abstract class Controler {
    protected String name;
    protected int currentAction = 0;

    protected Controler(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(int currentAction) {
        this.currentAction = currentAction;
    }
}
