package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.Frequency;
import gnn.com.googlealbumdownloadappnougat.service.SyncScheduler;
import gnn.com.photos.service.Cache;
import gnn.com.photos.sync.SynchronizerDelayed;

public class ScheduleFromFreqTest {

    private WallpaperSchedulerWithPermission wlppSchedulerMock;
    private SyncScheduler syncSchedulerMock;
    private PresenterFrequencies presenter;
    private ScheduleFromFreq schedulerFromFreq;

    @Before
    public void setUp() throws Exception {
        wlppSchedulerMock = mock(WallpaperSchedulerWithPermission.class);
        syncSchedulerMock = mock(SyncScheduler.class);
        presenter = mock(PresenterFrequencies.class);
        schedulerFromFreq = new ScheduleFromFreq(presenter, wlppSchedulerMock, syncSchedulerMock);
    }

    @Test
    public void all_frequencies() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(30);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(60);

        when(presenter.getFrequencySyncMinute()).thenCallRealMethod();
        when(presenter.getFrequencyUpdatePhotosHour()).thenCallRealMethod();

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15, 30 * 60, 60 * 24);
    }

    @Test
    public void wallpaper_never_never() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.NEVER);

        when(presenter.getFrequencySyncMinute()).thenCallRealMethod();
        when(presenter.getFrequencyUpdatePhotosHour()).thenCallRealMethod();

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15, SynchronizerDelayed.DELAY_NEVER_SYNC, Cache.DELAY_NEVER_EXPIRE);
    }

    @Test
    public void wallpaper_sync_never() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(30);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.NEVER);

        when(presenter.getFrequencySyncMinute()).thenCallRealMethod();
        when(presenter.getFrequencyUpdatePhotosHour()).thenCallRealMethod();

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15, 30 * 60, Cache.DELAY_NEVER_EXPIRE);
    }

    @Test
    public void wallpaper_sync_always() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(30);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.ALWAYS);

        when(presenter.getFrequencySyncMinute()).thenCallRealMethod();
        when(presenter.getFrequencyUpdatePhotosHour()).thenCallRealMethod();

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15, 30 * 60, Cache.DELAY_ALWAYS_EXPIRE);
    }

    @Test
    public void wallpaper_never_update() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(30);

        when(presenter.getFrequencySyncMinute()).thenCallRealMethod();
        when(presenter.getFrequencyUpdatePhotosHour()).thenCallRealMethod();

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15, SynchronizerDelayed.DELAY_NEVER_SYNC, 30*24);
    }

    @Test
    public void wallpaper_never_always() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.ALWAYS);

        when(presenter.getFrequencySyncMinute()).thenCallRealMethod();
        when(presenter.getFrequencyUpdatePhotosHour()).thenCallRealMethod();

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15, SynchronizerDelayed.DELAY_NEVER_SYNC, Cache.DELAY_ALWAYS_EXPIRE);
    }

    @Test
    public void wallpaper_always_update() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.ALWAYS);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(30);

        when(presenter.getFrequencySyncMinute()).thenCallRealMethod();
        when(presenter.getFrequencyUpdatePhotosHour()).thenCallRealMethod();

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15, SynchronizerDelayed.DELAY_ALWAYS_SYNC, 30*24);
    }

    @Test
    public void wallpaper_always_never() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.ALWAYS);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.NEVER);

        when(presenter.getFrequencySyncMinute()).thenCallRealMethod();
        when(presenter.getFrequencyUpdatePhotosHour()).thenCallRealMethod();

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15, SynchronizerDelayed.DELAY_ALWAYS_SYNC, Cache.DELAY_NEVER_EXPIRE);
    }

    @Test
    public void wallpaper_always_always() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.ALWAYS);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.ALWAYS);

        when(presenter.getFrequencySyncMinute()).thenCallRealMethod();
        when(presenter.getFrequencyUpdatePhotosHour()).thenCallRealMethod();

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15, SynchronizerDelayed.DELAY_ALWAYS_SYNC, Cache.DELAY_ALWAYS_EXPIRE);
    }

    @Test
    public void never_sync_update() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencySyncHour()).thenReturn(15);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(30);

        when(presenter.getFrequencySyncMinute()).thenCallRealMethod();
        when(presenter.getFrequencyUpdatePhotosHour()).thenCallRealMethod();

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(-1, 15*60, 30*24);
    }

    @Test
    public void never_sync_never() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencySyncHour()).thenReturn(15);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.NEVER);

        when(presenter.getFrequencySyncMinute()).thenCallRealMethod();
        when(presenter.getFrequencyUpdatePhotosHour()).thenCallRealMethod();

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(-1, 15*60, Cache.DELAY_NEVER_EXPIRE);
    }

    @Test
    public void never_sync_always() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencySyncHour()).thenReturn(15);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.ALWAYS);

        when(presenter.getFrequencySyncMinute()).thenCallRealMethod();
        when(presenter.getFrequencyUpdatePhotosHour()).thenCallRealMethod();

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(-1, 15*60, Cache.DELAY_ALWAYS_EXPIRE);
    }

    @Test
    public void never_never_update() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(15);

        when(presenter.getFrequencySyncMinute()).thenCallRealMethod();
        when(presenter.getFrequencyUpdatePhotosHour()).thenCallRealMethod();

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(-1, SynchronizerDelayed.DELAY_NEVER_SYNC, 15*24);
    }

    @Test
    public void never_never_never() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.NEVER);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).cancel();
    }

    @Test
    public void never_never_always() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.ALWAYS);

        when(presenter.getFrequencySyncMinute()).thenCallRealMethod();
        when(presenter.getFrequencyUpdatePhotosHour()).thenCallRealMethod();

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(-1, SynchronizerDelayed.DELAY_NEVER_SYNC, Cache.DELAY_ALWAYS_EXPIRE);
    }

    @Test
    public void always_sync_never() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(Frequency.ALWAYS);
        when(presenter.getFrequencySyncHour()).thenReturn(15);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.NEVER);

        when(presenter.getFrequencySyncMinute()).thenCallRealMethod();
        when(presenter.getFrequencyUpdatePhotosHour()).thenCallRealMethod();

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(0, 15*60, Cache.DELAY_NEVER_EXPIRE);
    }

}