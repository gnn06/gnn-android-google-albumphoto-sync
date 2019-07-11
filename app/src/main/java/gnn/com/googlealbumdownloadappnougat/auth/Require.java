package gnn.com.googlealbumdownloadappnougat.auth;

public abstract class Require extends Exec {

    private Exec exec;

    protected Require(Exec exec) {
        this.exec = exec;
    }

    abstract public boolean check();

    abstract public void require();

    abstract public void postRequireSuccess();

    abstract public void postRequireFailure();

    public void exec() {
        if (!check()) {
            require();
        } else {
            if (exec != null) {
                exec.exec();
            }
        }
    }

    public void resumeRequirement() {
        postRequireSuccess();
        if (exec != null) {
            exec.exec();
        }
    }
}
