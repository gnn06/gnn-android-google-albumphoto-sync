package gnn.com.googlealbumdownloadappnougat.auth;

import gnn.com.googlealbumdownloadappnougat.view.IView;

public abstract class PermissionRequirement {

    AuthManager auth;
    IView view;

    public PermissionRequirement(PermissionRequirement nextRequirement, AuthManager auth, IView view) {
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
