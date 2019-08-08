package gnn.com.googlealbumdownloadappnougat.auth;

import org.junit.Test;
import org.mockito.Mockito;

import gnn.com.googlealbumdownloadappnougat.MainActivity;

import static org.mockito.Mockito.verify;

public class SignInRequirementTest {

    @Test
    public void test() {
        AuthManager auth = Mockito.mock(AuthManager.class);
        MainActivity view = Mockito.mock(MainActivity.class);
        SignInRequirement sign = new SignInRequirement(null, auth, view);
        sign.postRequireFailure();
        verify(view).showError();
    }

}