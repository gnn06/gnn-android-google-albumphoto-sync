package gnn.com.photos.service;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import gnn.com.photos.model.Photo;

public class Cache {
    private ArrayList<Photo> photos;
    private File file;

    /**
     * @param file File to use to store and retrieve cache
     */
    public Cache(File file) {
        this.file = file;
    }

    public ArrayList<Photo> get() throws IOException {
        ArrayList<Photo> photos = null;
        if (file != null && file.exists()) {
            photos = read();
            Log.i("Cache", "use cache");
        } else {
            photos = this.photos;
        }
        return photos;
    }

    public void put(ArrayList<Photo> photos) throws IOException {
        this.photos = photos;
        if (file != null) {
            write();
        }
    }

    void write() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(photos);
    }

    ArrayList<Photo> read() throws IOException {
        ObjectInputStream iis = new ObjectInputStream(new FileInputStream(file));
        ArrayList<Photo> result = null;
        try {
            result = (ArrayList<Photo>) iis.readObject();
        } catch (ClassNotFoundException e) {
            Log.e("Cach", "can't deserialize");
        }
        return result;
    }

    public void reset() {
        this.photos = null;
        if (this.file != null) {
            file.delete();
        }
    }
}
