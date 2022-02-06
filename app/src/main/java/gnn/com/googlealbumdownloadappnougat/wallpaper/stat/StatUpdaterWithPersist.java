package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.util.DateProvider;

public class StatUpdaterWithPersist {

    // Injected
    private final PersistWallpaperStat persist;

    // Injected
    private DateProvider dateProvider;

    public StatUpdaterWithPersist(PersistWallpaperStat persist, DateProvider dateProvider) {
        this.persist = persist;
        this.dateProvider = dateProvider;
    }

    public void  onWallpaperChange() {
        WallpaperStat stat = persist.read();
        if (stat == null) {
            stat = new WallpaperStatFactory(new DateProvider()).get();
        }
        new StatUpdater(new DateProvider()).updateOnNewChange(stat);
        try {
            persist.write(stat);
        } catch (IOException e) {
            Log.e("GOI", "can not write stat");
        }

    }
}
