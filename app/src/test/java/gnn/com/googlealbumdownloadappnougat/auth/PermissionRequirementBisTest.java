package gnn.com.googlealbumdownloadappnougat.auth;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import static org.mockito.Mockito.*;

public class PermissionRequirementBisTest {

    @Test
    public void exec() {
        Exec exec1 = new Exec(){
            @Override
            public void exec() {
                System.out.println("to exec");
            }
        };
        exec1.exec();
    }

    @Test
    public void reqAndExec_granted() {
        Require req1 = new Require(){
            @Override
            public boolean check() {
                return true;
            }

            @Override
            public void require() {
                System.out.println("require");
            }

            @Override
            public void postRequireSuccess() {
                System.out.println("success");
            }

            @Override
            public void postRequireFailure() {
                System.out.println("failure");
            }
        };
        Require spy1 = spy(req1);
        spy1.exec();
        verify(spy1, Mockito.never()).require();
        verify(spy1, Mockito.never()).postRequireSuccess();
        verify(spy1, Mockito.never()).postRequireFailure();
    }

    @Test
    public void reaAndExec_NotGranted_Success() {
        Require req1 = new Require(){
            @Override
            public boolean check() {
                return false;
            }

            @Override
            public void require() {
                System.out.println("require");
            }

            @Override
            public void postRequireSuccess() {
                System.out.println("success");
            }

            @Override
            public void postRequireFailure() {
                System.out.println("failure");
            }
        };
        Require spy1 = spy(req1);
        spy1.exec();
        spy1.postRequireSuccess();
        verify(spy1).require();
        verify(spy1).postRequireSuccess();
        verify(spy1, Mockito.never()).postRequireFailure();
    }

    @Test
    public void reqAndExec_NotGranted_Failure() {
        Require req1 = new Require(){
            @Override
            public boolean check() {
                return false;
            }

            @Override
            public void require() {
                System.out.println("require");
            }

            @Override
            public void postRequireSuccess() {
                System.out.println("success");
            }

            @Override
            public void postRequireFailure() {
                System.out.println("failure");
            }
        };
        Require spy1 = spy(req1);
        spy1.exec();
        spy1.postRequireFailure();
        verify(spy1).require();
        verify(spy1, Mockito.never()).postRequireSuccess();
        verify(spy1, Mockito.times(1)).postRequireFailure();
    }

}