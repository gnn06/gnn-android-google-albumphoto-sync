package gnn.com.googlealbumdownloadappnougat.auth;

import javax.annotation.Nonnull;

import gnn.com.googlealbumdownloadappnougat.view.IView;

public class WritePermission extends Require {

    private AuthManager auth;
    private IView view;

    public WritePermission(@Nonnull Exec exec, AuthManager auth, IView view) {
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
        view.showError();
    }

}
