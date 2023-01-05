package gnn.com.googlealbumdownloadappnougat.auth;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;

public class GoogleAuthRequirement extends Require {

    private final AuthManager auth;
    private final IView view;

    public GoogleAuthRequirement(Exec exec, AuthManager auth, IView view) {
        super(exec);
        this.auth = auth;
        this.view = view;
    }

    @Override
    boolean check() {
        return auth.hasGooglePermission();
    }

    @Override
    void require() {
        auth.requestGooglePermission();
    }

    @Override
    void postRequireSuccess() {
        ((MainActivity) view).setWarningPermissionDenied(false);
    }

    @Override
    void postRequireFailure() {}
}
