package gnn.com.googlealbumdownloadappnougat;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import androidx.work.WorkInfo;

class WorkerResultStore {

    public static final String FILE_NAME = "work.json";

    void store(WorkInfo result) throws IOException {
        Item item = convert(result);
        Item[] items1 = readItems();
        List<Item> items2;
        if (items1 == null) {
            items2 = new ArrayList<Item>(1);
        } else {
            items2 = new ArrayList(Arrays.asList(items1));
        }
        items2.add(item);
        writeItems((Item[]) items2.toArray(new Item[items2.size()]));
    }

    private Item[] readItems() throws FileNotFoundException {
        Gson gson = new Gson();
        if (new File(FILE_NAME).exists()) {
            FileReader reader = new FileReader(FILE_NAME);
            Item[] items = gson.fromJson(reader, Item[].class);
            return items;
        } else {
            return null;
        }
    }

    private void writeItems(Item[] items) throws IOException {
        Gson gson = new Gson();
        FileWriter writer = new FileWriter(FILE_NAME);
        gson.toJson(items, writer);
        writer.close();
    }

    private Item convert(WorkInfo result) {
        return new Item(result.getState());
    }

    private class Item {
        private final String state;

        public Item(WorkInfo.State state) {
            this.state = state.name();
        }
    }
}
