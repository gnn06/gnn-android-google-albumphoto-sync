package gnn.com.googlealbumdownloadappnougat.service;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class WorkerResultStore {

    public static final String FILE_NAME = "work.json";

    private final Context context;

    WorkerResultStore(Context context) {
        this.context = context;
    }

    void store(State result) throws IOException {
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

    Item[] readItems() throws FileNotFoundException {
        Gson gson = new Gson();
        if (new File(getFileStore()).exists()) {
            FileReader reader = new FileReader(getFileStore());
            // TODO gère la récupération d'un fichier dans un ancien format
            Item[] items = gson.fromJson(reader, Item[].class);
            return items;
        } else {
            return null;
        }
    }

    private void writeItems(Item[] items) throws IOException {
        Gson gson = new Gson();
        FileWriter writer = new FileWriter(getFileStore());
        gson.toJson(items, writer);
        writer.close();
    }

    private Item convert(State result) {
        return new Item(result);
    }

    enum State {
        SUCCESS, FAILURE;
    }

    class Item {
        private final State state;
        private final String dateTime;
        public Item(State state) {
            this.state = state;
            this.dateTime = LocalDateTime.now().toString();
        }
    }

    private String getFileStore() {
        return context.getFilesDir().getAbsoluteFile() + "/" + FILE_NAME;
    }
}
