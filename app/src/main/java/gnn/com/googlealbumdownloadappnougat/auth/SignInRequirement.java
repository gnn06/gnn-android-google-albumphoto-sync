package gnn.com.googlealbumdownloadappnougat.auth;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;

public class SignInRequirement extends Require {

    private final AuthManager auth;
    private final IView view;
    final private UserModel userModel;

    public SignInRequirement(Exec exec, AuthManager auth, IView view, UserModel userModel) {
        super(exec);
        this.auth = auth;
        this.view = view;
        this.userModel = userModel;
    }

    @Override
    boolean check() {
        return auth.isSignIn();
    }

    @Override
    void require() {
        auth.signIn();
    }

    @Override
    void postRequireSuccess() {
        GoogleSignInAccount account = auth.getAccount();
        this.userModel.getUser().setValue(account);
    }

    @Override
    void postRequireFailure() {
        view.showError("message");
    }
}
