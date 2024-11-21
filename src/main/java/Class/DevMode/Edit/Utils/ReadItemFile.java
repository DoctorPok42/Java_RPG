package Class.DevMode.Edit.Utils;

import Class.Item.Item;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadItemFile {
    private final List<Item> itemList = new ArrayList<>();
    private final String filePath;

    public ReadItemFile(String filePath) {
        this.filePath = filePath;
    }

    public List<Item> readItems(Gson gson) throws IOException {
        Path itemFilePath = Paths.get(filePath);
        if (Files.exists(itemFilePath)) {
            Reader reader = Files.newBufferedReader(itemFilePath);
            Item[] existingObstacles = gson.fromJson(reader, Item[].class);
            if (existingObstacles != null) {
                itemList.addAll(Arrays.asList(existingObstacles));
            }
            reader.close();
        }

        return itemList;
    }

    public Path getItemFilePath() {
        return Paths.get(filePath);
    }
}
