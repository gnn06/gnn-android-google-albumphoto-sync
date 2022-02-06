package gnn.com.photos.stat.stat;

import java.io.File;
import java.io.IOException;

import gnn.com.util.DateProvider;
import gnn.com.googlealbumdownloadappnougat.util.Logger;

public class WallpaperStatProvider {

    // injected
    private final DateProvider currentDateProvider;
    private final PersistWallpaperStat persist;

    // For test -> package private
    WallpaperStatProvider(PersistWallpaperStat persist, DateProvider currentDateProvider) {
        this.currentDateProvider = currentDateProvider;
        this.persist = persist;
    }

    // Auto inject dependencies
    public WallpaperStatProvider(File processFolder) {
        this.currentDateProvider = new DateProvider();
        this.persist = new PersistWallpaperStat(processFolder);
    }

    public WallpaperStat get() {
        WallpaperStat stat = this.persist.read();
        if (stat == null) {
            stat = new WallpaperStat(currentDateProvider.get());
        }
        return stat;
    }

    public void  onWallpaperChange() {
        WallpaperStat stat = persist.read();
        if (stat == null) {
            stat = new WallpaperStat(currentDateProvider.get());
        }
        stat.updateOnNewChange(new DateProvider());
        try {
            persist.write(stat);
        } catch (IOException e) {
            Logger.getLogger().severe("can not write stat");
        }
    }
}
