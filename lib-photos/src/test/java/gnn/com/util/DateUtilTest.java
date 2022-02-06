package gnn.com.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.hamcrest.core.Is.*;
import org.junit.Test;

import java.util.Date;

import gnn.com.util.DateProvider;
import gnn.com.util.DateUtil;

public class DateUtilTest {

    @Test
    public void is_today() {
        DateProvider dateProvider = mock(DateProvider.class);
        when(dateProvider.get()).thenReturn(new Date(2022, 3, 9));
        DateUtil dateUtil = new DateUtil(dateProvider);
        boolean result = dateUtil.isToday(new Date(2022, 3, 9));
        assertThat(result, is(true));
    }

    @Test
    public void is_today_after() {
        DateProvider dateProvider = mock(DateProvider.class);
        when(dateProvider.get()).thenReturn(new Date(2022, 3, 9));
        DateUtil dateUtil = new DateUtil(dateProvider);
        boolean result = dateUtil.isToday(new Date(2022, 3, 10));
        assertThat(result, is(false));
    }

    @Test
    public void is_today_before() {
        DateProvider dateProvider = mock(DateProvider.class);
        when(dateProvider.get()).thenReturn(new Date(2022, 3, 9));
        DateUtil dateUtil = new DateUtil(dateProvider);
        boolean result = dateUtil.isToday(new Date(2022, 3, 8));
        assertThat(result, is(false));
    }

    @Test
    public void is_yesterday() {
        DateProvider dateProvider = mock(DateProvider.class);
        when(dateProvider.get()).thenReturn(new Date(2022, 3, 9));
        DateUtil dateUtil = new DateUtil(dateProvider);
        boolean result = dateUtil.isYesterday(new Date(2022, 3, 8));
        assertThat(result, is(true));
    }

    @Test
    public void is_not_yesterday_today() {
        DateProvider dateProvider = mock(DateProvider.class);
        when(dateProvider.get()).thenReturn(new Date(2022, 3, 9));
        DateUtil dateUtil = new DateUtil(dateProvider);
        boolean result = dateUtil.isYesterday(new Date(2022, 3, 9));
        assertThat(result, is(false));
    }

    @Test
    public void is_not_yesterday_before() {
        DateProvider dateProvider = mock(DateProvider.class);
        when(dateProvider.get()).thenReturn(new Date(2022, 3, 9));
        DateUtil dateUtil = new DateUtil(dateProvider);
        boolean result = dateUtil.isYesterday(new Date(2022, 3, 10));
        assertThat(result, is(false));
    }

    @Test
    public void is_not_yesterday_after() {
        DateProvider dateProvider = mock(DateProvider.class);
        when(dateProvider.get()).thenReturn(new Date(2022, 3, 9));
        DateUtil dateUtil = new DateUtil(dateProvider);
        boolean result = dateUtil.isYesterday(new Date(2022, 3, 11));
        assertThat(result, is(false));
    }

    @Test
    public void is_sameDay_true() {
        DateUtil dateUtil = new DateUtil(null);
        boolean result = dateUtil
                .isSameDay(
                        new Date(2022, 3, 11),
                        new Date(2022, 3, 11));
        assertThat(result, is(true));
    }

    @Test
    public void is_sameDay_false() {
        DateUtil dateUtil = new DateUtil(null);
        boolean result = dateUtil
                .isSameDay(
                        new Date(2022, 3, 9),
                        new Date(2022, 3, 10));
        assertThat(result, is(false));
    }
}