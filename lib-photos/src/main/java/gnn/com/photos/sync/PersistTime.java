package gnn.com.photos.sync;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

abstract public class PersistTime {

    private final File processFolder;
    private final String filename;

    public PersistTime(File processFolder, String filename) {
        this.processFolder = processFolder;
        this.filename = filename;
    }

    // TODO 04/10 extract store et retrieve methods

    void storeTime() throws IOException {
        if (processFolder != null) {
            File file = new File(processFolder, filename);
            System.out.println(file.getAbsolutePath());
            FileWriter writer = new FileWriter(file);
            // TODO get current time and write it
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
}