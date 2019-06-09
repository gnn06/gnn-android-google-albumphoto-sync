package gnn.com.googlealbumdownloadappnougat.auth;

import org.junit.Test;
import org.mockito.Mockito;

import gnn.com.googlealbumdownloadappnougat.view.IView;

import static org.junit.Assert.*;

public class PermissionRequirementTest {

    class TaskExec extends PermissionRequirement {
        public TaskExec() {
            super(null, null, null);
        }

        @Override
        public boolean checkRequirement() {
            return true;
        }

        @Override
        public void askAsyncRequirement() {

        }

        @Override
        public void exec() {
            System.out.println("execute");
        }
    }

    @Test
    public void checkAndExec() {
        AuthManager auth = Mockito.mock(AuthManager.class);
        IView view = Mockito.mock(IView.class);

        Mockito.when(auth.isSignIn()).thenReturn(true);
        Mockito.when(auth.hasGooglePhotoPermission()).thenReturn(true);
        Mockito.when(auth.hasWritePermission()).thenReturn(true);

        PermissionRequirement taskExeq  = Mockito.spy(new TaskExec());
        PermissionRequirement writeReq  = Mockito.spy(new WritePermission(taskExeq, auth));
        PermissionRequirement photoReq  = Mockito.spy(new GooglePhotoAPI_Requirement(writeReq, auth, view));
        PermissionRequirement signInReq = Mockito.spy(new SignRquirement(photoReq, auth, view));

        PermissionRequirement requirement = signInReq.checkAndExec();
        Mockito.doNothing().when(signInReq).askAsyncRequirement();
        Mockito.doNothing().when(photoReq).askAsyncRequirement();
        Mockito.doNothing().when(writeReq).askAsyncRequirement();
        Mockito.verify(taskExeq, Mockito.times(1)).exec();

    }

    @Test
    public void checkAndExec_noWritePermission() {
        AuthManager auth = Mockito.mock(AuthManager.class);
        IView view = Mockito.mock(IView.class);

        Mockito.when(auth.isSignIn()).thenReturn(true);
        Mockito.when(auth.hasGooglePhotoPermission()).thenReturn(true);
        Mockito.when(auth.hasWritePermission()).thenReturn(false);

        PermissionRequirement taskExeq  = Mockito.spy(new TaskExec());
        PermissionRequirement writeReq  = Mockito.spy(new WritePermission(taskExeq, auth));
        PermissionRequirement photoReq  = Mockito.spy(new GooglePhotoAPI_Requirement(writeReq, auth, view));
        PermissionRequirement signInReq = Mockito.spy(new SignRquirement(photoReq, auth, view));

        PermissionRequirement requirement = signInReq.checkAndExec();
        Mockito.doNothing().when(signInReq).askAsyncRequirement();
        Mockito.doNothing().when(photoReq).askAsyncRequirement();
        Mockito.doNothing().when(writeReq).askAsyncRequirement();
        Mockito.doNothing().when(taskExeq).exec();
        assertNotNull(requirement);
        assertEquals(taskExeq, requirement);

    }
}