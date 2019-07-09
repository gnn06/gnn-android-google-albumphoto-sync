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
        };
        Require spy1 = spy(req1);
        spy1.exec();
        verify(spy1, Mockito.never()).require();
    }

    @Test
    public void reqAndExec_NotGranted() {
        Require req1 = new Require(){
            @Override
            public boolean check() {
                return false;
            }

            @Override
            public void require() {
                System.out.println("require");
            }
        };
        Require spy1 = spy(req1);
        spy1.exec();
        verify(spy1).require();
    }

}