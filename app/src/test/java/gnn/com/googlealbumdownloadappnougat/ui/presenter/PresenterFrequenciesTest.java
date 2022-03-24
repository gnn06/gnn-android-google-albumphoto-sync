package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

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

    @Before
    public void setUp() throws Exception {
        view = spy(IViewFrequencies.class);
        activity = null;
        context = null;
        usermodel = null;
        this.persist = mock(PersistPrefMain.class);
        this.scheduler = mock(WallpaperScheduler.class);
        this.authManager = mock(AuthManager.class);
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
    public void defaultValue() {
        // given
        SyncData syncData = new SyncData();
        syncData.setFrequencyWallpaper(60);
        when(persist.getData()).thenReturn(syncData);
        PresenterFrequencies presenter = new PresenterFrequencies(view, context, activity, usermodel,
                persist, scheduler, authManager);
        doCallRealMethod().when(persist).restoreFrequencies(presenter);
        // when
        presenter.onAppStart();
        // then
        verify(view).setFrequencyWallpaper(60);
    }
}