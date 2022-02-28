package gnn.com.photos.stat.stat;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

class PersistWallpaperStat {

    static final String STAT_FILENAME = "stat_wallpaper.json";

    private final File processFolder;

    public PersistWallpaperStat(File processFolder) {
        this.processFolder = processFolder;
    }

    void write(WallpaperStat stat) throws IOException {
        FileWriter writer = new FileWriter(new File(processFolder, STAT_FILENAME));
        new Gson().toJson(stat, writer);
        writer.close();
    }

    /**
     *
     * @return null if no file exists, null if file not contains date
     */
    WallpaperStat read() {
        try {
            Reader reader = new FileReader(new File(processFolder, STAT_FILENAME));
            WallpaperStat result = new Gson().fromJson(reader, WallpaperStat.class);
            if (result.getDate() == null) {
                return null;
            }
            return result;
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
