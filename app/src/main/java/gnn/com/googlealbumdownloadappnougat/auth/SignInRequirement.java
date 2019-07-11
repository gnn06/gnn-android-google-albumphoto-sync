package gnn.com.googlealbumdownloadappnougat.auth;

import javax.annotation.Nonnull;

import gnn.com.googlealbumdownloadappnougat.view.IView;

public class SignInRequirement extends Require {

    private AuthManager auth;
    private IView view;

    public SignInRequirement(@Nonnull Exec exec, AuthManager auth, IView view) {
        super(exec);
        this.auth = auth;
        this.view = view;
    }

    @Override
    public boolean check() {
        return auth.isSignIn();
    }

    @Override
    public void require() {
        view.updateUI_User();
        auth.signInWithPermission();
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