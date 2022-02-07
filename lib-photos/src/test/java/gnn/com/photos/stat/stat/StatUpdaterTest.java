package gnn.com.photos.stat.stat;

import static org.junit.Assert.*;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import gnn.com.util.DateProvider;

public class StatUpdaterTest {

    private DateProvider currentDateProvider;

    @Before
    public void setUp() throws Exception {
        currentDateProvider = new DateProvider() {
            @Override
            public Date get() {
                return new Date(2022, 5, 12, 18, 0);
            }
        };
    }

    @Test
    public void updateOnChange_on_same_day() {
        WallpaperStat stat = new WallpaperStat(12, 14, new Date(2022, 5, 12, 17, 32));
        stat.updateOnNewChange(currentDateProvider);
        assertThat(stat.getChangeOnDate(), Is.is(13));
    }

    @Test
    public void updateOnChange_on_other_day() {
        WallpaperStat stat = new WallpaperStat(12, 14, new Date(2022, 5, 10, 17, 32));
        stat.updateOnNewChange(currentDateProvider);
        assertThat(stat.getChangeOnDate(), Is.is(1));
    }
}