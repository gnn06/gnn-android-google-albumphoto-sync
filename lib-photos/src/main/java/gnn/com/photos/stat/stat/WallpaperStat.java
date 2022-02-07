package gnn.com.photos.stat.stat;

import java.util.Date;

import gnn.com.util.DateProvider;
import gnn.com.util.DateUtil;

public class WallpaperStat {

    private int nbChangeOnDate;

    private Date date;

    // public for test
    public WallpaperStat(int changeByDay, Date lastWallpaper) {
        this.nbChangeOnDate = changeByDay;
        this.date = lastWallpaper;
    }

    WallpaperStat(Date date) {
        this.nbChangeOnDate = 0;
        this.date = date;
    }

    public int getNbChangeOnDate() {
        return nbChangeOnDate;
    }

    public Date getDate() {
        return date;
    }

    private void increase() {
        this.nbChangeOnDate += 1;
    }

    private void reset(Date date) {
        this.nbChangeOnDate = 1;
        this.date = date;
    }

    void updateOnNewChange(DateProvider currentDateProvider) {
        DateUtil dateUtil = new DateUtil(currentDateProvider);
        if (dateUtil.isToday(this.getDate())) {
            this.increase();
        } else {
            Date newChangeDate = currentDateProvider.get();
            this.reset(newChangeDate);
        }
    }
}
