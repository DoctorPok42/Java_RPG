package Class.Character;

import Class.Map.Map;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.scene.image.Image;

import javax.management.relation.Role;
import java.io.IOException;

public class PnjTypeAdapter extends TypeAdapter<Pnj> {
    @Override
    public void write(JsonWriter out, Pnj pnj) throws IOException {
        // No need to implement this method
    }

    @Override
    public Pnj read(JsonReader in) throws IOException {
        in.beginObject();

        String name = null;
        String roleString = null;
        double x = 0;
        double y = 0;
        int z = 0;
        Image texture = null;

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "name":
                    name = in.nextString();
                    break;
                case "role":
                    roleString = in.nextString();
                    break;
                case "x":
                    x =  in.nextDouble();
                    break;
                case "y":
                    y =  in.nextDouble();
                    break;
                case "z":
                    z = in.nextInt();
                    break;
                case "texture":
                    texture = new Image(in.nextString());
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }

        in.endObject();

        // Convert the role string to a role
        Roles type = Roles.valueOf(roleString.toUpperCase());

        return new Pnj(name, type, texture, x, y);
    }
}
