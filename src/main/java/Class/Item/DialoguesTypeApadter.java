package Class.Item;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DialoguesTypeApadter extends TypeAdapter<Dialogues> {
    @Override
    public void write(JsonWriter out, Dialogues dialogues) throws IOException {
        // No need to implement this method
    }

    @Override
    public Dialogues read(JsonReader in) throws IOException {
        in.beginObject();

        String text = null;
        int id = 0;
        String namePnj = null;
        List<Integer> choices = new ArrayList<>();

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "text":
                    text = in.nextString();
                    break;
                case "id":
                    id = in.nextInt();
                    break;
                case "name":
                    namePnj = in.nextString();
                    break;
                case "choices":
                    in.beginArray();
                    while (in.hasNext()) {
                        choices.add(in.nextInt());
                    }
                    in.endArray();
                    break;

                default:
                    in.skipValue();
                    break;
            }
        }

        in.endObject();

        return new Dialogues(namePnj, text, id, choices);
    }
}
