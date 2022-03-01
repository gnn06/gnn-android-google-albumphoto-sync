package gnn.com.photos.sync;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;

import gnn.com.photos.Photo;
import gnn.com.util.DateProvider;

public class PersistChoose {

    static final String FILENAME = "lastchoose.json";

    private final File processFolder;

    public PersistChoose(File processFolder) {
        this.processFolder = processFolder;
    }

    void write(Photo photo) throws IOException {
        PhotoChoose choose = new PhotoChoose(photo, new DateProvider().get());
        FileWriter writer = new FileWriter(new File(processFolder, FILENAME));
        new Gson().toJson(choose, writer);
        writer.close();
    }

    /**
     *
     * @return null if FileNotFound, null if file mal formated
     */
    PhotoChoose read() {
        try {
            Reader reader = new FileReader(new File(processFolder, FILENAME));
            PhotoChoose choose = new Gson().fromJson(reader, PhotoChoose.class);
            if (choose.photo == null || choose.chooseDate == null) {
                return null;
            } else {
                return choose;
            }
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public Photo getCurrentPhoto() {
        PhotoChoose photoChoose = read();
        return photoChoose != null ? photoChoose.photo : null;
    }

    public Date getLastChooseDate() {
        PhotoChoose photoChoose = read();
        return photoChoose != null ? photoChoose.chooseDate : null;
    }
}

