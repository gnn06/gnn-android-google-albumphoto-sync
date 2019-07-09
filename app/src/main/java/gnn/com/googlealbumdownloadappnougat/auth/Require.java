package gnn.com.googlealbumdownloadappnougat.auth;

import javax.annotation.Nonnull;

abstract class Require extends Exec {

    private Exec exec;

    protected Require(@Nonnull Exec exec) {
        assert exec != null;
        this.exec = exec;
    }

    abstract public boolean check();

    abstract public void require();

    public void postRequireSuccess() {
        exec.exec();
    }

    abstract public void postRequireFailure();

    public void exec() {
        if (!check()) {
            require();
        } else {
            exec.exec();
        }
    }
}
