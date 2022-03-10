package gnn.com.googlealbumdownloadappnougat.auth;

import javax.annotation.Nonnull;

import gnn.com.googlealbumdownloadappnougat.UserModel;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;

public class WritePermissionRequirement extends Require {

    private AuthManager auth;
    private IView view;
    final private UserModel userModel;

    public WritePermissionRequirement(@Nonnull Exec exec, AuthManager auth, IView view, UserModel userModel) {
        super(exec);
        this.auth = auth;
        this.view = view;
        this.userModel = userModel;
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
        userModel.getUser().setValue(auth.getAccount());
    }

    @Override
    void postRequireFailure() {
        view.showError("message");
    }

}
