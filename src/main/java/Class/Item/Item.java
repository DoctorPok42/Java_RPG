package Class.Item;

import Class.Character.Player;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Item {
    protected String name;
    protected Node itemView;
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

        switch (type) {
            case ItemType.PC:
                this.itemView = new ImageView(new Image("file:assets/items/pc.png"));
                break;
            case ItemType.CANAP:
                this.itemView = new ImageView(new Image("file:assets/items/canap.png"));
                break;
            case ItemType.DISTRIBUTOR:
                this.itemView = new ImageView(new Image("file:assets/items/distributor.png"));
                break;
            default:
                break;
        }
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

    public Node getItemView() {
        return itemView;
    }

    public boolean doAction(Player player, Enum<?> action, int time, String module) {
        return false;
    }
}
