package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.util.DateProvider;

class StatUpdater {

    // Injected
    private final DateProvider currentDateProvider;

    StatUpdater(DateProvider currentDateProvider) {
        this.currentDateProvider = currentDateProvider;
    }

    void updateOnNewChange(WallpaperStat stat) {
        Date newChangeDate = currentDateProvider.get();
        Instant instantNewChange = newChangeDate.toInstant().truncatedTo(ChronoUnit.DAYS);
        Instant instantLastChange = stat.getLastChangeDate().toInstant().truncatedTo(ChronoUnit.DAYS);
        if (instantNewChange.equals(instantLastChange)) {
            stat.increase(newChangeDate);
        } else {
            stat.reset(newChangeDate);
        }
    }
}
