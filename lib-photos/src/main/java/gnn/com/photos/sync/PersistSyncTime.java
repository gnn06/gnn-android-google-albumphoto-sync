package gnn.com.photos.sync;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    public Temp retrieveTimeWithResult() {
        try {
            Reader reader = new FileReader(new File(processFolder, FILENAME));
            Temp result = gson.fromJson(reader, Temp.class);
            return result;
        } catch (FileNotFoundException e) {
            return null;
        }

    }

    public void storeTimeWithResult(Temp data) throws IOException {
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
