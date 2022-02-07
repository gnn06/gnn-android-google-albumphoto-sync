package gnn.com.photos.stat.stat;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import java.util.Date;

import gnn.com.util.DateProvider;

public class WallpaperStatTest {

    @Test
    public void update_dayBefore_with_onDate() {
        WallpaperStat stat = new WallpaperStat(12, 14, new Date(74, 2, 9));
        DateProvider dateProviderMock = mock(DateProvider.class);
        when(dateProviderMock.get()).thenReturn(new Date(74, 2, 10));

        stat.updateOnNewChange(dateProviderMock);

        assertThat(stat.getChangeOnDate(), is(1));
        assertThat(stat.getChangeOnDayBefore(), is(12));
    }
}