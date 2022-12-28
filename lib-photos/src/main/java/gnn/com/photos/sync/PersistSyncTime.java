package gnn.com.photos.sync;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import gnn.com.googlealbumdownloadappnougat.util.Logger;

public class PersistSyncTime extends PersistTime{

    public static final String FILENAME = "last_sync";

    private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public PersistSyncTime(File processFolder) {
        super(processFolder, FILENAME);
    }

    /**
     *
     * @return list are null
     */
    public SyncData retrieveSyncResult() {
        Reader reader = null;
        try {
            reader = new FileReader(new File(processFolder, FILENAME));
            // Gson use default constructor
            SyncData result = gson.fromJson(reader, SyncData.class);
            return result;
        } catch (FileNotFoundException|JsonSyntaxException e) {
            return null;
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException ignored) {}
        }

    }

    public void storeTimeWithResult(SyncData data) throws IOException {
        // require Logger was initialized
        Logger logger = Logger.getLogger();
        if (processFolder != null) {
            File file = new File(processFolder, filename);
            logger.finest(file.getAbsolutePath());
            FileWriter writer = new FileWriter(file);
            gson.toJson(data, writer);
            writer.close();
        }

    }
}
