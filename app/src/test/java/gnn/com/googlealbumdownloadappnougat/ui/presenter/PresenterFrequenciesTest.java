package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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

public class PresenterFrequenciesTest {

    private IViewFrequencies view;
    private MainActivity activity;
    private Context context;
    private UserModel usermodel;
    private PersistPrefMain persist;
    private WallpaperScheduler scheduler;
    private AuthManager authManager;
    private ApplicationContext applicationContext;
    private ScheduleTask scheduleTask;

    @Before
    public void setUp() throws Exception {
        view = spy(IViewFrequencies.class);
        when(view.getFrequencySync()).thenReturn("60");
        when(view.getFrequencyUpdatePhotos()).thenReturn("60");
        activity = null;
        context = null;
        usermodel = null;
        this.persist = mock(PersistPrefMain.class);
        this.scheduler = mock(WallpaperScheduler.class);
        this.authManager = mock(AuthManager.class);
        this.applicationContext = mock(ApplicationContext.class);
        this.scheduleTask = mock(ScheduleTask.class);
    }

    @Test
    public void getFrequencyUpdatePhotosHour() {
        PresenterFrequencies presenter = mock(PresenterFrequencies.class);
        when(presenter.getFrequencyUpdatePhotos()).thenReturn(10);
        when(presenter.getFrequencyUpdatePhotosHour()).thenCallRealMethod();
        int result = presenter.getFrequencyUpdatePhotosHour();
        assertThat(result, is(10 * 24));
    }

    @Test
    public void defaultValue_ui_setted() {
        // given
        SyncData syncData = new SyncData();
        syncData.setFrequencyWallpaper(60);
        when(persist.getData()).thenReturn(syncData);
        PresenterFrequencies presenter = new PresenterFrequencies(view, context, activity, usermodel,
                persist, scheduler, scheduleTask);
        doCallRealMethod().when(persist).restoreFrequencies(presenter);
        // when
        presenter.onAppStart();
        // then
        verify(view).setFrequencyWallpaper(60);
    }

    @Test
    public void defaultValue_schedule_ok() {
        // given
        SyncData syncData = new SyncData();
        syncData.setFrequencyWallpaper(60);
        when(persist.getData()).thenReturn(syncData);
        PresenterFrequencies presenter = new PresenterFrequencies(view, context, activity, usermodel,
                persist, scheduler, scheduleTask);
        doCallRealMethod().when(persist).restoreFrequencies(presenter);
        // when
        presenter.onAppStart();
        presenter.onSwitchWallpaper(true);
        // then
        verify(scheduleTask).schedule(anyBoolean(), eq(60L), anyInt(), anyLong());
    }
}