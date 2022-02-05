package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import java.io.File;
import java.io.FileNotFoundException;

import gnn.com.googlealbumdownloadappnougat.util.DateProvider;

public class WallpaperStatProvider {

    // injected
    private final File processFolder;

    public WallpaperStatProvider(File processFolder) {
        this.processFolder = processFolder;
    }

    public WallpaperStat get() {
        try {
            return new PersistWallpaperStat(processFolder).read();
        } catch (FileNotFoundException e) {
            return new WallpaperStatFactory(new DateProvider()).get();
        }
    }
}
