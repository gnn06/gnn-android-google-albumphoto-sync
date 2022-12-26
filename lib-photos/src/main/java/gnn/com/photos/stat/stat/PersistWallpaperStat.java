package gnn.com.photos.stat.stat;

import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;

import gnn.com.googlealbumdownloadappnougat.util.Logger;

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
            if (result == null) {
                Logger logger = Logger.getLogger();
                try {
                    logger.severe("can't read wallpaper stat. Gson returns null. processFolder=" + processFolder + ", filename=" + STAT_FILENAME + "(file=" +
                                    FileUtils.readFileToString(new File(processFolder, PersistWallpaperStat.STAT_FILENAME), "UTF-8") + ")");
                } catch (IOException e) {
                    logger.severe("can't read wallpaper stat. Gson returns null. can't read file " + processFolder + ", filename=" + STAT_FILENAME);
                }
                throw new NullPointerException("can't read wallpaper stat. processFolder=" + processFolder + ", filename=" + STAT_FILENAME);
            }
            if (result.getDate() == null) {
                return null;
            }
            return result;
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
