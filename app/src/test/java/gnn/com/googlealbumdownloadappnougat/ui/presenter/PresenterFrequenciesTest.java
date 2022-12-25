package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.ui.view.IViewFrequencies;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;
import gnn.com.photos.service.Cache;
import gnn.com.photos.sync.SynchronizerDelayed;

public class PresenterFrequenciesTest {

    private IViewFrequencies view;
    private MainActivity activity;
    private Context context;
    private UserModel usermodel;
    private PersistPrefMain persist;
    private ScheduleFromFreq scheduleFromFreq;
    private WallpaperScheduler scheduler;
    private AuthManager authManager;
    private ApplicationContext applicationContext;
    private WallpaperSchedulerWithPermission scheduleWithPermission;
    private SyncData defaultValue;

    @Before
    public void setUp() throws Exception {
        view = mock(FragmentFrequencies.class);
        activity = null;
        context = null;
        usermodel = null;
        this.persist = mock(PersistPrefMain.class);
        this.scheduler = mock(WallpaperScheduler.class);
        this.authManager = mock(AuthManager.class);
        this.applicationContext = mock(ApplicationContext.class);
        this.scheduleWithPermission = mock(WallpaperSchedulerWithPermission.class);
        this.scheduleFromFreq = mock(ScheduleFromFreq.class);
        defaultValue = new SyncData();
        defaultValue.setFrequencyWallpaper(60);
        defaultValue.setFrequencySync(168);
        defaultValue.setFrequencyUpdatePhotos(720);
        when(persist.getData()).thenReturn(defaultValue);
    }

        @Test
    public void defaultValue_ui_setted() {
        // given
        PresenterFrequencies presenter = new PresenterFrequencies(view, context,
                persist, null);
        doCallRealMethod().when(persist).restoreFrequencies(presenter);
        // when
        presenter.onAppStart();
        // then
        verify(view).setFrequencyWallpaper(60);
        verify(view).setFrequencySync(168);
        verify(view).setFrequencyUpdate(720);
    }

    @Test
    public void defaultValue_change_ui_ok() {
        // given
        PresenterFrequencies presenter = new PresenterFrequencies(view, context,
                persist, scheduleFromFreq);
        doCallRealMethod().when(persist).restoreFrequencies(presenter);
        presenter.onAppStart();
        verify(view).setFrequencyWallpaper(60);
        // when
        presenter.setFrequencyWallpaperWithSchedule(120);
        presenter.setFrequencySyncWithSchedule(720);
        presenter.setFrequencyUpdateWithSchedule(168);
        // then
        verify(view).setFrequencyWallpaper(120);
        verify(view).setFrequencySync(720);
        verify(view).setFrequencySync(168);
    }

    @Test
    public void valueChange_persist_OK() {
        PresenterFrequencies presenter = new PresenterFrequencies(view, context,
                persist, scheduleFromFreq);
        doCallRealMethod().when(persist).restoreFrequencies(presenter);

        presenter.setFrequencyWallpaperWithSchedule(120);
        presenter.setFrequencySyncWithSchedule(720);
        presenter.setFrequencyUpdateWithSchedule(168);
        // when
        presenter.onAppStop();
        // then
        verify(persist, times(2)).saveFrequencies(eq(120), eq(720), eq(168));
    }
}