package gnn.com.googlealbumdownloadappnougat.service;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;

class WorkerResultStore {

    public static final String FILE_NAME = "work.json";

    private final File processFolder;

    WorkerResultStore(Context context) {
        this.processFolder = new File(ApplicationContext.getInstance(context).getProcessPath()
                + "/" + FILE_NAME);
    }

    void store(Item.State result) throws IOException {
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
        if (this.processFolder.exists()) {
            FileReader reader = new FileReader(this.processFolder);
            Item[] items = gson.fromJson(reader, Item[].class);
            return items;
        } else {
            return null;
        }
    }

    private void writeItems(Item[] items) throws IOException {
        Gson gson = new Gson();
        FileWriter writer = new FileWriter(this.processFolder);
        gson.toJson(items, writer);
        writer.close();
    }

    private Item convert(Item.State result) {
        return new Item(result);
    }
}
