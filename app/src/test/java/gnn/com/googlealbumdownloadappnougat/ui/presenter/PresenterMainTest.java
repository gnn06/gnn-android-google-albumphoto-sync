package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.content.ContextWrapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.Require;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PresenterMainTest {

    @Test
    public void onSyncClick_NoAlbum() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        PresenterMain presenter = spy(new PresenterMain(activity, activity));
        presenter.setAlbum(null);
        IView view = spy(IView.class);
        presenter.onSyncClick();
        verify(view, Mockito.never()).alertNoAlbum();
    }

    @Test
    public void onSyncClick_AllGrantedPermission() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        when(activity.getApplicationContext()).thenReturn(mock(ContextWrapper.class));
        PresenterMain presenter = spy(new PresenterMain(activity, activity));
        presenter.setAlbum("test");
        AuthManager authMock = Mockito.mock(AuthManager.class);
        Mockito.when(authMock.isSignIn()).thenReturn(true);
        Mockito.when(authMock.hasWritePermission()).thenReturn(true);
        presenter.setAuth(authMock);
        presenter.onSyncClick();
    }

    @Test
    public void onSyncClick_MissingPermission() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        when(activity.getApplicationContext()).thenReturn(mock(ContextWrapper.class));
        PresenterMain presenter = spy(new PresenterMain(activity, activity));
        presenter.setAlbum("test");
        AuthManager authMock = Mockito.mock(AuthManager.class);
        Mockito.when(authMock.isSignIn()).thenReturn(false);
        presenter.setAuth(authMock);
        presenter.onSyncClick();
        assertNotNull(presenter.getPendingRequirement());
    }

    @Test
    public void onShowAlbumList_NoCache_NoPermission () {
        MainActivity activity = Mockito.mock(MainActivity.class);
        when(activity.getApplicationContext()).thenReturn(mock(ContextWrapper.class));
        PresenterMain presenter = spy(new PresenterMain(activity, activity));
        AuthManager authMock = Mockito.mock(AuthManager.class);
        Mockito.when(authMock.isSignIn()).thenReturn(false);
        presenter.setAuth(authMock);

        presenter.onShowAlbumList();

        assertNotNull(presenter.getPendingRequirement());
    }

    @Test
    public void startRequirement () {
        // given
        MainActivity view = Mockito.mock(MainActivity.class);
        PresenterMain presenter = new PresenterMain(view, view);
        Require require = Mockito.mock(Require.class);

        // when
        presenter.startRequirement(require);

        // then
        // verify that the require is stored and started
        assertEquals(require, presenter.getPendingRequirement());
        verify(require).exec();

    }

    @Test
    public void test_resetCache() {
        // given
        MainActivity view = Mockito.mock(MainActivity.class);
        when(view.getApplicationContext()).thenReturn(mock(ContextWrapper.class));
        PresenterMain presenter = new PresenterMain(view, view);
        // when
        presenter.onResetCache();
        // then
    }


    @Test
    public void init() {
        // given a mocked Presenter
        IView view = mock(IView.class);
        MainActivity activity = mock(MainActivity.class);

        PresenterMain presenter = spy(new PresenterMain(view, activity));

        SynchronizerAndroid synchronizer = mock(SynchronizerAndroid.class);
        doReturn(synchronizer).when(presenter).getSync();

        // when call init
        presenter.init();

        // then have called update_UI and updateUI_lastSyncTime
        verify(view, times(1)).updateUI_User();
        verify(view, times(1)).updateUI_lastSyncTime(null);
    }

    @Test
    public void getQuantity_empty() {
        IView view = mock(MainActivity.class);
        MainActivity activity = mock(MainActivity.class);
        PresenterMain presenter = new PresenterMain(view, activity);

        when(view.getQuantity()).thenReturn("");

        int actual = presenter.getQuantity();

        assertEquals(-1, actual);
    }

    @Test
    public void getQuantity_notEmpty() {
        IView view = mock(MainActivity.class);
        MainActivity activity = mock(MainActivity.class);
        PresenterMain presenter = new PresenterMain(view, activity);

        when(view.getQuantity()).thenReturn("5");

        int actual = presenter.getQuantity();

        assertEquals(5, actual);
    }

    @Test
    public void setQuantity_notEmpty() {
        IView view = mock(MainActivity.class);
        MainActivity activity = mock(MainActivity.class);
        PresenterMain presenter = new PresenterMain(view, activity);

        presenter.setQuantity(5);

        verify(view).setQuantity("5");
    }

    @Test
    public void setQuantity_empty() {
        IView view = mock(MainActivity.class);
        MainActivity activity = mock(MainActivity.class);
        PresenterMain presenter = new PresenterMain(view, activity);

        presenter.setQuantity(-1);

        verify(view).setQuantity("");
    }
}