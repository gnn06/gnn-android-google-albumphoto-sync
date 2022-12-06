package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.ui.FolderModel;
import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.PermissionHandler;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.ui.view.IViewHome;

@RunWith(MockitoJUnitRunner.class)
public class PresenterHomeTest {

    private PersistPrefMain persistPref;
    private FragmentHome view;
    private UserModel userModel;
    private FolderModel folderModel;
    private FragmentHome fragmentHome;
    private MainActivity activity;
    private PresenterHome presenter;

    @Before
    public void setUp() throws Exception {
        persistPref = mock(PersistPrefMain.class);
        view = mock(FragmentHome.class);
        userModel = mock(UserModel.class);
        folderModel = mock(FolderModel.class);
        fragmentHome = mock(FragmentHome.class);
        activity = Mockito.mock(MainActivity.class);
        presenter = spy(new PresenterHome(view, activity, fragmentHome, userModel, folderModel));
    }

    @Test
    public void onSyncClick_NoAlbum() {
        IViewHome view = spy(IViewHome.class);
        UserModel userModel = mock (UserModel.class);
        presenter.setAlbum(null);
        presenter.onButtonSyncOnce();
        verify(fragmentHome, Mockito.atLeastOnce()).alertNoAlbum();
    }

    @Test
    @Ignore
    public void onSyncClick_AllGrantedPermission() {
        when(activity.getApplicationContext()).thenReturn(mock(ContextWrapper.class));
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
        when(activity.getApplicationContext()).thenReturn(mock(ContextWrapper.class));
        PermissionHandler permissionHandler = new PermissionHandler();
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
        PermissionHandler permissionHandler = mock(PermissionHandler.class);
        when(activity.getPermissionHandler()).thenReturn(permissionHandler);
        AuthManager authMock = Mockito.mock(AuthManager.class);
        presenter.setAuth(authMock);
        when(activity.getFilesDir()).thenReturn(tmpFolder.newFolder());
        ApplicationContext.getInstance(activity);
        // when
        presenter.onShowAlbumList();
        // then
        verify(permissionHandler).startRequirement(any());
    }

    @Test
    public void onWorkerRunning() {
        // when
        presenter.onWorkerRunning(SyncStep.IN_PRORGESS);
        // then
        verify(presenter).setSyncResult(any(), eq(SyncStep.IN_PRORGESS));
    }

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    @Ignore
    public void init() {
        // given a mocked Presenter
        IViewHome view = mock(IViewHome.class);
        MainActivity activity = mock(MainActivity.class);

        PresenterHome presenter = spy(new PresenterHome(activity, fragmentHome));

        SynchronizerAndroid synchronizer = mock(SynchronizerAndroid.class);
        doReturn(synchronizer).when(presenter).getSync();

        // when call init
        presenter.onAppStart();

        // then have called update_UI and updateUI_lastSyncTime
        verify(view, times(1)).updateUI_User();
        verify(view, times(1)).updateUI_lastSyncTime(null, null, null);
    }
}