package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

class PersistWallpaperStat {

    private static final String STAT_FILENAME = "wallpaper.json";

    void write(WallpaperStat stat) throws IOException {
        FileWriter writer = new FileWriter(STAT_FILENAME);
        new Gson().toJson(stat, writer);
        writer.close();
    }

    WallpaperStat read() throws FileNotFoundException {
        Reader reader = new FileReader(STAT_FILENAME);
        WallpaperStat wallpaperStat = new Gson().fromJson(reader, WallpaperStat.class);
        return wallpaperStat;
    }
}
