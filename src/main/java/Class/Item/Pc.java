package Class.Item;

public class Pc extends Item {
    private Enum<PcTypeAction> typeAction = PcTypeAction.WORK;

    public Pc(String name, Enum<ItemType> type, float x, float y, int z, Enum<PcTypeAction> typeAction) {
        super(name, type, x, y, z);
        this.typeAction = typeAction;
    }

    public Enum<PcTypeAction> getTypeAction() {
        return typeAction;
    }

    public void displayMenu() {
        System.out.println("Displaying menu...");
    }

    public void doAction(int nbSleep) {
        System.out.println("Doing action...");
    }
}
