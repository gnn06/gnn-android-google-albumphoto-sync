package gnn.com.googlealbumdownloadappnougat.auth;

import gnn.com.googlealbumdownloadappnougat.tasks.SyncTask;
import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;

public class SignInGoogleAPIRequirementBuilder {

    static public Require build(Exec exec, AuthManager auth, IView view, UserModel userModel) {
        Require googleAuth = new GoogleAuthRequirement(exec, auth, view, userModel);
        Require signInReq = new SignInRequirement(googleAuth, auth, view, userModel);
        return signInReq;
    }
}
