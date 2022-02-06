package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import android.util.Log;

import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.util.DateProvider;

public class WallpaperStatProvider {

    // injected
    private final DateProvider currentDateProvider;
    private final PersistWallpaperStat persist;

    public WallpaperStatProvider(PersistWallpaperStat persist, DateProvider currentDateProvider) {
        this.currentDateProvider = currentDateProvider;
        this.persist = persist;
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
            Log.e("GOI", "can not write stat");
        }
    }
}
