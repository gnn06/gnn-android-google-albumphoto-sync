package gnn.com.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtil {

    // Injected
    final private DateProvider currentDateProvider;

    public DateUtil(DateProvider currentDateProvider) {
        this.currentDateProvider = currentDateProvider;
    }

    boolean isSameDay(Date date, Date otherDate) {
        Instant truncDate      = date.toInstant().truncatedTo(ChronoUnit.DAYS);
        Instant truncOtherDate = otherDate.toInstant().truncatedTo(ChronoUnit.DAYS);
        return truncDate.equals(truncOtherDate);
    }

    public boolean isToday(Date date) {
        return isSameDay(date, currentDateProvider.get());
    }

    boolean isYesterday(Date date) {
        Instant yesterday = currentDateProvider.get().toInstant().minus(1, ChronoUnit.DAYS);
        return isSameDay(date, Date.from(yesterday));
    }
}
