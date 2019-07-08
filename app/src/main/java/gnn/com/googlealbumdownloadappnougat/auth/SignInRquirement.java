package gnn.com.googlealbumdownloadappnougat.auth;

import gnn.com.googlealbumdownloadappnougat.view.IView;

public class SignInRquirement extends PermissionRequirement {

    public SignInRquirement(PermissionRequirement nextRequirement, IView view, AuthManager auth) {
        super(nextRequirement, view, auth);
    }

    @Override
    public boolean checkRequirement() {
        return auth.isSignIn();
    }

    @Override
    public void askAsyncRequirement() {
        view.updateUI_User();
        auth.signInWithPermission();
    }

    @Override
    public void exec() {

    }
}
