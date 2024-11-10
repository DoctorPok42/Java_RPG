package Class.Item;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class ItemTypeAdapter extends TypeAdapter<Item> {
    @Override
    public void write(JsonWriter out, Item item) throws IOException {
    }

    @Override
    public Item read(JsonReader in) throws IOException {
        in.beginObject();

        String name = null;
        String typeString = null;
        float x = 0;
        float y = 0;
        int z = 0;

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "name":
                    name = in.nextString();
                    break;
                case "type":
                    typeString = in.nextString();
                    break;
                case "x":
                    x = (float) in.nextDouble();
                    break;
                case "y":
                    y = (float) in.nextDouble();
                    break;
                case "z":
                    z = in.nextInt();
                    break;
            }
        }

        in.endObject();

        // Convert the type string to an ItemType
        ItemType type = ItemType.valueOf(typeString.toUpperCase());

        return new Item(name, type, x, y, z);
    }
}
