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
    private SyncData defaultValue;

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
        defaultValue = new SyncData();
        defaultValue.setFrequencyWallpaper(60);
        defaultValue.setFrequencySync(168);
        when(persist.getData()).thenReturn(defaultValue);
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
        PresenterFrequencies presenter = new PresenterFrequencies(view, context, activity, usermodel,
                persist, scheduler, scheduleTask);
        doCallRealMethod().when(persist).restoreFrequencies(presenter);
        // when
        presenter.onAppStart();
        // then
        verify(view).setFrequencyWallpaper(60);
        verify(view).setFrequencySync(168);
    }

    @Test
    public void defaultValue_schedule_ok() {
        // given
        PresenterFrequencies presenter = new PresenterFrequencies(view, context, activity, usermodel,
                persist, scheduler, scheduleTask);
        doCallRealMethod().when(persist).restoreFrequencies(presenter);
        // when
        presenter.onAppStart();
        presenter.onSwitchWallpaper(true);
        // then
        verify(scheduleTask).schedule(anyBoolean(), eq(60L), eq(168 * 60), anyLong());
    }

    @Test
    public void defaultValue_change_ui_ok() {
        // given
        PresenterFrequencies presenter = new PresenterFrequencies(view, context, activity, usermodel,
                persist, scheduler, scheduleTask);
        doCallRealMethod().when(persist).restoreFrequencies(presenter);
        presenter.onAppStart();
        verify(view).setFrequencyWallpaper(60);
        // when
        presenter.setFrequencyWallpaper(120);
        presenter.setFrequencySyncHour(720);
        // then
        verify(view).setFrequencyWallpaper(120);
        verify(view).setFrequencySync(720);
    }

    @Test
    public void valueChange_toggle_OK() {
        PresenterFrequencies presenter = new PresenterFrequencies(view, context, activity, usermodel,
                persist, scheduler, scheduleTask);
        doCallRealMethod().when(persist).restoreFrequencies(presenter);
        presenter.onAppStart();
        presenter.setFrequencyWallpaper(120);
        presenter.setFrequencySyncHour(720);
        // when
        presenter.onSwitchWallpaper(true);
        // then
        verify(scheduleTask).schedule(anyBoolean(), eq(120L), anyInt(), anyLong());
        verify(scheduleTask).schedule(anyBoolean(), eq(120L), eq(720*60), anyLong());
    }

    @Test
    public void valueChange_presist_OK() {
        PresenterFrequencies presenter = new PresenterFrequencies(view, context, activity, usermodel,
                persist, scheduler, scheduleTask);
        doCallRealMethod().when(persist).restoreFrequencies(presenter);
        presenter.setFrequencyWallpaper(120);
        presenter.setFrequencySyncHour(720);
        // when
        presenter.onAppStop();
        // then
        verify(persist).saveFrequencies(eq(120), eq(720), anyInt());
    }
}