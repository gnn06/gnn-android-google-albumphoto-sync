package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import java.io.File;
import java.io.FileNotFoundException;

import gnn.com.googlealbumdownloadappnougat.util.DateProvider;

public class WallpaperStatProvider {

    // injected
    private final File processFolder;
    private final DateProvider currentDateProvider;

    public WallpaperStatProvider(File processFolder, DateProvider currentDateProvider) {
        this.processFolder = processFolder;
        this.currentDateProvider = currentDateProvider;
    }

    public WallpaperStat get() {
        WallpaperStat stat = new PersistWallpaperStat(processFolder).read();
        if (stat == null) {
            stat = new WallpaperStatFactory(currentDateProvider).get();
        }
        return stat;
    }
}
