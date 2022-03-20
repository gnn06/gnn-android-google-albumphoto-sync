package gnn.com.photos.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.Photo;

public class Cache {

    public static final int DELAY_ALWAYS_EXPIRE = 0;
    public static final int DELAY_NEVER_EXPIRE = Integer.MAX_VALUE;

    private static File file = null;

    // 0 => no cache
    // MAX_VALUE => always use cache
    // in seconds
    private static long maxAgeHour = 0;

    public static void config(File file, long maxAgeHour) {
        Cache.file = file;
        Cache.maxAgeHour = maxAgeHour;
    }

    private ArrayList<Photo> photos;

    /**
     * Singleton pattern
     */
    private static Cache _cache;

    // Used to Unit test
    public static Cache getCache() {
        if (_cache == null) {
            _cache = new Cache();
        }
        return _cache;
    }

    /**
     * if no cache file, store only in memory
     * @return null if cache no cache or expired
     * @throws IOException
     */
    public ArrayList<Photo> get() throws IOException {
        Logger logger = Logger.getLogger();
        ArrayList<Photo> photos;
        if (file != null && file.exists()) {
            long delayMilli = System.currentTimeMillis() - file.lastModified();
            if (delayMilli < maxAgeHour * 60 * 60 * 1000) {
                // valid cache
                photos = read();
                logger.fine("use photo list cache");
            } else {
                // cache expired
                reset();
                logger.fine("photo list cache expired with delay=" + maxAgeHour);
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
        Logger logger = Logger.getLogger();
        if (file != null) {
            ObjectInputStream iis = new ObjectInputStream(new FileInputStream(file));
            ArrayList<Photo> result = null;
            try {
                result = (ArrayList<Photo>) iis.readObject();
            } catch (ClassNotFoundException e) {
                logger.severe("can't deserialize");
            }
            return result;
        } else return null;
    }

    public void reset() {
        this.photos = null;
        if (file != null) {
            file.delete();
        }
    }

    /**
     * @return Date or null
     */
    static public Date getLastUpdateTime(File file) {
        if (file != null && file.exists()) {
            return new Date(file.lastModified());
        }
        return null;
    }
}
