package Class.Item;

public class Canap extends Item {
    public Canap(String name, Enum<ItemType> type, float x, float y, int z, int nbPlaces) {
        super(name, type, x, y, z);
    }

    public void sleep() {
        System.out.println("Sleeping...");
    }
}
