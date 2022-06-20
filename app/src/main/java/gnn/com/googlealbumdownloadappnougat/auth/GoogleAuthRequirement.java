package gnn.com.googlealbumdownloadappnougat.auth;

import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;

public class GoogleAuthRequirement extends Require {

    private AuthManager auth;
    private IView view;
    final private UserModel userModel;

    public GoogleAuthRequirement(Exec exec, AuthManager auth, IView view, UserModel userModel) {
        super(exec);
        this.auth = auth;
        this.view = view;
        this.userModel = userModel;
    }

    @Override
    boolean check() {
        return auth.hasGooglePermission();
    }

    @Override
    void require() {
        this.userModel.getUser().setValue(null);
        auth.signInWithPermission();
    }

    @Override
    void postRequireSuccess() {
        userModel.getUser().setValue(auth.getAccount());
    }

    @Override
    void postRequireFailure() {
        view.showError("message");
    }
}
