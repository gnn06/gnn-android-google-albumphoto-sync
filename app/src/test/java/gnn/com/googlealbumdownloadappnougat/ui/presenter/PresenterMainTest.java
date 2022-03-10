package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.ui.FolderModel;
import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.PermissionHandler;
import gnn.com.googlealbumdownloadappnougat.ui.view.IViewHome;

public class PresenterMainTest {

    private IViewHome view;
    private UserModel userModel;
    private FolderModel folderModel;
    private AuthManager auth;
    private PermissionHandler permissionHandler;
    private PresenterHome presenterHome;
    private MainActivity activity;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        this.view = mock(FragmentHome.class);
        userModel = mock(UserModel.class);
        folderModel = mock(FolderModel.class);
        auth = mock(AuthManager.class);
        permissionHandler = mock(PermissionHandler.class);
        presenterHome = mock(PresenterHome.class);
        activity = Mockito.mock(MainActivity.class);
        when(activity.getFilesDir()).thenReturn(temporaryFolder.newFolder());
    }

    @Test
    public void test_resetCache() throws IOException {
        // Given
        PresenterMain presenter = new PresenterMain(auth, view, userModel, permissionHandler, activity, presenterHome);
        // when
        presenter.onMenuResetCache();
        // then
    }




}