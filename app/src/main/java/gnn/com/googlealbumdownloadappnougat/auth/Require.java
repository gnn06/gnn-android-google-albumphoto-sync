package gnn.com.googlealbumdownloadappnougat.auth;

public abstract class Require extends Exec {

    public static final int SUCCESS = 1;
    public static final int FAILURE = -1;

    private Exec exec;

    Require(Exec exec) {
        this.exec = exec;
    }

    abstract boolean check();

    abstract void require();

    abstract void postRequireSuccess();

    abstract void postRequireFailure();

    public void exec() {
        if (!check()) {
            require();
        } else {
            if (exec != null) {
                exec.exec();
            }
        }
    }

    public void resumeRequirement(int result) {
        if (result == SUCCESS) {
            postRequireSuccess();
            if (exec != null) {
                exec.exec();
            }
        } else {
            postRequireFailure();
        }
    }
}
