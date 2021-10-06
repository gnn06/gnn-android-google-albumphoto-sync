package gnn.com.photos.sync;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class PersistSyncTime {

    final File processFolder;

    public PersistSyncTime(File processFolder) {
        this.processFolder = processFolder;
    }

    // TODO 04/10 extract store et retrieve methods

    void storeSyncTime() throws IOException {
        if (processFolder != null) {
            File file = new File(processFolder, "last_sync");
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
    public Date retrieveLastSyncTime() {
        Date stringLastModified = null;
        if (processFolder != null) {
            File file = new File(processFolder, "last_sync");
            if (file.exists()) {
                return new Date(file.lastModified());
            }
        }
        return stringLastModified;
    }
}