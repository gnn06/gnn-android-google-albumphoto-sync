package gnn.com.photos.sync;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.util.Logger;

import javax.annotation.Nonnull;

abstract public class PersistTime {

    private final File processFolder;
    private final String filename;

    public PersistTime(@Nonnull File processFolder, String filename) {
        this.processFolder = processFolder;
        this.filename = filename;
    }

    public void storeTime() throws IOException {
        // require Logger was initialized
        Logger logger = Logger.getLogger();
        if (processFolder != null) {
            File file = new File(processFolder, filename);
            logger.finest(file.getAbsolutePath());
            FileWriter writer = new FileWriter(file);
            writer.write("sync time");
            writer.close();
        }
    }

    /**
     * @return format = MM/dd/yyyy HH:mm:ss
     * null if no previous sync
     */
    public Date retrieveTime() {
        Date stringLastModified = null;
        if (processFolder != null) {
            File file = new File(processFolder, filename);
            if (file.exists()) {
                return new Date(file.lastModified());
            }
        }
        return stringLastModified;
    }

    public void reset() {
        File file = new File(processFolder, filename);
        file.delete();
    }
}