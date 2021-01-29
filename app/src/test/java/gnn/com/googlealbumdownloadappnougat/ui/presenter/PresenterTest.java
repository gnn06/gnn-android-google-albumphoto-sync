package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.content.ContextWrapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.Require;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PresenterTest {

    @Test
    public void onSyncClick_NoAlbum() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        when(activity.getApplicationContext()).thenReturn(mock(ContextWrapper.class));
        Presenter presenter = spy(new Presenter(activity, activity));
        Whitebox.setInternalState(presenter, "album", null);
        IView view = spy(IView.class);
        presenter.onSyncClick();
        verify(view, Mockito.never()).alertNoAlbum();
    }

    @Test
    public void onSyncClick_AllGrantedPermission() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        when(activity.getApplicationContext()).thenReturn(mock(ContextWrapper.class));
        Presenter presenter = spy(new Presenter(activity, activity));
        Whitebox.setInternalState(presenter, "album", "test");
        AuthManager authMock = Mockito.mock(AuthManager.class);
        Mockito.when(authMock.isSignIn()).thenReturn(true);
        Mockito.when(authMock.hasWritePermission()).thenReturn(true);
        presenter.setAuth(authMock);
        presenter.onSyncClick();
//        assertNull(null, Whitebox.getInternalState(presenter, "pendingRequirement"));
    }

    @Test
    public void onSyncClick_MissingPermission() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        when(activity.getApplicationContext()).thenReturn(mock(ContextWrapper.class));
        Presenter presenter = spy(new Presenter(activity, activity));
        Whitebox.setInternalState(presenter, "album", "test");
        AuthManager authMock = Mockito.mock(AuthManager.class);
        Mockito.when(authMock.isSignIn()).thenReturn(false);
        Mockito.when(authMock.hasWritePermission()).thenReturn(false);
        presenter.setAuth(authMock);
        presenter.onSyncClick();
        assertNotNull(Whitebox.getInternalState(presenter, "pendingRequirement"));
    }

    @Test
    public void onShowAlbumList_NoCache_NoPermission () {
        MainActivity activity = Mockito.mock(MainActivity.class);
        when(activity.getApplicationContext()).thenReturn(mock(ContextWrapper.class));
        Presenter presenter = spy(new Presenter(activity, activity));
        AuthManager authMock = Mockito.mock(AuthManager.class);
        Mockito.when(authMock.isSignIn()).thenReturn(false);
        Mockito.when(authMock.hasWritePermission()).thenReturn(false);
        presenter.setAuth(authMock);

        presenter.onShowAlbumList();

        assertNotNull(Whitebox.getInternalState(presenter, "pendingRequirement"));
    }

    @Test
    public void startRequirement () {
        // given
        MainActivity view = Mockito.mock(MainActivity.class);
        when(view.getApplicationContext()).thenReturn(mock(ContextWrapper.class));
        Presenter presenter = new Presenter(view, view);
        Require require = Mockito.mock(Require.class);

        // when
        presenter.startRequirement(require);

        // then
        // verify that the require is stored and started
        assertEquals(require, Whitebox.getInternalState(presenter, "pendingRequirement"));
        verify(require).exec();

    }

    @Test
    public void test_resetCache() {
        // given
        MainActivity view = Mockito.mock(MainActivity.class);
        when(view.getApplicationContext()).thenReturn(mock(ContextWrapper.class));
        Presenter presenter = new Presenter(view, view);
        // when
        presenter.onResetCache();
        // then
    }


    @Test
    public void init() {
        // given a mocked Presenter
        IView view = mock(IView.class);
        MainActivity activity = mock(MainActivity.class);

        Presenter presenter = spy(new Presenter(view, activity));

        SynchronizerAndroid synchronizer = mock(SynchronizerAndroid.class);
        doReturn(synchronizer).when(presenter).getSync();

        // when call init
        presenter.init();

        // then have called update_UI and updateUI_lastSyncTime
        verify(view, times(1)).updateUI_User();
        verify(view, times(1)).updateUI_lastSyncTime(anyString());
    }
}