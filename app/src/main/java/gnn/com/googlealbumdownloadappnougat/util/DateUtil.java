package gnn.com.googlealbumdownloadappnougat.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

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

    boolean isSameDay() {
        return false;
    }
}
