package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.ContextWrapper;

import org.junit.Before;
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
public class PresenterHomeTest {

    private PersistPrefMain persistPref;

    @Before
    public void setUp() throws Exception {
        persistPref = mock(PersistPrefMain.class);
    }

    @Test
    public void onSyncClick_NoAlbum() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        PresenterHome presenter = spy(new PresenterHome(activity, activity, null));
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
        PresenterHome presenter = spy(new PresenterHome(activity, activity, null));
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
        PresenterHome presenter = spy(new PresenterHome(activity, activity, permissionHandler));
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
        PresenterHome presenter = spy(new PresenterHome(activity, activity, permissionHandler));
        AuthManager authMock = Mockito.mock(AuthManager.class);
        presenter.setAuth(authMock);
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

        PresenterHome presenter = new PresenterHome(view, view, null);
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

        PresenterHome presenter = spy(new PresenterHome(view, activity, null));

        SynchronizerAndroid synchronizer = mock(SynchronizerAndroid.class);
        doReturn(synchronizer).when(presenter).getSync();

        // when call init
        presenter.onAppStart();

        // then have called update_UI and updateUI_lastSyncTime
        verify(view, times(1)).updateUI_User();
        verify(view, times(1)).updateUI_lastSyncTime(null, null, null);
    }
}