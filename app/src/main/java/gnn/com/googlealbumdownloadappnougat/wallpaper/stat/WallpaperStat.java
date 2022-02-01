package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import java.util.Date;

public class WallpaperStat {

    private int changeByDay = 0;

    public WallpaperStat(int changeByDay, Date lastWallpaper) {
        this.changeByDay = changeByDay;
        this.lastWallpaper = lastWallpaper;
    }

    private Date lastWallpaper = null;

    public int getChangeByDay() {
        return changeByDay;
    }

    private void setChangeByDay(int changeByDay) {
        this.changeByDay = changeByDay;
    }


    public Date getLastWallpaper() {
        return lastWallpaper;
    }

    private void setLastWallpaper(Date lastWallpaper) {
        this.lastWallpaper = lastWallpaper;
    }
}
