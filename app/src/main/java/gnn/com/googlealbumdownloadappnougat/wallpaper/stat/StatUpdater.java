package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.util.DateProvider;
import gnn.com.googlealbumdownloadappnougat.util.DateUtil;

class StatUpdater {

    // Injected
    private final DateProvider currentDateProvider;

    StatUpdater(DateProvider currentDateProvider) {
        this.currentDateProvider = currentDateProvider;
    }

    void updateOnNewChange(WallpaperStat stat) {
        Date newChangeDate = currentDateProvider.get();
        DateUtil dateUtil = new DateUtil(currentDateProvider);
        if (dateUtil.isToday(stat.getLastChangeDate())) {
            stat.increase();
        } else {
            stat.reset(newChangeDate);
        }
    }
}
