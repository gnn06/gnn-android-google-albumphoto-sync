package gnn.com.googlealbumdownloadappnougat.auth;

abstract class Require {

    abstract public boolean check();

    abstract public void require();

    abstract public void postRequireSuccess();

    abstract public void postRequireFailure();

    public void exec() {
        if (!check()) {
            require();
        }
    }
}
