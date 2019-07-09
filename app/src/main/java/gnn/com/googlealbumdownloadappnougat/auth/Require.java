package gnn.com.googlealbumdownloadappnougat.auth;

abstract class Require {

    abstract public boolean check();

    abstract public void require();

    public void exec() {
        if (!check()) {
            require();
        }
    }
}
