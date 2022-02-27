package gnn.com.photos.stat.stat;

import java.util.Date;

import gnn.com.util.DateProvider;
import gnn.com.util.DateUtil;

public class WallpaperStat {

    private int changeOnDate;
    private int changeOnDayBefore;
    private Date date;

    // public for test
    public WallpaperStat(int changeByDay, int changeOnDayBefore, Date lastWallpaper) {
        this.changeOnDate = changeByDay;
        this.changeOnDayBefore = changeOnDayBefore;
        this.date = lastWallpaper;
    }

    WallpaperStat(Date date) {
        this.changeOnDate = 0;
        this.changeOnDayBefore = 0;
        this.date = date;
    }

    public int getChangeOnDate() {
        return changeOnDate;
    }

    public int getChangeOnDayBefore() {
        return changeOnDayBefore;
    }

    public Date getDate() {
        return date;
    }

    private void increase() {
        this.changeOnDate += 1;
    }

    private void reset(Date date) {
        this.changeOnDayBefore = this.changeOnDate;
        this.changeOnDate = 1;
        this.date = date;
    }

    void updateOnNewChange(DateProvider currentDateProvider) {
        DateUtil dateUtil = new DateUtil(currentDateProvider);
        if (this.getDate() != null && dateUtil.isToday(this.getDate())) {
            this.increase();
        } else {
            Date newChangeDate = currentDateProvider.get();
            this.reset(newChangeDate);
        }
    }
}
