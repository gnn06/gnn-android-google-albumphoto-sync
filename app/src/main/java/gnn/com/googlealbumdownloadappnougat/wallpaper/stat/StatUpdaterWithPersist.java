package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import java.io.File;

import gnn.com.googlealbumdownloadappnougat.util.DateProvider;

public class StatUpdaterWithPersist {

    // Injected
    private final File processFolder;

    public StatUpdaterWithPersist(File processFolder) {
        this.processFolder = processFolder;
    }

    public void  onWallpaperChange() {
        PersistWallpaperStat persist = new PersistWallpaperStat(processFolder);
        WallpaperStat stat;
        try {
            stat = persist.read();
            new StatUpdater(new DateProvider()).updateOnNewChange(stat);
            persist.write(stat);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
