package Class.Item;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import Class.Skill.Skill;

import java.io.IOException;

public class SkillTypeAdapter extends TypeAdapter<Skill> {
    @Override
    public void write(JsonWriter out, Skill skill) throws IOException {
        // No need to implement this method
    }

    @Override
    public Skill read(JsonReader in) throws IOException {
        in.beginObject();

        String name = null;

        while (in.hasNext()) {
            if (in.nextName().equals("name")) {
                name = in.nextString();
            } else {
                in.skipValue();
            }
        }

        in.endObject();

        return new Skill(name);
    }
}
