package gnn.com.googlealbumdownloadappnougat.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import gnn.com.photos.service.Cache;

class DateUtil {

    // Injected
    final private DateProvider currentDateProvider;

    DateUtil(DateProvider currentDateProvider) {
        this.currentDateProvider = currentDateProvider;
    }

    boolean isToday(Date date) {
        Instant truncCurrentDate = currentDateProvider.get().toInstant().truncatedTo(ChronoUnit.DAYS);
        Instant truncGivenDate = date.toInstant().truncatedTo(ChronoUnit.DAYS);
        return truncCurrentDate.equals(truncGivenDate);
    }

    boolean isYesterday(Date date) {
        Instant truncYesterdayDate = currentDateProvider.get().toInstant()
                .truncatedTo(ChronoUnit.DAYS)
                .minus(1, ChronoUnit.DAYS);
        Instant truncGivenDate = date.toInstant().truncatedTo(ChronoUnit.DAYS);
        return truncYesterdayDate.equals(truncGivenDate);
    }

    boolean isSameDay(Date date, Date otherDate) {
        Instant truncDate      = date.toInstant().truncatedTo(ChronoUnit.DAYS);
        Instant truncOtherDate = otherDate.toInstant().truncatedTo(ChronoUnit.DAYS);
        return truncDate.equals(truncOtherDate);
    }
}
