package Class.Item;

public class Item {
    protected String name;
    protected Enum<ItemType> type;
    protected float x;
    protected float y;
    protected int z;

    public Item(String name, Enum<ItemType> type, float x, float y, int z) {
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public Enum<ItemType> getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
