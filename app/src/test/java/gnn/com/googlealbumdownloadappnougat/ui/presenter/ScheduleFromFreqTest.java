package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

public class ScheduleFromFreqTest {

    private WallpaperSchedulerWithPermission wlppSchedulerMock;

    @Before
    public void setUp() throws Exception {
        wlppSchedulerMock = mock(WallpaperSchedulerWithPermission.class);
    }

    @Test
    public void schedule() {
        // given scheduler not scheduled
        ScheduleFromFreq schedulerFromFreq = new ScheduleFromFreq(-1, wlppSchedulerMock);

        // when set one freq
        schedulerFromFreq.setFrequencyWallpaper(15);

        // then
        verify(wlppSchedulerMock).schedule(15, -1, -1);
    }

    @Test
    public void cancel() {
    }
}