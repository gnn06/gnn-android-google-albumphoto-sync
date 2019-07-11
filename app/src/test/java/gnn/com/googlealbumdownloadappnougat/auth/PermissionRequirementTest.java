package gnn.com.googlealbumdownloadappnougat.auth;

import org.junit.Test;
import org.mockito.Mockito;

import gnn.com.googlealbumdownloadappnougat.view.IView;

import static org.junit.Assert.*;

//public class PermissionRequirementTest {
//
//    @Test
//    public void onSyncClick_AllPermissionGranted() {
//        final AuthManager auth = Mockito.mock(AuthManager.class);
//        Mockito.when(auth.isSignIn()).thenReturn(true);
//        Mockito.when(auth.hasWritePermission()).thenReturn(true);
//        IView view = Mockito.mock(IView.class);
//        PermissionRequirement taskExeq  = Mockito.spy(new PermissionRequirement(null, null, auth) {
//            @Override
//            public boolean checkRequirement() {
//                return true;
//            }
//
//            @Override
//            public void askAsyncRequirement() {
//
//            }
//
//            @Override
//            public void exec() {
//                System.out.println("execute req3");
//            }
//        });
//        PermissionRequirement writeReq  = new WritePermission(taskExeq, auth);
//        PermissionRequirement requirement = new SignInRequirement(writeReq, view, auth);
//
//        PermissionRequirement resulRequirement = requirement.checkAndExec();
//
//        /*
//         * assert that
//         * - requirement chain was empty
//         * - no request permission was done
//         * - the final task was executed
//         */
//        assertNull(resulRequirement);
//        Mockito.verify(auth, Mockito.never()).signInWithPermission();
//        Mockito.verify(auth, Mockito.never()).requestWritePermission();
//        Mockito.verify(taskExeq, Mockito.times(1)).exec();
//    }
//
//    @Test
//    public void onSyncClick_LastPermissionMissing() {
//        AuthManager auth = Mockito.mock(AuthManager.class);
//        Mockito.when(auth.isSignIn()).thenReturn(true);
//        Mockito.when(auth.hasWritePermission()).thenReturn(false);
//
//        IView view = Mockito.mock(IView.class);
//        PermissionRequirement taskExeq  = Mockito.spy(new PermissionRequirement(null, null, auth) {
//            @Override
//            public boolean checkRequirement() {
//                return true;
//            }
//
//            @Override
//            public void askAsyncRequirement() {
//
//            }
//
//            @Override
//            public void exec() {
//                System.out.println("execute req3");
//            }
//        });
//        PermissionRequirement writeReq  = new WritePermission(taskExeq, auth);
//        PermissionRequirement firstReq  = new SignInRequirement(writeReq, view, auth);
//
//        /*
//         * assert that :
//         * - return requirement chain was the last one
//         * - already granted permission was not requested twice
//         * - missing permission was requested
//         */
//        PermissionRequirement resulRequirement = firstReq.checkAndExec();
//        assertEquals(taskExeq, resulRequirement);
//        Mockito.verify(auth, Mockito.never()).signInWithPermission();
//        Mockito.verify(auth, Mockito.times(1)).requestWritePermission();
//
//        /*
//         * assert that :
//         * - there is no more requested permission
//         * - the final task was executed
//         */
//        PermissionRequirement finalRequirement = resulRequirement.checkAndExec();
//        assertNull(finalRequirement);
//        Mockito.verify(auth, Mockito.never()).signInWithPermission();
//        Mockito.verify(auth, Mockito.times(1)).requestWritePermission();
//        Mockito.verify(taskExeq, Mockito.times(1)).exec();
//
//    }
//
//    @Test
//    public void onSyncClick_AllPermissionMissing() {
//        AuthManager auth = Mockito.mock(AuthManager.class);
//        Mockito.when(auth.isSignIn()).thenReturn(false);
//        Mockito.when(auth.hasWritePermission()).thenReturn(false);
//
//        IView view = Mockito.mock(IView.class);
//        PermissionRequirement taskExeq  = new PermissionRequirement(null, null, auth) {
//            @Override
//            public boolean checkRequirement() {
//                return true;
//            }
//
//            @Override
//            public void askAsyncRequirement() {
//
//            }
//
//            @Override
//            public void exec() {
//                System.out.println("execute req3");
//            }
//        };
//        PermissionRequirement writeReq  = new WritePermission(taskExeq, auth);
//        PermissionRequirement firstReq  = new SignInRequirement(writeReq, view, auth);
//
//        PermissionRequirement resulRequirement = firstReq.checkAndExec();
//
//        assertEquals(writeReq, resulRequirement);
//        Mockito.verify(auth, Mockito.times(1)).signInWithPermission();
//        Mockito.verify(auth, Mockito.never()).requestWritePermission();
//
//    }
//
//}