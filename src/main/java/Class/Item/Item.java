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
    protected int skin;
    protected int id;

    public Item(String name, Enum<ItemType> type, float x, float y, int z, int skin, int... id) {
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.skin = skin;
        if (id.length > 0) {
            this.id = id[0];
        }

        switch (type) {
            case ItemType.PC:
                this.itemView = new ImageView(new Image("file:assets/items/pc.png"));
                break;
            case ItemType.CANAP:
                switch (skin) {
                    case 1:
                        this.itemView = new ImageView(new Image("file:assets/items/canapplusgros.png"));
                        break;
                    case 2:
                        this.itemView = new ImageView(new Image("file:assets/items/canapplusgros2.png"));
                        break;
                    default:
                        break;
                }
                break;
            case ItemType.DISTRIBUTOR:
                this.itemView = new ImageView(new Image("file:assets/items/distributeur.png"));
                break;
            case ItemType.TABLE:
                switch (skin) {
                    case 1:
                        this.itemView = new ImageView(new Image("file:assets/items/table1.png"));
                        break;
                    case 2:
                        this.itemView = new ImageView(new Image("file:assets/items/table2.png"));
                        break;
                    case 3:
                        this.itemView = new ImageView(new Image("file:assets/items/table3.png"));
                        break;
                    case 4:
                        this.itemView = new ImageView(new Image("file:assets/items/table4.png"));
                    case 5:
                        this.itemView = new ImageView(new Image("file:assets/items/table5.png"));
                    default:
                        break;
                }
                break;
            case ItemType.PNJ:
                this.itemView = new ImageView(new Image("file:assets/item/pnjinteract.png"));
                break;
            default:
                break;
        }

        this.width = (int) itemView.getImage().getWidth();
        this.height = (int) itemView.getImage().getHeight();
        if (type == ItemType.PNJ) {
            this.width = 32;
            this.height = 46;
        }
        this.hitbox = new Rectangle((int) x, (int) y, width, height);
        this.hitbox.setFill(Color.TRANSPARENT);

        this.interactionHitbox = new Rectangle((int) x - 40, (int) y - 40, width + 80, height + 80);
        this.interactionHitbox.setFill(Color.TRANSPARENT);
        this.interactionHitbox.setStroke(Color.TRANSPARENT);
        this.interactionHitbox.setStrokeWidth(2);

        this.selected_menu = new ImageView(new Image("file:assets/interact/test4.png"));
        this.selected_menu.setFitWidth(91.96);
        this.selected_menu.setFitHeight(37.5);
    }

    public int getId() {
        return id;
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

    public int getSkin() {
        return skin;
    }

    public void setId(int id) {
        this.id = id;
    }

}
