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
        Whitebox.setInternalState(presenter, "album", null);
        IView view = Mockito.spy(IView.class);
        presenter.onSyncClick();
        Mockito.verify(view, Mockito.never()).alertNoAlbum();
    }

//    @Test
//    public void onSyncClick_init() {
//        MainActivity activity = Mockito.mock(MainActivity.class);
//        Presenter presenter = Mockito.spy(new Presenter(activity, activity));
//        Whitebox.setInternalState(presenter, "album", "test");
//        presenter.onSyncClick();
//
//    }

    @Test
    public void onSyncClick_AllPermissionGranted() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity));
        final AuthManager auth = Mockito.mock(AuthManager.class);
        Whitebox.setInternalState(presenter, "album", "test");
        Mockito.when(auth.isSignIn()).thenReturn(true);
        Mockito.when(auth.hasGooglePhotoPermission()).thenReturn(true);
        Mockito.when(auth.hasWritePermission()).thenReturn(true);
        IView view = Mockito.mock(IView.class);
        PermissionRequirement taskExeq  = Mockito.spy(new PermissionRequirement(null, null, auth) {
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
        });
        PermissionRequirement writeReq  = new WritePermission(taskExeq, auth);
        PermissionRequirement photoReq  = new GooglePhotoAPI_Requirement(writeReq, view, auth);
        PermissionRequirement requirement = new SignRquirement(photoReq, view, auth);

        PermissionRequirement resulRequirement = requirement.checkAndExec();

        /*
         * assert that
         * - requirement chain was empty
         * - no request permission was done
         * - the final task was executed
         */
        assertNull(resulRequirement);
        Mockito.verify(auth, Mockito.never()).signIn();
        Mockito.verify(auth, Mockito.never()).requestGooglePhotoPermission();
        Mockito.verify(auth, Mockito.never()).requestWritePermission();
        Mockito.verify(taskExeq, Mockito.times(1)).exec();
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
        PermissionRequirement taskExeq  = Mockito.spy(new PermissionRequirement(null, null, auth) {
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
        });
        PermissionRequirement writeReq  = new WritePermission(taskExeq, auth);
        PermissionRequirement photoReq  = new GooglePhotoAPI_Requirement(writeReq, view, auth);
        PermissionRequirement firstReq  = new SignRquirement(photoReq, view, auth);

        /*
         * assert that :
         * - return requirement chain was the last one
         * - already granted permission was not requested twice
         * - missing permission was requested
         */
        PermissionRequirement resulRequirement = firstReq.checkAndExec();
        assertEquals(taskExeq, resulRequirement);
        Mockito.verify(auth, Mockito.never()).signIn();
        Mockito.verify(auth, Mockito.never()).requestGooglePhotoPermission();
        Mockito.verify(auth, Mockito.times(1)).requestWritePermission();

        /*
         * assert that :
         * - there is no more requested permission
         * - the final task was executed
         */
        PermissionRequirement finalRequirement = resulRequirement.checkAndExec();
        assertNull(finalRequirement);
        Mockito.verify(auth, Mockito.never()).signIn();
        Mockito.verify(auth, Mockito.never()).requestGooglePhotoPermission();
        Mockito.verify(auth, Mockito.times(1)).requestWritePermission();
        Mockito.verify(taskExeq, Mockito.times(1)).exec();

    }

    @Test
    public void onSyncClick_AllPermissionMissing() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity));
        AuthManager auth = Mockito.mock(AuthManager.class);
        Whitebox.setInternalState(presenter, "album", "test");
        Mockito.when(auth.isSignIn()).thenReturn(false);
        Mockito.when(auth.hasGooglePhotoPermission()).thenReturn(false);
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
        PermissionRequirement secondReq  = new GooglePhotoAPI_Requirement(writeReq, view, auth);
        PermissionRequirement firstReq  = new SignRquirement(secondReq, view, auth);

        PermissionRequirement resulRequirement = firstReq.checkAndExec();

        assertEquals(secondReq, resulRequirement);
        Mockito.verify(auth, Mockito.times(1)).signIn();
        Mockito.verify(auth, Mockito.never()).requestGooglePhotoPermission();
        Mockito.verify(auth, Mockito.never()).requestWritePermission();

    }

}