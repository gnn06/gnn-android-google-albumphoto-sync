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

    private final File file;
    private final long cache_period_validity;

    private ArrayList<Photo> photos;

    /**
     * @param file File to use to store and retrieve cache
     *             Null if no cache
     * @param period_validity cache period validity in milliseconds
     *                        Useless if cache is null
     */
    public Cache(File file, long period_validity) {
        this.file = file;
        this.cache_period_validity = period_validity;
    }

    /**
     * if no cache file, store only in memory
     * @return null if cache no cache or expired
     * @throws IOException
     */
    public ArrayList<Photo> get() throws IOException {
        ArrayList<Photo> photos;
        if (file != null && file.exists()) {
            long delay = System.currentTimeMillis() - file.lastModified();
            if (delay < this.cache_period_validity) {
                // valid cache
                photos = read();
                Log.i("Cache", "use cache");
            } else {
                // cache expired
                reset();
                Log.i("Cache", "cache expired");
                return null;
            }
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
