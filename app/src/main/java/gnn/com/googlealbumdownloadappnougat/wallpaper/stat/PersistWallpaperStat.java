package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class PersistWallpaperStat {

    private static final String STAT_FILENAME = "wallpaper.json";

    private final File processFolder;

    public PersistWallpaperStat(File processFolder) {
        this.processFolder = processFolder;
    }

    void write(WallpaperStat stat) throws IOException {
        FileWriter writer = new FileWriter(new File(processFolder, STAT_FILENAME));
        new Gson().toJson(stat, writer);
        writer.close();
    }

    WallpaperStat read() {
        try {
            Reader reader = new FileReader(new File(processFolder, STAT_FILENAME));
            return new Gson().fromJson(reader, WallpaperStat.class);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
