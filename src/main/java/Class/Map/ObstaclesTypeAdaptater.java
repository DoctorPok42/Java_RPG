package Class.Map;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ObstaclesTypeAdaptater extends TypeAdapter<Obstacles> {
    @Override
    public void write(JsonWriter out, Obstacles obstacles) throws IOException {
        // No need to implement this method
    }

    @Override
    public Obstacles read(JsonReader in) throws IOException {
        in.beginObject();

        double x = 0;
        double y = 0;
        double width = 0;
        double height = 0;
        int id = 0;

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "x":
                    x = in.nextDouble();
                    break;
                case "y":
                    y = in.nextDouble();
                    break;
                case "width":
                    width = in.nextDouble();
                    break;
                case "height":
                    height = in.nextDouble();
                    break;
                case "id":
                    id = in.nextInt();
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }

        in.endObject();

        return new Obstacles(x, y, width, height, id);
    }

}