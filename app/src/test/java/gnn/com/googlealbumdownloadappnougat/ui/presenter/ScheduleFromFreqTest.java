package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

public class ScheduleFromFreqTest {

    private WallpaperSchedulerWithPermission wlppSchedulerMock;
    private PresenterFrequencies presenter;
    private ScheduleFromFreq schedulerFromFreq;

    @Before
    public void setUp() throws Exception {
        wlppSchedulerMock = mock(WallpaperSchedulerWithPermission.class);
        presenter = mock(PresenterFrequencies.class);
        schedulerFromFreq = new ScheduleFromFreq(presenter, wlppSchedulerMock);
    }

    @Test
    public void verif_all_frequencies() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(30);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(60);

        when(presenter.getFrequencySyncMinute()).thenReturn(-30);
        when(presenter.getFrequencyUpdatePhotosHour()).thenReturn(-60);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15, -30, -60);
    }

    @Test
    public void verif_any_frequencies_wallpaper() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(-12);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(-12);

        when(presenter.getFrequencySyncMinute()).thenReturn(-30);
        when(presenter.getFrequencyUpdatePhotosHour()).thenReturn(-60);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15, -30, -60);
    }

    @Test
    public void verif_any_frequencies_sync() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(-12);
        when(presenter.getFrequencySyncHour()).thenReturn(-24);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(15);

        when(presenter.getFrequencySyncMinute()).thenReturn(-30);
        when(presenter.getFrequencyUpdatePhotosHour()).thenReturn(-60);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(-12, -30, -60);
    }

    @Test
    public void verif_any_frequencies_update() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(0);
        when(presenter.getFrequencySyncHour()).thenReturn(15);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(-12);

        when(presenter.getFrequencySyncMinute()).thenReturn(-30);
        when(presenter.getFrequencyUpdatePhotosHour()).thenReturn(-60);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(0, -30, -60);
    }

    @Test
    public void cancel() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(0);
        when(presenter.getFrequencySyncHour()).thenReturn(0);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(0);

        when(presenter.getFrequencySyncMinute()).thenReturn(34);
        when(presenter.getFrequencyUpdatePhotosHour()).thenReturn(34);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).cancel();
    }

}