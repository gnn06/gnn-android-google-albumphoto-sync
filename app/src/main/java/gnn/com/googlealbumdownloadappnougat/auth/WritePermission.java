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
    public boolean check() {
        return auth.hasWritePermission();
    }

    @Override
    public void require() {
        auth.requestWritePermission();
    }

    @Override
    public void postRequireSuccess() {
        view.updateUI_User();
    }

    @Override
    public void postRequireFailure() {
        view.updateUI_User();
    }

}
