package gnn.com.googlealbumdownloadappnougat.auth;

import javax.annotation.Nonnull;

import gnn.com.googlealbumdownloadappnougat.ui.view.IView;

public class WritePermissionRequirement extends Require {

    private AuthManager auth;
    private IView view;

    public WritePermissionRequirement(@Nonnull Exec exec, AuthManager auth, IView view) {
        super(exec);
        this.auth = auth;
        this.view = view;
    }

    @Override
    boolean check() {
        return auth.hasWritePermission();
    }

    @Override
    void require() {
        auth.requestWritePermission();
    }

    @Override
    void postRequireSuccess() {
        view.updateUI_User();
    }

    @Override
    void postRequireFailure() {
        view.showError("message");
    }

}
