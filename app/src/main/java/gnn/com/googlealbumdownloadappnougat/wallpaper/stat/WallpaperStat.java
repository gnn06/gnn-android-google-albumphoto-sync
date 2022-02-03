package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import java.util.Date;

public class WallpaperStat {

    private int nbChangeOnLastDay = 0;

    private Date lastChangeDate = null;

    public WallpaperStat(int changeByDay, Date lastWallpaper) {
        this.nbChangeOnLastDay = changeByDay;
        this.lastChangeDate = lastWallpaper;
    }

    public int getNbChangeOnLastDay() {
        return nbChangeOnLastDay;
    }

    private void setNbChangeOnLastDay(int nbChangeOnLastDay) {
        this.nbChangeOnLastDay = nbChangeOnLastDay;
    }

    public Date getLastChangeDate() {
        return lastChangeDate;
    }

    private void setLastChangeDate(Date lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public void increase(Date date) {
        this.nbChangeOnLastDay += 1;
        this.lastChangeDate = date;
    }

    public void reset(Date date) {
        this.nbChangeOnLastDay = 1;
        this.lastChangeDate = date;
    }
}
