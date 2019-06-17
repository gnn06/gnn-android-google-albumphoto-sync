package gnn.com.googlealbumdownloadappnougat.presenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.PermissionRequirement;
import gnn.com.googlealbumdownloadappnougat.auth.SignRquirement;
import gnn.com.googlealbumdownloadappnougat.view.IView;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PresenterTest {

    @Test
    public void onSyncClick_not_signin() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        AuthManager auth = Mockito.mock(AuthManager.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity, auth));
        Mockito.when(auth.isSignIn()).thenReturn(false);
        presenter.onSyncClick();
        Mockito.verify(auth).signIn();



    }

    @Test
    public void onSyncClick_alreadySignIn_NotGranted() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        AuthManager auth = Mockito.mock(AuthManager.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity, auth));
        Mockito.when(auth.isSignIn()).thenReturn(true);
        Mockito.when(auth.hasGooglePhotoPermission()).thenReturn(false);
        presenter.onSyncClick();
        Mockito.verify(auth, Mockito.never()).signIn();
        Mockito.verify(auth).requestGooglePhotoPermission();
//        Mockito.verify(presenter, Mockito.never()).laucnhSync();
    }

    @Test
    public void onSyncClick_alreadySignIn_alreadeyGranted() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        AuthManager auth = Mockito.mock(AuthManager.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity, auth));
        Mockito.when(auth.isSignIn()).thenReturn(true);
        Mockito.when(auth.hasGooglePhotoPermission()).thenReturn(true);
//        Mockito.doNothing().when(presenter).laucnhSync();
        presenter.onSyncClick();
        Mockito.verify(auth, Mockito.never()).signIn();
//        Mockito.verify(presenter).laucnhSync();
    }

    @Test
    public void onSyncClick_AllPermissionGranted() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        AuthManager auth = Mockito.mock(AuthManager.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity, auth));
        Whitebox.setInternalState(presenter, "album", "test");
        Mockito.when(auth.isSignIn()).thenReturn(true);
        Mockito.when(auth.hasGooglePhotoPermission()).thenReturn(true);
        Mockito.when(auth.hasWritePermission()).thenReturn(false);
        PermissionRequirement req2 = new PermissionRequirement(null, auth, null) {
            @Override
            public boolean checkRequirement() {
                return true;
            }

            @Override
            public void askAsyncRequirement() {

            }

            @Override
            public void exec() {
                System.out.println("execute req3");
            }
        };
        IView iView = Mockito.mock(IView.class);
        PermissionRequirement req1 = new SignRquirement(req2, auth, iView);
        Mockito.when(presenter.buildNewSyncRequirement()).thenReturn(req1);
        presenter.onSyncClick();
        PermissionRequirement req = (PermissionRequirement)Whitebox.getInternalState(presenter, "permissionRequirement");
        assertNull(req);
    }

}