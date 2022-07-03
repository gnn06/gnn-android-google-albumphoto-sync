package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class ScheduleFromFreqTest {

    private WallpaperSchedulerWithPermission wlppSchedulerMock;
    private PresenterFrequencies presenter;

    @Before
    public void setUp() throws Exception {
        wlppSchedulerMock = mock(WallpaperSchedulerWithPermission.class);
        presenter = mock(PresenterFrequencies.class);
    }

    @Test
    public void verif_all_frequencies() {
        // given scheduler not scheduled
        ScheduleFromFreq schedulerFromFreq = new ScheduleFromFreq(presenter, wlppSchedulerMock);
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(-12);
        when(presenter.getFrequencySyncMinute()).thenReturn(30);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(-12);
        when(presenter.getFrequencyUpdatePhotosHour()).thenReturn(60);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15, 30, 60);
    }

    @Test
    public void cancel() {
    }
}