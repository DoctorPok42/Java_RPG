package Class.Item;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Item {
    protected String name;
    protected ImageView itemView;
    protected Enum<ItemType> type;
    protected Rectangle hitbox;
    protected Rectangle interactionHitbox;
    protected int width;
    protected int height;
    protected float x;
    protected float y;
    protected int z;
    protected List<ImageView> menu = new ArrayList<ImageView>();
    protected List<List<ImageView>> second_menu = new ArrayList<List<ImageView>>();
    protected ImageView selected_menu = null;

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
                this.itemView = new ImageView(new Image("file:assets/items/distributeur.png"));
                break;
            default:
                break;
        }

        this.width = (int) itemView.getImage().getWidth();
        this.height = (int) itemView.getImage().getHeight();
        this.hitbox = new Rectangle((int) x, (int) y, width, height);
        this.hitbox.setFill(Color.TRANSPARENT);

        this.interactionHitbox = new Rectangle((int) x - 40, (int) y - 40, width + 80, height + 80);
        this.interactionHitbox.setFill(Color.TRANSPARENT);

        this.selected_menu = new ImageView(new Image("file:assets/interact/test4.png"));
        this.selected_menu.setFitWidth(91.96);
        this.selected_menu.setFitHeight(37.5);
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

    public Rectangle getHitbox() {
        return hitbox;
    }

    public Rectangle getInteractionHitbox() {
        return interactionHitbox;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean doAction() {
        return false;
    }

    public List<?> getActions() {
        return null;
    }

    public List<ImageView> getMenu() {
        return menu;
    }

    public List<List<ImageView>> getSecondMenu() {
        return second_menu;
    }

    public ImageView getSelectedMenu() {
        return selected_menu;
    }
}
