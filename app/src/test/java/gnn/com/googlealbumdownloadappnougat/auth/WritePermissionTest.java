package gnn.com.googlealbumdownloadappnougat.auth;

import org.junit.Test;
import org.mockito.Mockito;

import gnn.com.googlealbumdownloadappnougat.MainActivity;

import static org.mockito.Mockito.verify;

public class WritePermissionTest {

    @Test
    public void test() {
        AuthManager auth = Mockito.mock(AuthManager.class);
        MainActivity view = Mockito.mock(MainActivity.class);
        WritePermission permission = new WritePermission(null, auth, view);
        permission.postRequireFailure();
        verify(view).showError("message");
    }

}