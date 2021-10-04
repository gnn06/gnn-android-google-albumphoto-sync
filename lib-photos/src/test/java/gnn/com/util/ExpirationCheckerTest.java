package gnn.com.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ExpirationCheckerTest {
    @Test
    public void not_expired() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MINUTE, -30);
        Date cacheDate = calendar.getTime();
        ExpirationChecker checker = new ExpirationChecker(cacheDate, 60);
        boolean expired = checker.isExpired();
        assertFalse(expired);
    }

    @Test
    public void expired() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MINUTE, -90);
        Date cacheDate = calendar.getTime();
        ExpirationChecker checker = new ExpirationChecker(cacheDate, 60);
        boolean expired = checker.isExpired();
        assertTrue(expired);
    }
}