package gnn.com.googlealbumdownloadappnougat.presenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.GooglePhotoAPI_Requirement;
import gnn.com.googlealbumdownloadappnougat.auth.PermissionRequirement;
import gnn.com.googlealbumdownloadappnougat.auth.SignRquirement;
import gnn.com.googlealbumdownloadappnougat.auth.WritePermission;
import gnn.com.googlealbumdownloadappnougat.view.IView;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PresenterTest {

    @Test
    public void onSyncClick_NoAlbum() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity));
        Whitebox.setInternalState(presenter, "album", "");
        IView view = Mockito.spy(IView.class);
        presenter.onSyncClick();
        Mockito.verify(view, Mockito.never()).alertNoAlbum();
    }

    @Test
    public void onSyncClick_AllPermissionGranted() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity));
        AuthManager auth = Mockito.mock(AuthManager.class);
        Whitebox.setInternalState(presenter, "album", "test");
        Mockito.when(auth.isSignIn()).thenReturn(true);
        Mockito.when(auth.hasGooglePhotoPermission()).thenReturn(true);
        Mockito.when(auth.hasWritePermission()).thenReturn(true);
        PermissionRequirement requirement = getPermissionRequirement(auth);
        PermissionRequirement resulRequirement = requirement.checkAndExec();
        assertNull(resulRequirement);
    }

    @Test
    public void onSyncClick_LastPermissionMissing() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity));
        AuthManager auth = Mockito.mock(AuthManager.class);
        Whitebox.setInternalState(presenter, "album", "test");
        Mockito.when(auth.isSignIn()).thenReturn(true);
        Mockito.when(auth.hasGooglePhotoPermission()).thenReturn(true);
        Mockito.when(auth.hasWritePermission()).thenReturn(false);

        IView view = Mockito.mock(IView.class);
        PermissionRequirement taskExeq  = new PermissionRequirement(null, null, auth) {
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
        PermissionRequirement writeReq  = new WritePermission(taskExeq, auth);
        PermissionRequirement photoReq  = new GooglePhotoAPI_Requirement(writeReq, view, auth);
        PermissionRequirement firstReq  = new SignRquirement(photoReq, view, auth);

        PermissionRequirement resulRequirement = firstReq.checkAndExec();
        assertEquals(taskExeq, resulRequirement);
        Mockito.verify(auth, Mockito.times(1)).requestWritePermission();
    }

    private PermissionRequirement getPermissionRequirement(final AuthManager auth) {
        IView view = Mockito.mock(IView.class);
        PermissionRequirement taskExeq  = new PermissionRequirement(null, null, auth) {
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
        PermissionRequirement writeReq  = new WritePermission(taskExeq, auth);
        PermissionRequirement photoReq  = new GooglePhotoAPI_Requirement(writeReq, view, auth);
        return new SignRquirement(photoReq, view, auth);
    }

}