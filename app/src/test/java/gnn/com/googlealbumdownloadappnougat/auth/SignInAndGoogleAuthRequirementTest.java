package gnn.com.googlealbumdownloadappnougat.auth;

import org.junit.Test;
import org.mockito.Mockito;

import gnn.com.googlealbumdownloadappnougat.MainActivity;

import static org.mockito.Mockito.verify;

public class SignInAndGoogleAuthRequirementTest {

    @Test
    public void test() {
        AuthManager auth = Mockito.mock(AuthManager.class);
        MainActivity view = Mockito.mock(MainActivity.class);
        SignInAndGoogleAuthRequirement sign = new SignInAndGoogleAuthRequirement(null, auth, view);
        sign.postRequireFailure();
        verify(view).showError("message");
    }

}