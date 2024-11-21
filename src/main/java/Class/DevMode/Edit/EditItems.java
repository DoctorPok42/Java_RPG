package Class.DevMode.Edit;

import Class.Item.ItemType;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class EditItems {
    private final ImageView img;
    private final ItemType type;
    private final String name;
    private final int skin;

    private static final List<EditItems> allitems = new ArrayList<>();

    public EditItems(ImageView img, ItemType type, String name, int skin) {
        this.img = img;
        this.type = type;
        this.name = name;
        this.skin = skin;

        allitems.add(this);
    }

    public ImageView getImg() {
        return img;
    }

    public ItemType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getSkin() {
        return skin;
    }

    public static List<ImageView> getAllItemsView() {
        List<ImageView> listAllItems = new ArrayList<>();
        for (EditItems item : allitems) {
            listAllItems.add(item.getImg());
        }
        return listAllItems;
    }
}
