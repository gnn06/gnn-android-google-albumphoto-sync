package gnn.com.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ExpirationChecker {

    private Date cacheDate;
    private int maxAge;

    /**
     *
     * @param cacheDate
     * @param maxAge in minutes
     */
    public ExpirationChecker(Date cacheDate, int maxAge) {
        this.cacheDate = cacheDate;
        this.maxAge = maxAge;
    }

    public boolean isExpired() {

        // |             |
        // Cache         Cache + delay
        //                   |
        //                   Current => expired
        //           |
        //           Current => not expired

        Calendar cacheExpirationDate = new GregorianCalendar();
        cacheExpirationDate.setTime(this.cacheDate);
        cacheExpirationDate.add(Calendar.MINUTE, maxAge);
        Calendar currentDate = new GregorianCalendar();
        boolean expired = cacheExpirationDate.before(currentDate);
        return expired;
    }
}
