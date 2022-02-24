package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.ContextWrapper;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.PermissionHandler;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;

@RunWith(MockitoJUnitRunner.class)
public class PresenterMainTest {

    @Test
    public void onSyncClick_NoAlbum() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        PresenterMain presenter = spy(new PresenterMain(activity, activity, null));
        presenter.setAlbum(null);
        IView view = spy(IView.class);
        presenter.onButtonSyncOnce();
        verify(view, Mockito.never()).alertNoAlbum();
    }

    @Test
    @Ignore
    public void onSyncClick_AllGrantedPermission() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        when(activity.getApplicationContext()).thenReturn(mock(ContextWrapper.class));
        PresenterMain presenter = spy(new PresenterMain(activity, activity, null));
        presenter.setAlbum("test");
        AuthManager authMock = Mockito.mock(AuthManager.class);
        Mockito.when(authMock.isSignIn()).thenReturn(true);
        Mockito.when(authMock.hasWritePermission()).thenReturn(true);
        presenter.setAuth(authMock);
        presenter.onButtonSyncOnce();
    }

    @Test
    @Ignore
    public void onSyncClick_store_requirement() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        when(activity.getApplicationContext()).thenReturn(mock(ContextWrapper.class));
        PermissionHandler permissionHandler = new PermissionHandler();
        PresenterMain presenter = spy(new PresenterMain(activity, activity, permissionHandler));
        presenter.setAlbum("test");
        AuthManager authMock = Mockito.mock(AuthManager.class);
        Mockito.when(authMock.isSignIn()).thenReturn(false);
        presenter.setAuth(authMock);
        // when
        presenter.onButtonSyncOnce();
        // then
        assertNotNull(permissionHandler.getPendingRequirement());
    }

    @Test
    public void onShowAlbumList_store_requirement () throws IOException {
        MainActivity activity = Mockito.mock(MainActivity.class);
        PermissionHandler permissionHandler = new PermissionHandler();
        PresenterMain presenter = spy(new PresenterMain(activity, activity, permissionHandler));
        AuthManager authMock = Mockito.mock(AuthManager.class);
        presenter.setAuth(authMock);
        when(activity.getFrequencyUpdatePhotos()).thenReturn("1");
        when(activity.getFilesDir()).thenReturn(tmpFolder.newFolder());
        ApplicationContext.getInstance(activity);
        // when
        presenter.onShowAlbumList();
        // then
        assertNotNull(permissionHandler.getPendingRequirement());
    }

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    public void test_resetCache() throws IOException {
        // given
        MainActivity view = Mockito.mock(MainActivity.class);
        when(view.getFrequencyUpdatePhotos()).thenReturn("1");

        PresenterMain presenter = new PresenterMain(view, view, null);
        // when
        presenter.onMenuResetCache();
        // then
    }


    @Test
    @Ignore
    public void init() {
        // given a mocked Presenter
        IView view = mock(IView.class);
        MainActivity activity = mock(MainActivity.class);

        PresenterMain presenter = spy(new PresenterMain(view, activity, null));

        SynchronizerAndroid synchronizer = mock(SynchronizerAndroid.class);
        doReturn(synchronizer).when(presenter).getSync();

        // when call init
        presenter.onAppStart();

        // then have called update_UI and updateUI_lastSyncTime
        verify(view, times(1)).updateUI_User();
        verify(view, times(1)).updateUI_lastSyncTime(null, null, null);
    }

    @Test
    public void getQuantity_empty() {
        IView view = mock(MainActivity.class);
        MainActivity activity = mock(MainActivity.class);
        PresenterMain presenter = new PresenterMain(view, activity, null);

        when(view.getQuantity()).thenReturn("");

        int actual = presenter.getQuantity();

        assertEquals(-1, actual);
    }

    @Test
    public void getQuantity_notEmpty() {
        IView view = mock(MainActivity.class);
        MainActivity activity = mock(MainActivity.class);
        PresenterMain presenter = new PresenterMain(view, activity, null);

        when(view.getQuantity()).thenReturn("5");

        int actual = presenter.getQuantity();

        assertEquals(5, actual);
    }

    @Test
    public void setQuantity_notEmpty() {
        IView view = mock(MainActivity.class);
        MainActivity activity = mock(MainActivity.class);
        PresenterMain presenter = new PresenterMain(view, activity, null);

        presenter.setQuantity(5);

        verify(view).setQuantity("5");
    }

    @Test
    public void setQuantity_empty() {
        IView view = mock(MainActivity.class);
        MainActivity activity = mock(MainActivity.class);
        PresenterMain presenter = new PresenterMain(view, activity, null);

        presenter.setQuantity(-1);

        verify(view).setQuantity("");
    }
}