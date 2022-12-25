package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.auth.PersistOauthError;
import gnn.com.googlealbumdownloadappnougat.ui.FolderModel;
import gnn.com.googlealbumdownloadappnougat.ui.UserModel;

@RunWith(RobolectricTestRunner.class)
public class PresenterHomeRoboTest {

    private PresenterHome presenter;
    private FragmentHome view;
    private MainActivity activity;
    private FragmentHome fragmentHome;
    private UserModel userModel;
    private FolderModel folderModel;
    private ApplicationContext context;

    @Before
    public void setUp() throws Exception {
        context = ApplicationContext.getInstance(ApplicationProvider.getApplicationContext());
        view = mock(FragmentHome.class);
        userModel = mock(UserModel.class);
        folderModel = mock(FolderModel.class);
        fragmentHome = mock(FragmentHome.class);
        activity = mock(MainActivity.class);
        presenter = new PresenterHome(view, activity, fragmentHome, userModel, folderModel);
    }

    @Test
    public void foreground() {
        // when call init
        presenter.onAppForeground();

        // then have called update_UI and updateUI_lastSyncTime
        verify(fragmentHome).updateUI_User();
        verify(fragmentHome).updateUI_lastSyncTime(null, null, null);
        verify(fragmentHome).updateUI_CallResult(any(), any());
    }

    @Test
    public void warning_onerror() throws IOException {
        // given
        // - an error
        new PersistOauthError(context.getProcessFolder()).storeTime();
        // when call init
        presenter.onAppForeground();
        // then
        // - warning called with true
        verify(fragmentHome).setWarningPermissionDenied(true);
    }}
