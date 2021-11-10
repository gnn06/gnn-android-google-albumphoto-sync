package gnn.com.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ExpirationChecker {

    private Date cacheDate;
    private int maxAgeMinute;

    /**
     *
     * @param cacheDate
     * @param maxAgeMinute in minutes
     */
    public ExpirationChecker(Date cacheDate, int maxAgeMinute) {
        this.cacheDate = cacheDate;
        this.maxAgeMinute = maxAgeMinute;
    }

    public boolean isExpired() {

        // |             |
        // Cache         Cache + delay
        //                   |
        //                   Current => expired
        //           |
        //           Current => not expired

        if (maxAgeMinute == 0) {
            return true;
        } else if (maxAgeMinute == Integer.MAX_VALUE) {
            return false;
        } else {
            Calendar cacheExpirationDate = new GregorianCalendar();
            cacheExpirationDate.setTime(this.cacheDate);
            cacheExpirationDate.add(Calendar.MINUTE, maxAgeMinute);
            Calendar currentDate = new GregorianCalendar();
            boolean expired = cacheExpirationDate.before(currentDate);
            return expired;
        }
    }
}
