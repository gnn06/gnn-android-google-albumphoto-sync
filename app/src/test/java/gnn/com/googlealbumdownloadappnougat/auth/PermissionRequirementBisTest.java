package gnn.com.googlealbumdownloadappnougat.auth;

import org.junit.Test;
import org.mockito.Mockito;

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
    public void requirement_granted() {
        Exec exec = new Exec() {
            @Override
            public void exec() {
                System.out.println("exec");
            }
        };
        Require req1 = new Require(exec){
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
    public void requirement_NotGranted_Success() {
        Exec exec = Mockito.spy(new Exec() {
            @Override
            public void exec() {
                System.out.println("exec");
            }
        });
        Require req1 = new Require(exec){
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
        verify(spy1).require();
        spy1.handleRequirement();
        verify(spy1).postRequireSuccess();
        verify(spy1, Mockito.never()).postRequireFailure();
        verify(exec).exec();
    }

    @Test
    public void requirement_NotGranted_Failure() {
        Exec exec = new Exec() {
            @Override
            public void exec() {
                System.out.println("exec");
            }
        };
        Require req1 = new Require(exec){
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

    @Test
    public void chain_satisfaid_requirement() {
        Exec exec = Mockito.spy(new Exec() {
            @Override
            public void exec() {
                System.out.println("exec");
            }
        });
        Require chain2 = Mockito.spy(new Require(exec) {
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
        });
        Require chain1 = Mockito.spy(new Require(chain2) {
            @Override
            public boolean check() {
                return true;
            }

            @Override
            public void require() {
                System.out.println("require");
            }

            @Override
            void postRequireSuccess() {
                System.out.println("success");
            }

            @Override
            public void postRequireFailure() {
                System.out.println("failure");
            }
        });
        chain1.exec();
        Mockito.verify(chain1, Mockito.never()).require();
        Mockito.verify(chain2, Mockito.never()).require();
        Mockito.verify(chain2, Mockito.times(1)).exec();
        Mockito.verify(exec, Mockito.times(1)).exec();
    }

    @Test
    public void chain_unsatisfaid_requirement() {
        Exec exec = Mockito.spy(new Exec() {
            @Override
            public void exec() {
                System.out.println("exec");
            }
        });
        Require chain2 = Mockito.spy(new Require(exec) {
            @Override
            public boolean check() {
                return false;
            }

            @Override
            public void require() {
                System.out.println("require chain2");
            }

            @Override
            public void postRequireSuccess() {
                System.out.println("success chain2");
            }

            @Override
            public void postRequireFailure() {
                System.out.println("failure");
            }
        });
        Require chain1 = Mockito.spy(new Require(chain2) {
            @Override
            public boolean check() {
                return false;
            }

            @Override
            public void require() {
                System.out.println("require chain1");
            }

            @Override
            public void postRequireSuccess() {
                System.out.println("success chain1");
            }

            @Override
            public void postRequireFailure() {
                System.out.println("failure chain1");
            }
        });
        chain1.exec();
        Mockito.verify(chain1, Mockito.times(1)).require();
        Mockito.verify(chain2, Mockito.never()).exec();
        Mockito.verify(exec, Mockito.never()).exec();
        chain1.handleRequirement();
        Mockito.verify(chain2, Mockito.times(1)).exec();
        Mockito.verify(exec, Mockito.never()).exec();
        chain2.handleRequirement();
        Mockito.verify(exec, Mockito.times(1)).exec();
    }

}