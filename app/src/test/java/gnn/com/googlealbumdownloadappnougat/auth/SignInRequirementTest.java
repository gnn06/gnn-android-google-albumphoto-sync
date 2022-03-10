package gnn.com.googlealbumdownloadappnougat.auth;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.UserModel;

public class SignInRequirementTest {

    @Test
    public void test() {
        AuthManager auth = Mockito.mock(AuthManager.class);
        MainActivity view = Mockito.mock(MainActivity.class);
        UserModel userModel = mock(UserModel.class);
        SignInRequirement sign = new SignInRequirement(null, auth, view, userModel);
        sign.postRequireFailure();
        verify(view).showError("message");
    }

}
