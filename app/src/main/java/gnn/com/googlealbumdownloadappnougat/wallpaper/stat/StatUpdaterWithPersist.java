package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.util.DateProvider;
import gnn.com.googlealbumdownloadappnougat.util.DateUtil;

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
        stat.updateOnNewChange(new DateProvider());
        try {
            persist.write(stat);
        } catch (IOException e) {
            Log.e("GOI", "can not write stat");
        }
    }
}
