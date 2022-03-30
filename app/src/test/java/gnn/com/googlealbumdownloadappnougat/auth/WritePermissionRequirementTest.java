package gnn.com.googlealbumdownloadappnougat.auth;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.FragmentHome;
import gnn.com.googlealbumdownloadappnougat.ui.UserModel;

public class WritePermissionRequirementTest {

    @Test
    public void test() {
        AuthManager auth = mock(AuthManager.class);
        FragmentHome view = mock(FragmentHome.class);
        UserModel userModel = mock(UserModel.class);
        WritePermissionRequirement permission = new WritePermissionRequirement(null, auth, view, userModel);
        permission.postRequireFailure();
        verify(view).showError("message");
    }

}
