package gnn.com.googlealbumdownloadappnougat.auth;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.UserModel;

public class WritePermissionRequirementTest {

    @Test
    public void test() {
        AuthManager auth = mock(AuthManager.class);
        MainActivity view = mock(MainActivity.class);
        UserModel userModel = mock(UserModel.class);
        WritePermissionRequirement permission = new WritePermissionRequirement(null, auth, view, userModel);
        permission.postRequireFailure();
        verify(view).showError("message");
    }

}
