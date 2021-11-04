package gnn.com.googlealbumdownloadappnougat.auth;

import gnn.com.googlealbumdownloadappnougat.ui.view.IView;

public class SignInAndGoogleAuthRequirement extends Require {

    private AuthManager auth;
    private IView view;

    public SignInAndGoogleAuthRequirement(Exec exec, AuthManager auth, IView view) {
        super(exec);
        this.auth = auth;
        this.view = view;
    }

    @Override
    boolean check() {
        return auth.isSignIn() && auth.hasScope();
    }

    @Override
    void require() {
        view.updateUI_User();
        auth.signInWithPermission();
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