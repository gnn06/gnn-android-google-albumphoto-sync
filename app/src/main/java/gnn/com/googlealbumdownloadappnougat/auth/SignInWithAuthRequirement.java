package gnn.com.googlealbumdownloadappnougat.auth;

import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;

public class SignInWithAuthRequirement extends Require {

    private final AuthManager auth;
    private final IView view;
    final private UserModel userModel;

    public SignInWithAuthRequirement(Exec exec, AuthManager auth, IView view, UserModel userModel) {
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
