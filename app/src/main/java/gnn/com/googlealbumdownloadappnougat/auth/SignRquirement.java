package gnn.com.googlealbumdownloadappnougat.auth;

import gnn.com.googlealbumdownloadappnougat.view.IView;

public class SignRquirement extends PermissionRequirement {

    public SignRquirement(PermissionRequirement nextRequirement, IView view, AuthManager auth) {
        super(nextRequirement, view, auth);
    }

    @Override
    public boolean checkRequirement() {
        return auth.isSignIn();
    }

    @Override
    public void askAsyncRequirement() {
        view.updateUI_User();
        auth.signIn();
    }

    @Override
    public void exec() {

    }
}