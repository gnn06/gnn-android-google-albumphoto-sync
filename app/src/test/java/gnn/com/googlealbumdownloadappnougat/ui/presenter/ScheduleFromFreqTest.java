package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.Frequency;
import gnn.com.googlealbumdownloadappnougat.service.SyncScheduler;

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

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15);
        verify(syncSchedulerMock).cancel();
    }

    @Test
    public void wallpaper_never_never() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.NEVER);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15);
        verify(syncSchedulerMock).cancel();
    }

    @Test
    public void wallpaper_sync_never() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(30);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.NEVER);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15);
        verify(syncSchedulerMock).cancel();
    }

    @Test
    public void wallpaper_sync_always() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(30);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.ALWAYS);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15);
        verify(syncSchedulerMock).cancel();
    }

    @Test
    public void wallpaper_never_update() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(30);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15);
        verify(syncSchedulerMock).cancel();
    }

    @Test
    public void wallpaper_never_always() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.ALWAYS);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15);
        verify(syncSchedulerMock).cancel();
    }

    @Test
    public void wallpaper_always_update() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.ALWAYS);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(30);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15);
        verify(syncSchedulerMock).cancel();
    }

    @Test
    public void wallpaper_always_never() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.ALWAYS);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.NEVER);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15);
        verify(syncSchedulerMock).cancel();
    }

    @Test
    public void wallpaper_always_always() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(15);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.ALWAYS);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.ALWAYS);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(15);
        verify(syncSchedulerMock).cancel();
    }

    @Test
    public void never_sync_update() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencySyncHour()).thenReturn(15);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(30);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).cancel();
        verify(syncSchedulerMock).schedule(15);
    }

    @Test
    public void never_sync_never() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencySyncHour()).thenReturn(15);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.NEVER);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).cancel();
        verify(syncSchedulerMock).schedule(15);
    }

    @Test
    public void never_sync_always() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencySyncHour()).thenReturn(15);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.ALWAYS);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).cancel();
        verify(syncSchedulerMock).schedule(15);
    }

    @Test
    public void never_never_update() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(15);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock, never()).schedule(-1);
        verify(syncSchedulerMock, never()).schedule(anyInt());
        verify(wlppSchedulerMock).cancel();
        verify(syncSchedulerMock).cancel();
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
        verify(wlppSchedulerMock, never()).schedule(-1);
        verify(syncSchedulerMock, never()).schedule(anyInt());
        verify(wlppSchedulerMock).cancel();
        verify(syncSchedulerMock).cancel();
    }

    @Test
    public void never_never_always() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencySyncHour()).thenReturn(Frequency.NEVER);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.ALWAYS);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock, never()).schedule(-1);
        verify(syncSchedulerMock, never()).schedule(anyInt());
        verify(wlppSchedulerMock).cancel();
        verify(syncSchedulerMock).cancel();
    }

    @Test
    public void always_sync_never() {
        // given scheduler not scheduled
        when(presenter.getFrequencyWallpaper()).thenReturn(Frequency.ALWAYS);
        when(presenter.getFrequencySyncHour()).thenReturn(15);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(Frequency.NEVER);

        // when set one freq
        schedulerFromFreq.scheduleOrCancel();

        // then
        verify(wlppSchedulerMock).schedule(0);
        verify(syncSchedulerMock, never()).schedule(anyInt());
        verify(wlppSchedulerMock,never()).cancel();
        verify(syncSchedulerMock).cancel();
    }

}