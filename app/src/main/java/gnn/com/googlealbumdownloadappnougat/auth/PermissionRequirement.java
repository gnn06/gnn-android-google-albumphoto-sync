package gnn.com.googlealbumdownloadappnougat.auth;

import gnn.com.googlealbumdownloadappnougat.view.IView;

public abstract class PermissionRequirement {

    AuthManager auth;
    IView view;

    protected PermissionRequirement(PermissionRequirement nextRequirement, IView view, AuthManager auth) {
        this.nextRequirement = nextRequirement;
        this.auth = auth;
        this.view = view;
    }

    private PermissionRequirement nextRequirement;

    public abstract boolean checkRequirement();

    public abstract void askAsyncRequirement();

    public abstract void exec();

    public PermissionRequirement checkAndExec() {
        if (!checkRequirement()) {
            askAsyncRequirement();
            return nextRequirement;
        }
        if (nextRequirement != null) {
            return nextRequirement.checkAndExec();
        } else {
            exec();
            return null;
        }
    }
}
