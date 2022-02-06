package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.util.DateProvider;
import gnn.com.googlealbumdownloadappnougat.util.DateUtil;

public class WallpaperStat {

    private int nbChangeOnLastDay = 0;

    private Date lastChangeDate = null;

    public WallpaperStat(int changeByDay, Date lastWallpaper) {
        this.nbChangeOnLastDay = changeByDay;
        this.lastChangeDate = lastWallpaper;
    }

    public WallpaperStat(Date lastChangeDate) {
        this.nbChangeOnLastDay = 0;
        this.lastChangeDate = lastChangeDate;
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

    public void increase() {
        this.nbChangeOnLastDay += 1;
    }

    public void reset(Date date) {
        this.nbChangeOnLastDay = 1;
        this.lastChangeDate = date;
    }

    void updateOnNewChange(DateProvider currentDateProvider) {
        DateUtil dateUtil = new DateUtil(currentDateProvider);
        if (dateUtil.isToday(this.getLastChangeDate())) {
            this.increase();
        } else {
            Date newChangeDate = currentDateProvider.get();
            this.reset(newChangeDate);
        }
    }
}
