package gnn.com.photos.sync;

import java.io.File;

public class PersistSyncTime extends PersistTime{
    public PersistSyncTime(File processFolder) {
        super(processFolder, "last_sync");
    }
}
